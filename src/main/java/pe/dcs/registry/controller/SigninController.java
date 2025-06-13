package pe.dcs.registry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.dcs.registry.entity.Usuario;
import pe.dcs.registry.payload.request.SigninRequest;
import pe.dcs.registry.payload.response.SigninResponse;
import pe.dcs.registry.payload.response.other.BodyResponse;
import pe.dcs.registry.payload.response.other.ServiceResponse;
import pe.dcs.registry.security.jwt.JwtProvider;
import pe.dcs.registry.security.payload.JwtTimesResponse;
import pe.dcs.registry.security.service.UserDetailsImpl;
import pe.dcs.registry.service.IUsuarioService;
import pe.dcs.registry.util.DataConverter;

import java.util.Optional;

import static pe.dcs.registry.constant.ControllerConstant.FORMAT_REQUEST;

@RestController
@RequestMapping("/signin")
@CrossOrigin
public class SigninController {

    private static final Logger logger = LogManager.getLogger(SigninController.class);

    private static final String INICIO_TITULO_SIGNIN_SERVICE = "---- Inicio de Servicio: Signin ----";
    private static final String FIN_TITULO_SIGNIN_SERVICE = "---- Fin de Servicio: Signin ----";

    final DaoAuthenticationProvider daoAuthenticationProvider;
    final JwtProvider jwtProvider;
    final AuthenticationManager authenticationManager;

    final IUsuarioService usuarioService;

    public SigninController(DaoAuthenticationProvider daoAuthenticationProvider, JwtProvider jwtProvider, AuthenticationManager authenticationManager,
                            IUsuarioService usuarioService) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
    }

    private static ServiceResponse getServiceResponse(String jwt, JwtTimesResponse jwtTimesResponse, UserDetailsImpl userDetails) {

        SigninResponse signinResponse = new SigninResponse(
                jwt,
                jwtTimesResponse.getEmision(),
                jwtTimesResponse.getExpiracion(),
                userDetails.getUsername(),
                userDetails.getAuthorities()
        );

        BodyResponse bodyResponse = new BodyResponse(signinResponse);

        return new ServiceResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                bodyResponse
        );
    }

    @PostMapping("/v1")
    public ResponseEntity<ServiceResponse> signin(@Valid @RequestBody SigninRequest signinRequest) throws JsonProcessingException {

        logger.info(INICIO_TITULO_SIGNIN_SERVICE);

        logger.info("JSON Request: ");
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonBody = objectWriter.writeValueAsString(signinRequest);
        logger.info(jsonBody);

        logger.info(FORMAT_REQUEST);
        String usernameUsuario = DataConverter.formatTextTrim(signinRequest.getUsername());
        String passwordUsuario = DataConverter.formatTextTrim(signinRequest.getPassword());

        logger.info("Buscando el Usuario: {}", usernameUsuario);
        Optional<Usuario> dataUsuario = usuarioService.findUsuarioToAuthentication(usernameUsuario);

        if (dataUsuario.isEmpty()) {
            logger.error("No se encontró el Usuario: {}", usernameUsuario);

            ServiceResponse serviceResponse = new ServiceResponse(
                    "Ocurrió un error al encontrar el Usuario en el Sistema.",
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase()
            );

            logger.info(FIN_TITULO_SIGNIN_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.NOT_FOUND);
        }

        logger.info("Se encontró el Usuario con username: {}", usernameUsuario);
        Usuario usuario = dataUsuario.get();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                usuario.getUsername(),
                passwordUsuario
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!Boolean.TRUE.equals(userDetails.getHabilitado())) {
            logger.error("El Usuario no se encuentra habilitado");

            ServiceResponse serviceResponse = new ServiceResponse(
                    "El Usuario no se encuentra habilitado para uso.",
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase()
            );

            logger.info(FIN_TITULO_SIGNIN_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.UNAUTHORIZED);
        }

        logger.info("El Usuario se encuentra habilitado");

        String jwt = jwtProvider.generateJWT(authentication);

        JwtTimesResponse jwtTimesResponse = jwtProvider.getTimesFromJWT(jwt);

        if (jwtTimesResponse == null) {
            logger.error("Ocurrió un error al consultar las fechas del JWT");

            ServiceResponse serviceResponse = new ServiceResponse(
                    "Ocurrió un error al obtener las Fechas de Registro y Expiración del JWT.",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
            );

            logger.info(FIN_TITULO_SIGNIN_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ServiceResponse serviceResponse = getServiceResponse(jwt, jwtTimesResponse, userDetails);

        logger.info(FIN_TITULO_SIGNIN_SERVICE);

        return new ResponseEntity<>(serviceResponse,
                HttpStatus.OK);
    }
}
