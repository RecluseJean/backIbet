package pe.dcs.registry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.dcs.registry.constant.GeneralConstant;
import pe.dcs.registry.entity.Rol;
import pe.dcs.registry.entity.Usuario;
import pe.dcs.registry.payload.request.GuardarUsuarioRequest;
import pe.dcs.registry.payload.response.other.ServiceResponse;
import pe.dcs.registry.service.IRolService;
import pe.dcs.registry.service.IUsuarioService;
import pe.dcs.registry.util.DataConverter;
import pe.dcs.registry.validation.UsuarioValidation;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static pe.dcs.registry.constant.ControllerConstant.*;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
public class UsuarioController {

    private static final Logger logger = LogManager.getLogger(UsuarioController.class);

    private static final String INICIO_TITULO_GUARDAR_ENCARGADO_SERVICE = "---- Inicio de Servicio: Guardar Encargado ----";
    private static final String FIN_TITULO_GUARDAR_ENCARGADO_SERVICE = "---- Fin de Servicio: Guardar Encargado ----";

    final IUsuarioService usuarioService;
    final IRolService rolService;

    final PasswordEncoder passwordEncoder;

    public UsuarioController(IUsuarioService usuarioService, IRolService rolService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/v1/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> guardarUsuario(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                          @Valid @RequestBody GuardarUsuarioRequest guardarUsuarioRequest) throws JsonProcessingException {

        logger.info(INICIO_TITULO_GUARDAR_ENCARGADO_SERVICE);

        logger.info(VALIDACION_AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_GUARDAR_ENCARGADO_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.BAD_REQUEST);
        }

        String authorizationHeader = DataConverter.formatTextTrim(authorization);
        logger.info(CON_AUTHORIZATION_HEADER, authorizationHeader);

        //Get JSON Request
        logger.info("JSON Request: ");
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonBody = objectWriter.writeValueAsString(guardarUsuarioRequest);
        logger.info(jsonBody);

        logger.info(FORMAT_REQUEST);
        String usernameUsuario = DataConverter.formatTextTrim(guardarUsuarioRequest.getUsername());
        String passwordUsuario = DataConverter.formatTextTrim(guardarUsuarioRequest.getPassword());

        logger.info("Inicio de Validación");
        Set<UsuarioValidation> validationListUsuario = new HashSet<>();

        usuarioService.findUsuarioToValidate(usernameUsuario)
                .forEach(usuario -> validationListUsuario.add(new UsuarioValidation(usernameUsuario)));

        if (!validationListUsuario.isEmpty()) {
            logger.error("Usuario con Username: {} ya existente", usernameUsuario);

            ServiceResponse serviceResponse = new ServiceResponse(
                    "El Usuario ya fue registrado previamente.",
                    HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT.getReasonPhrase()
            );

            logger.info(FIN_TITULO_GUARDAR_ENCARGADO_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.CONFLICT);
        }

        logger.info("Usuario con Username: {} no existente", usernameUsuario);
        logger.info("Se continúa con el proceso de registro");

        logger.info("Buscando el Rol: {}", GeneralConstant.ROL_ENCARGADO);
        Optional<Rol> dataRol = rolService.findRolByDescripcion(GeneralConstant.ROL_ENCARGADO);

        if (dataRol.isEmpty()) {
            logger.error("No se encontró el Rol: {}", GeneralConstant.ROL_ENCARGADO);

            ServiceResponse serviceResponse = new ServiceResponse(
                    "Ocurrió un error al encontrar el Rol.",
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase()
            );

            logger.info(FIN_TITULO_GUARDAR_ENCARGADO_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.NOT_FOUND);
        }

        logger.info("Se encontró el Rol: {}", GeneralConstant.ROL_ENCARGADO);
        Rol rol = dataRol.get();

        Set<Rol> listaRoles = new HashSet<>();
        listaRoles.add(rol);

        Usuario usuario = new Usuario(
                usernameUsuario,
                passwordEncoder.encode(passwordUsuario),
                GeneralConstant.FECHA_HORA_LOCAL,
                true,
                listaRoles
        );

        usuarioService.saveUsuario(usuario);
        logger.info("Se guardó el Usuario");

        ServiceResponse serviceResponse = new ServiceResponse(
                "Se guardó el Usuario correctamente.",
                HttpStatus.ACCEPTED.value(),
                HttpStatus.ACCEPTED.getReasonPhrase()
        );

        logger.info(FIN_TITULO_GUARDAR_ENCARGADO_SERVICE);

        return new ResponseEntity<>(serviceResponse,
                HttpStatus.ACCEPTED);
    }
}
