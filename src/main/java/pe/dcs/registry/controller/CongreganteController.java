package pe.dcs.registry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.dcs.registry.constant.GeneralConstant;
import pe.dcs.registry.constant.RegexConstant;
import pe.dcs.registry.entity.Congregante;
import pe.dcs.registry.helper.GetExcelDataHelper;
import pe.dcs.registry.payload.request.ActualizarCongreganteRequest;
import pe.dcs.registry.payload.request.GuardarCongreganteRequest;
import pe.dcs.registry.payload.response.CongreganteResponse;
import pe.dcs.registry.payload.response.other.BodyResponse;
import pe.dcs.registry.payload.response.other.PaginationResponse;
import pe.dcs.registry.payload.response.other.ServiceResponse;
import pe.dcs.registry.service.ICongreganteService;
import pe.dcs.registry.util.DataConverter;
import pe.dcs.registry.validation.CongreganteValidation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pe.dcs.registry.constant.ControllerConstant.*;

@RestController
@RequestMapping("/congregante")
@CrossOrigin
public class CongreganteController {

    private static final Logger logger = LogManager.getLogger(CongreganteController.class);

    private static final String INICIO_TITULO_CARGA_MASIVA_CONGREGANTES_SERVICE = "---- Inicio de Servicio: Cargar Masivo Congregantes ----";
    private static final String FIN_TITULO_CARGA_MASIVA_CONGREGANTES_SERVICE = "---- Fin de Servicio: Cargar Masivo Congregantes ----";
    private static final String INICIO_TITULO_GUARDAR_CONGREGANTE_SERVICE = "---- Inicio de Servicio: Guardar Congregante ----";
    private static final String FIN_TITULO_GUARDAR_CONGREGANTE_SERVICE = "---- Fin de Servicio: Guardar Congregante ----";
    private static final String INICIO_TITULO_MOSTRAR_CONGREGANTES_SERVICE = "---- Inicio de Servicio: Mostrar Congregantes ----";
    private static final String FIN_TITULO_MOSTRAR_CONGREGANTES_SERVICE = "---- Fin de Servicio: Mostrar Congregantes ----";
    private static final String INICIO_TITULO_BUSCAR_CONGREGANTES_SERVICE = "---- Inicio de Servicio: Buscar Congregantes ----";
    private static final String FIN_TITULO_BUSCAR_CONGREGANTES_SERVICE = "---- Fin de Servicio: Buscar Congregantes ----";
    private static final String INICIO_TITULO_BUSCAR_CONGREGANTE_POR_ID_SERVICE = "---- Inicio de Servicio: Buscar Congregante por ID ----";
    private static final String FIN_TITULO_BUSCAR_CONGREGANTE_POR_ID_SERVICE = "---- Fin de Servicio: Buscar Congregante por ID ----";
    private static final String INICIO_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE = "---- Inicio de Servicio: Actualizar Congregante ----";
    private static final String FIN_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE = "---- Fin de Servicio: Actualizar Congregante ----";

    final ICongreganteService congreganteService;

    public CongreganteController(ICongreganteService congreganteService) {
        this.congreganteService = congreganteService;
    }

    private static ServiceResponse mostrarListaCongregantesResponse(int sizeCongregantes, Set<CongreganteResponse> listCongregantesResponse, int totalRegistros, int currentPage) {

        int totalPages;

        totalPages = (int) Math.ceil((double) totalRegistros / sizeCongregantes);

        PaginationResponse paginationResponse = new PaginationResponse(
                totalRegistros,
                totalPages,
                sizeCongregantes,
                currentPage
        );

        BodyResponse bodyResponse = new BodyResponse(
                paginationResponse,
                listCongregantesResponse
        );

        return new ServiceResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                bodyResponse
        );
    }

    private static ServiceResponse mostrarDetalleCongreganteServiceResponse(Congregante congregante) {

        //Asignando Fecha de Modificacion
        LocalDate fechaModificacion;

        if (congregante.getFechaModificacion() == null) {
            fechaModificacion = congregante.getFechaRegistro().toLocalDate();
        } else {
            fechaModificacion = congregante.getFechaModificacion().toLocalDate();
        }

        CongreganteResponse congreganteResponse = new CongreganteResponse(
                congregante.getId(),
                congregante.getApellido(),
                congregante.getNombre(),
                congregante.getSexo(),
                congregante.getTelefono(),
                congregante.getDireccion(),
                congregante.getMesCumpleanios(),
                congregante.getFechaNacimiento(),
                congregante.getEdad(),
                congregante.getEstadoCivil(),
                congregante.getCantidadHijo(),
                congregante.getTiempoIBET(),
                congregante.getIglesiaAnterior(),
                congregante.getBautizado(),
                congregante.getIglesiaBautizo(),
                congregante.getTiempoBautizo(),
                congregante.getEstudiando(),
                congregante.getCurso(),
                congregante.getCursoUltimo(),
                congregante.getTiempoSinEstudio(),
                congregante.getMotivoSinEstudio(),
                congregante.getParticipandoGPC(),
                congregante.getMotivoNoGPC(),
                congregante.getNumeroGPC(),
                congregante.getEnMinisterio(),
                congregante.getMinisterio(),
                congregante.getCargo(),
                congregante.getEstudiandoEscuelaCiervo(),
                congregante.getCursoEscuelaCiervo(),
                congregante.getCursoUltimoEscuelaCiervo(),
                congregante.getTiempoSinEstudioEscuelaCiervo(),
                congregante.getMotivoSinEstudioEscuelaCiervo(),
                congregante.getFechaRegistro().toLocalDate(),
                fechaModificacion,
                congregante.getHabilitado()
        );

        BodyResponse bodyResponse = new BodyResponse(congreganteResponse);

        return new ServiceResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                bodyResponse
        );
    }

    @PostMapping("/v1/bulk-load")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENCARGADO')")
    public ResponseEntity<ServiceResponse> cargaMasivaCongregantes(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                                   @RequestPart("excel") MultipartFile excel) throws IOException {

        logger.info(INICIO_TITULO_CARGA_MASIVA_CONGREGANTES_SERVICE);

        logger.info(VALIDACION_AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_CARGA_MASIVA_CONGREGANTES_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.BAD_REQUEST);
        }

        String authorizationHeader = DataConverter.formatTextTrim(authorization);
        logger.info(CON_AUTHORIZATION_HEADER, authorizationHeader);

        logger.info("Validando Archivo Excel");
        if (!GetExcelDataHelper.esFormatoExcel(excel)) {
            logger.error("No es un Archivo Excel");

            ServiceResponse serviceResponse = new ServiceResponse(
                    "El tipo de archivo no corresponde a uno de Excel.",
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_CARGA_MASIVA_CONGREGANTES_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.BAD_REQUEST);
        }

        logger.info("Es un Archivo Excel");

        Set<GuardarCongreganteRequest> listaCongregantesRequest = GetExcelDataHelper.getCongregantesFromExcel(excel.getInputStream());
        logger.info("Cantidad de Registros del Excel: {}", listaCongregantesRequest.size());

        for (GuardarCongreganteRequest guardarCongreganteRequest : listaCongregantesRequest) {
            logger.info(INICIO_VALIDACION);
            // 🚫 Validación para evitar guardar filas vacías
            if ((guardarCongreganteRequest.getApellido() == null || guardarCongreganteRequest.getApellido().trim().isEmpty()) &&
                    (guardarCongreganteRequest.getNombre() == null || guardarCongreganteRequest.getNombre().trim().isEmpty())) {
                logger.warn("Fila omitida: Apellido y Nombre vacíos.");
                continue;
            }
            Set<CongreganteValidation> validationListCongregante = new HashSet<>();

            congreganteService.findCongreganteToValidate(
                            guardarCongreganteRequest.getApellido(),
                            guardarCongreganteRequest.getNombre())
                    .forEach(congregante -> validationListCongregante.add(new CongreganteValidation(
                            guardarCongreganteRequest.getApellido(),
                            guardarCongreganteRequest.getNombre()
                    )));

            if (!validationListCongregante.isEmpty()) {
                logger.error(VALIDACION_CAMPOS_DUPLICIDAD_CONGREGANTE,
                        guardarCongreganteRequest.getApellido(),
                        guardarCongreganteRequest.getNombre()
                );
            } else {
                logger.info(VALIDACION_CAMPOS_SIN_DUPLICIDAD_CONGREGANTE,
                        guardarCongreganteRequest.getApellido(),
                        guardarCongreganteRequest.getNombre()
                );

                logger.info("Se continúa con el proceso de registro");

                logger.info(FORMAT_REQUEST);
                String apellidoCongregante = DataConverter.formatTextTrim(guardarCongreganteRequest.getApellido());
                String nombreCongregante = DataConverter.formatTextTrim(guardarCongreganteRequest.getNombre());
                String sexoCongregante = guardarCongreganteRequest.getSexo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getSexo());
                String telefonoCongregante = guardarCongreganteRequest.getTelefono().isEmpty() ? null : DataConverter.formatTextTrim(guardarCongreganteRequest.getTelefono());
                String direccionCongregante = guardarCongreganteRequest.getDireccion().isEmpty() ? null : DataConverter.formatTextTrim(guardarCongreganteRequest.getDireccion());
                String mesCumpleaniosCongregante = guardarCongreganteRequest.getMesCumpleanios().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMesCumpleanios());
                String fechaNacimientoCongregante = guardarCongreganteRequest.getFechaNacimiento().isEmpty() ? null : DataConverter.formatTextTrim(guardarCongreganteRequest.getFechaNacimiento());
                String edadCongregante = guardarCongreganteRequest.getEdad().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEdad());
                String estadoCivilCongregante = guardarCongreganteRequest.getEstadoCivil().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEstadoCivil());
                String cantidadHijoCongregante = guardarCongreganteRequest.getCantidadHijo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCantidadHijo());
                String tiempoIBETCongregante = guardarCongreganteRequest.getTiempoIBET().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getTiempoIBET());
                String iglesiaAnteriorCongregante = guardarCongreganteRequest.getIglesiaAnterior().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getIglesiaAnterior());
                String bautizadoCongregante = guardarCongreganteRequest.getBautizado().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getBautizado());
                String iglesiaBautizoCongregante = guardarCongreganteRequest.getIglesiaBautizo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getIglesiaBautizo());
                String tiempoBautizoCongregante = guardarCongreganteRequest.getTiempoBautizo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getTiempoBautizo());
                String estudiandoCongregante = guardarCongreganteRequest.getEstudiando().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEstudiando());
                String cursoCongregante = guardarCongreganteRequest.getCurso().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCurso());
                String cursoUltimoCongregante = guardarCongreganteRequest.getCursoUltimo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCursoUltimo());
                String tiempoSinEstudioCongregante = guardarCongreganteRequest.getTiempoSinEstudio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getTiempoSinEstudio());
                String motivoSinEstudioCongregante = guardarCongreganteRequest.getMotivoSinEstudio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMotivoSinEstudio());
                String participandoGPCCongregante = guardarCongreganteRequest.getParticipandoGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getParticipandoGPC());
                String motivoNoGPCCongregante = guardarCongreganteRequest.getMotivoNoGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMotivoNoGPC());
                String numeroGPCCongregante = guardarCongreganteRequest.getNumeroGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getNumeroGPC());
                String enMinisterioCongregante = guardarCongreganteRequest.getEnMinisterio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEnMinisterio());
                String ministerioCongregante = guardarCongreganteRequest.getMinisterio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMinisterio());
                String cargoCongregante = guardarCongreganteRequest.getCargo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCargo());
                String estudiandoEscuelaCiervoCongregante = guardarCongreganteRequest.getEstudiandoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEstudiandoEscuelaCiervo());
                String cursoEscuelaCiervoCongregante = guardarCongreganteRequest.getCursoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCursoEscuelaCiervo());
                String cursoUltimoEscuelaCiervoCongregante = guardarCongreganteRequest.getCursoUltimoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCursoUltimoEscuelaCiervo());
                String tiempoSinEstudioEscuelaCiervoCongregante = guardarCongreganteRequest.getTiempoSinEstudioEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getTiempoSinEstudioEscuelaCiervo());
                String motivoSinEstudioEscuelaCiervoCongregante = guardarCongreganteRequest.getMotivoSinEstudioEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMotivoSinEstudioEscuelaCiervo());

                Congregante congregante = new Congregante(
                        apellidoCongregante,
                        nombreCongregante,
                        sexoCongregante,
                        telefonoCongregante,
                        direccionCongregante,
                        mesCumpleaniosCongregante,
                        fechaNacimientoCongregante,
                        edadCongregante,
                        estadoCivilCongregante,
                        cantidadHijoCongregante,
                        tiempoIBETCongregante,
                        iglesiaAnteriorCongregante,
                        bautizadoCongregante,
                        iglesiaBautizoCongregante,
                        tiempoBautizoCongregante,
                        estudiandoCongregante,
                        cursoCongregante,
                        cursoUltimoCongregante,
                        tiempoSinEstudioCongregante,
                        motivoSinEstudioCongregante,
                        participandoGPCCongregante,
                        motivoNoGPCCongregante,
                        numeroGPCCongregante,
                        enMinisterioCongregante,
                        ministerioCongregante,
                        cargoCongregante,
                        estudiandoEscuelaCiervoCongregante,
                        cursoEscuelaCiervoCongregante,
                        cursoUltimoEscuelaCiervoCongregante,
                        tiempoSinEstudioEscuelaCiervoCongregante,
                        motivoSinEstudioEscuelaCiervoCongregante,
                        GeneralConstant.FECHA_HORA_LOCAL,
                        true
                );

                congreganteService.saveCongregante(congregante);
                logger.info("Se guardó el Congregante");
            }
        }

        ServiceResponse serviceResponse = new ServiceResponse(
                "Se guardaron los Congregantes por Carga Masiva correctamente.",
                HttpStatus.ACCEPTED.value(),
                HttpStatus.ACCEPTED.getReasonPhrase()
        );

        logger.info(FIN_TITULO_CARGA_MASIVA_CONGREGANTES_SERVICE);

        return new ResponseEntity<>(serviceResponse,
                HttpStatus.ACCEPTED);
    }

    @PostMapping("/v1/save")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENCARGADO')")
    public ResponseEntity<ServiceResponse> guardarCongregante(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                              @RequestBody GuardarCongreganteRequest guardarCongreganteRequest) throws JsonProcessingException {

        logger.info(INICIO_TITULO_GUARDAR_CONGREGANTE_SERVICE);

        logger.info(VALIDACION_AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_GUARDAR_CONGREGANTE_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.BAD_REQUEST);
        }

        String authorizationHeader = DataConverter.formatTextTrim(authorization);
        logger.info(CON_AUTHORIZATION_HEADER, authorizationHeader);

        //Get JSON Request
        logger.info("JSON Request: ");
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonBody = objectWriter.writeValueAsString(guardarCongreganteRequest);
        logger.info(jsonBody);

        logger.info(FORMAT_REQUEST);
        String apellidoCongregante = DataConverter.formatTextTrim(guardarCongreganteRequest.getApellido());
        String nombreCongregante = DataConverter.formatTextTrim(guardarCongreganteRequest.getNombre());
        String sexoCongregante = guardarCongreganteRequest.getSexo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getSexo());
        String telefonoCongregante = guardarCongreganteRequest.getTelefono().isEmpty() ? null : DataConverter.formatTextTrim(guardarCongreganteRequest.getTelefono());
        String direccionCongregante = guardarCongreganteRequest.getDireccion().isEmpty() ? null : DataConverter.formatTextTrim(guardarCongreganteRequest.getDireccion());

        String mesCumpleaniosCongregante = null;
        String fechaNacimientoCongregante = null;
        String edadCongregante = null;

        if (guardarCongreganteRequest.getFechaNacimiento() != null && !guardarCongreganteRequest.getFechaNacimiento().isEmpty()) {
            fechaNacimientoCongregante = DataConverter.formatTextTrim(guardarCongreganteRequest.getFechaNacimiento());

            Pattern pattern = Pattern.compile(RegexConstant.REGEX_FECHA_DD_MM_YY);
            Matcher matcher = pattern.matcher(fechaNacimientoCongregante);

            if (matcher.matches()) {
                mesCumpleaniosCongregante = DataConverter.getMonthTextFromTextDate(fechaNacimientoCongregante);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GeneralConstant.PATTERN_FECHA_SLASH);
                LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoCongregante, formatter);

                edadCongregante = DataConverter.getPeriodDiffTextFromDates(fechaNacimiento, GeneralConstant.FECHA_HORA_LOCAL.toLocalDate());
            } else {
                fechaNacimientoCongregante = null;
            }
        }

        String estadoCivilCongregante = guardarCongreganteRequest.getEstadoCivil().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEstadoCivil());
        String cantidadHijoCongregante = guardarCongreganteRequest.getCantidadHijo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCantidadHijo());
        String tiempoIBETCongregante = guardarCongreganteRequest.getTiempoIBET().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getTiempoIBET());
        String iglesiaAnteriorCongregante = guardarCongreganteRequest.getIglesiaAnterior().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getIglesiaAnterior());
        String bautizadoCongregante = guardarCongreganteRequest.getBautizado().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getBautizado());
        String iglesiaBautizoCongregante = guardarCongreganteRequest.getIglesiaBautizo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getIglesiaBautizo());
        String tiempoBautizoCongregante = guardarCongreganteRequest.getTiempoBautizo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getTiempoBautizo());
        String estudiandoCongregante = guardarCongreganteRequest.getEstudiando().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEstudiando());
        String cursoCongregante = guardarCongreganteRequest.getCurso().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCurso());
        String cursoUltimoCongregante = guardarCongreganteRequest.getCursoUltimo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCursoUltimo());
        String tiempoSinEstudioCongregante = guardarCongreganteRequest.getTiempoSinEstudio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getTiempoSinEstudio());
        String motivoSinEstudioCongregante = guardarCongreganteRequest.getMotivoSinEstudio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMotivoSinEstudio());
        String participandoGPCCongregante = guardarCongreganteRequest.getParticipandoGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getParticipandoGPC());
        String motivoNoGPCCongregante = guardarCongreganteRequest.getMotivoNoGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMotivoNoGPC());
        String numeroGPCCongregante = guardarCongreganteRequest.getNumeroGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getNumeroGPC());
        String enMinisterioCongregante = guardarCongreganteRequest.getEnMinisterio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEnMinisterio());
        String ministerioCongregante = guardarCongreganteRequest.getMinisterio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMinisterio());
        String cargoCongregante = guardarCongreganteRequest.getCargo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCargo());
        String estudiandoEscuelaCiervoCongregante = guardarCongreganteRequest.getEstudiandoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getEstudiandoEscuelaCiervo());
        String cursoEscuelaCiervoCongregante = guardarCongreganteRequest.getCursoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCursoEscuelaCiervo());
        String cursoUltimoEscuelaCiervoCongregante = guardarCongreganteRequest.getCursoUltimoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getCursoUltimoEscuelaCiervo());
        String tiempoSinEstudioEscuelaCiervoCongregante = guardarCongreganteRequest.getTiempoSinEstudioEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getTiempoSinEstudioEscuelaCiervo());
        String motivoSinEstudioEscuelaCiervoCongregante = guardarCongreganteRequest.getMotivoSinEstudioEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(guardarCongreganteRequest.getMotivoSinEstudioEscuelaCiervo());

        logger.info(INICIO_VALIDACION);
        Set<CongreganteValidation> validationListCongregante = new HashSet<>();

        congreganteService.findCongreganteToValidate(
                        apellidoCongregante,
                        nombreCongregante)
                .forEach(congregante -> validationListCongregante.add(new CongreganteValidation(
                        apellidoCongregante,
                        nombreCongregante
                )));

        if (!validationListCongregante.isEmpty()) {
            logger.error(VALIDACION_CAMPOS_DUPLICIDAD_CONGREGANTE,
                    apellidoCongregante,
                    nombreCongregante
            );

            ServiceResponse serviceResponse = new ServiceResponse(
                    "El Congregante ya fue registrado previamente.",
                    HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT.getReasonPhrase()
            );

            logger.info(FIN_TITULO_GUARDAR_CONGREGANTE_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.CONFLICT);
        }

        logger.info(VALIDACION_CAMPOS_SIN_DUPLICIDAD_CONGREGANTE,
                apellidoCongregante,
                nombreCongregante
        );
        logger.info("Se continúa con el proceso de registro");

        Congregante congregante = new Congregante(
                apellidoCongregante,
                nombreCongregante,
                sexoCongregante,
                telefonoCongregante,
                direccionCongregante,
                mesCumpleaniosCongregante,
                fechaNacimientoCongregante,
                edadCongregante,
                estadoCivilCongregante,
                cantidadHijoCongregante,
                tiempoIBETCongregante,
                iglesiaAnteriorCongregante,
                bautizadoCongregante,
                iglesiaBautizoCongregante,
                tiempoBautizoCongregante,
                estudiandoCongregante,
                cursoCongregante,
                cursoUltimoCongregante,
                tiempoSinEstudioCongregante,
                motivoSinEstudioCongregante,
                participandoGPCCongregante,
                motivoNoGPCCongregante,
                numeroGPCCongregante,
                enMinisterioCongregante,
                ministerioCongregante,
                cargoCongregante,
                estudiandoEscuelaCiervoCongregante,
                cursoEscuelaCiervoCongregante,
                cursoUltimoEscuelaCiervoCongregante,
                tiempoSinEstudioEscuelaCiervoCongregante,
                motivoSinEstudioEscuelaCiervoCongregante,
                GeneralConstant.FECHA_HORA_LOCAL,
                true
        );

        congreganteService.saveCongregante(congregante);
        logger.info("Se guardó el Congregante");

        ServiceResponse serviceResponse = new ServiceResponse(
                "Se guardó el Congregante correctamente.",
                HttpStatus.ACCEPTED.value(),
                HttpStatus.ACCEPTED.getReasonPhrase()
        );

        logger.info(FIN_TITULO_GUARDAR_CONGREGANTE_SERVICE);

        return new ResponseEntity<>(serviceResponse,
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/v1/congregantes")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENCARGADO')")
    public ResponseEntity<ServiceResponse> mostrarCongregantes(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                               @RequestParam(value = "page", required = false) String page,
                                                               @RequestParam(value = "size", required = false) String size) {

        logger.info(INICIO_TITULO_MOSTRAR_CONGREGANTES_SERVICE);

        logger.info(VALIDACION_AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_MOSTRAR_CONGREGANTES_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.BAD_REQUEST);
        }

        String authorizationHeader = DataConverter.formatTextTrim(authorization);
        logger.info(CON_AUTHORIZATION_HEADER, authorizationHeader);

        //Variables para Paginacion
        String pagePaginacionCongregantes = null;
        String sizePaginacionCongregantes = null;

        logger.info("Validando Param: page");
        if (page == null || page.isEmpty()) {
            logger.info("Param page no ingresado");
        } else {
            logger.info("Validando Expresión del Param: page");

            if (!page.matches(RegexConstant.REGEX_ENTEROS_POSITIVOS)) {
                logger.error("Param page no válido");

                ServiceResponse serviceResponse = new ServiceResponse(
                        "Se requiere ingresar el Param page con un expresión válida.",
                        HttpStatus.EXPECTATION_FAILED.value(),
                        HttpStatus.EXPECTATION_FAILED.getReasonPhrase()
                );

                logger.info(FIN_TITULO_MOSTRAR_CONGREGANTES_SERVICE);

                return new ResponseEntity<>(serviceResponse,
                        HttpStatus.EXPECTATION_FAILED);
            }

            pagePaginacionCongregantes = page;
            logger.info("Param page ingresado: {}", pagePaginacionCongregantes);
        }

        logger.info("Validando Param: size");
        if (size == null || size.isEmpty()) {
            logger.info("Param size no ingresado");
        } else {
            logger.info("Validando Expresión del Param: size");

            if (!size.matches(RegexConstant.REGEX_ENTEROS_POSITIVOS)) {
                logger.error("Param size no válido");

                ServiceResponse serviceResponse = new ServiceResponse(
                        "Se requiere ingresar el Param size con un expresión válida.",
                        HttpStatus.EXPECTATION_FAILED.value(),
                        HttpStatus.EXPECTATION_FAILED.getReasonPhrase()
                );

                logger.info(FIN_TITULO_MOSTRAR_CONGREGANTES_SERVICE);

                return new ResponseEntity<>(serviceResponse,
                        HttpStatus.EXPECTATION_FAILED);
            }

            sizePaginacionCongregantes = size;
            logger.info("Param size ingresado: {}", sizePaginacionCongregantes);
        }

        logger.info(INICIO_BUSQUEDA_CONGREGANTES);

        Set<Congregante> listCongregantes;
        Set<CongreganteResponse> listCongregantesResponse = new LinkedHashSet<>();
        int totalRegistros;

        Sort sort = Sort.by(PARAM_SORT_APELLIDO, PARAM_SORT_NOMBRE).ascending();

        if (pagePaginacionCongregantes != null && sizePaginacionCongregantes != null) {
            int pageCongregantes = Integer.parseInt(pagePaginacionCongregantes);
            int sizeCongregantes = Integer.parseInt(sizePaginacionCongregantes);

            Pageable pageable = PageRequest.of(pageCongregantes, sizeCongregantes, sort);

            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantes(pageable).getContent());
            totalRegistros = (int) congreganteService.findCongregantes(pageable).getTotalElements();
            logger.info(TOTAL_REGISTROS, totalRegistros);

            AtomicLong indiceCongregante = new AtomicLong(1);

            for (Congregante congregante : listCongregantes) {
                listCongregantesResponse.add(new CongreganteResponse(
                        congregante.getId(),
                        String.valueOf(indiceCongregante.getAndIncrement()),
                        DataConverter.getFirstLetterAndUpperFromText(congregante.getApellido()),
                        congregante.getApellido(),
                        congregante.getNombre(),
                        congregante.getSexo(),
                        congregante.getTelefono(),
                        congregante.getDireccion(),
                        congregante.getMesCumpleanios(),
                        congregante.getFechaNacimiento(),
                        congregante.getEdad(),
                        congregante.getEstadoCivil(),
                        congregante.getCantidadHijo(),
                        congregante.getTiempoIBET(),
                        congregante.getIglesiaAnterior(),
                        congregante.getBautizado(),
                        congregante.getIglesiaBautizo(),
                        congregante.getTiempoBautizo(),
                        congregante.getEstudiando(),
                        congregante.getCurso(),
                        congregante.getCursoUltimo(),
                        congregante.getTiempoSinEstudio(),
                        congregante.getMotivoSinEstudio(),
                        congregante.getParticipandoGPC(),
                        congregante.getMotivoNoGPC(),
                        congregante.getNumeroGPC(),
                        congregante.getEnMinisterio(),
                        congregante.getMinisterio(),
                        congregante.getCargo(),
                        congregante.getEstudiandoEscuelaCiervo(),
                        congregante.getCursoEscuelaCiervo(),
                        congregante.getCursoUltimoEscuelaCiervo(),
                        congregante.getTiempoSinEstudioEscuelaCiervo(),
                        congregante.getMotivoSinEstudioEscuelaCiervo(),
                        congregante.getHabilitado()
                ));
            }

            ServiceResponse serviceResponse = mostrarListaCongregantesResponse(
                    sizeCongregantes,
                    listCongregantesResponse,
                    totalRegistros,
                    pageable.getPageNumber()
            );

            logger.info(FIN_TITULO_MOSTRAR_CONGREGANTES_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.OK);
        } else {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantes(sort));
            totalRegistros = congreganteService.findCongregantes(sort).size();
            logger.info(TOTAL_REGISTROS, totalRegistros);

            AtomicLong indiceCongregante = new AtomicLong(1);

            for (Congregante congregante : listCongregantes) {
                listCongregantesResponse.add(new CongreganteResponse(
                        congregante.getId(),
                        String.valueOf(indiceCongregante.getAndIncrement()),
                        DataConverter.getFirstLetterAndUpperFromText(congregante.getApellido()),
                        congregante.getApellido(),
                        congregante.getNombre(),
                        congregante.getSexo(),
                        congregante.getTelefono(),
                        congregante.getDireccion(),
                        congregante.getMesCumpleanios(),
                        congregante.getFechaNacimiento(),
                        congregante.getEdad(),
                        congregante.getEstadoCivil(),
                        congregante.getCantidadHijo(),
                        congregante.getTiempoIBET(),
                        congregante.getIglesiaAnterior(),
                        congregante.getBautizado(),
                        congregante.getIglesiaBautizo(),
                        congregante.getTiempoBautizo(),
                        congregante.getEstudiando(),
                        congregante.getCurso(),
                        congregante.getCursoUltimo(),
                        congregante.getTiempoSinEstudio(),
                        congregante.getMotivoSinEstudio(),
                        congregante.getParticipandoGPC(),
                        congregante.getMotivoNoGPC(),
                        congregante.getNumeroGPC(),
                        congregante.getEnMinisterio(),
                        congregante.getMinisterio(),
                        congregante.getCargo(),
                        congregante.getEstudiandoEscuelaCiervo(),
                        congregante.getCursoEscuelaCiervo(),
                        congregante.getCursoUltimoEscuelaCiervo(),
                        congregante.getTiempoSinEstudioEscuelaCiervo(),
                        congregante.getMotivoSinEstudioEscuelaCiervo(),
                        congregante.getHabilitado()
                ));
            }

            BodyResponse bodyResponse = new BodyResponse(listCongregantesResponse);

            ServiceResponse serviceResponse = new ServiceResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    bodyResponse
            );

            logger.info(FIN_TITULO_MOSTRAR_CONGREGANTES_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.OK);
        }
    }

    @GetMapping("/v1/congregantes/find-by")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENCARGADO')")
    public ResponseEntity<ServiceResponse> buscarCongregantes(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                              @RequestParam(value = "apellido", required = false) String apellido,
                                                              @RequestParam(value = "nombre", required = false) String nombre,
                                                              @RequestParam(value = "fechaNacimiento", required = false) String fechaNacimiento,
                                                              @RequestParam(value = "bautizado", required = false) String bautizado,
                                                              @RequestParam(value = "numeroGPC", required = false) String numeroGPC,
                                                              @RequestParam(value = "ministerio", required = false) String ministerio,
                                                              @RequestParam(value = "curso", required = false) String curso,
                                                              @RequestParam(value = "page", required = false) String page,
                                                              @RequestParam(value = "size", required = false) String size) {

        logger.info(INICIO_TITULO_BUSCAR_CONGREGANTES_SERVICE);

        logger.info(VALIDACION_AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_BUSCAR_CONGREGANTES_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.BAD_REQUEST);
        }

        String authorizationHeader = DataConverter.formatTextTrim(authorization);
        logger.info(CON_AUTHORIZATION_HEADER, authorizationHeader);

        //Parámetros Iniciales de Búsqueda
        String apellidoCongregante = null;
        String nombreCongregante = null;
        String fechaNacimientoCongregante = null;
        String bautizadoCongregante = null;
        String numeroGPCCongregante = null;
        String ministerioCongregante = null;
        String cursoCongregante = null;

        logger.info("Validando Param: apellido");

        if (apellido == null || apellido.isEmpty()) {
            logger.info("Param apellido no ingresado");
        } else {
            apellidoCongregante = DataConverter.formatTextTrimAndLower(apellido);

            logger.info("Param apellido ingresado: {}", apellido);
        }

        logger.info("Validando Param: nombre");

        if (nombre == null || nombre.isEmpty()) {
            logger.info("Param nombre no ingresado");
        } else {
            nombreCongregante = DataConverter.formatTextTrimAndLower(nombre);

            logger.info("Param nombre ingresado: {}", nombre);
        }

        logger.info("Validando Param: fechaNacimiento");

        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) {
            logger.info("Param fechaNacimiento no ingresado");
        } else {
            fechaNacimientoCongregante = DataConverter.formatTextTrim(fechaNacimiento);

            logger.info("Param fechaNacimiento ingresado: {}", fechaNacimiento);
        }

        logger.info("Validando Param: bautizado");

        if (bautizado == null || bautizado.isEmpty()) {
            logger.info("Param bautizado no ingresado");
        } else {
            bautizadoCongregante = DataConverter.formatTextTrimAndLower(bautizado);

            logger.info("Param bautizado ingresado: {}", bautizado);
        }

        logger.info("Validando Param: numeroGPC");

        if (numeroGPC == null || numeroGPC.isEmpty()) {
            logger.info("Param numeroGPC no ingresado");
        } else {
            numeroGPCCongregante = DataConverter.formatTextTrimAndLower(numeroGPC);

            logger.info("Param numeroGPC ingresado: {}", numeroGPC);
        }

        logger.info("Validando Param: ministerio");

        if (ministerio == null || ministerio.isEmpty()) {
            logger.info("Param ministerio no ingresado");
        } else {
            ministerioCongregante = DataConverter.formatTextTrimAndLower(ministerio);

            logger.info("Param ministerio ingresado: {}", ministerio);
        }

        logger.info("Validando Param: curso");

        if (curso == null || curso.isEmpty()) {
            logger.info("Param curso no ingresado");
        } else {
            cursoCongregante = DataConverter.formatTextTrimAndLower(curso);

            logger.info("Param curso ingresado: {}", curso);
        }

        //Variables para Paginacion
        String pagePaginacionCongregantes = null;
        String sizePaginacionCongregantes = null;

        logger.info("Validando Param: page");
        if (page == null || page.isEmpty()) {
            logger.info("Param page no ingresado");
        } else {
            logger.info("Validando Expresión del Param: page");

            if (!page.matches(RegexConstant.REGEX_ENTEROS_POSITIVOS)) {
                logger.error("Param page no válido");

                ServiceResponse serviceResponse = new ServiceResponse(
                        "Se requiere ingresar el Param page con un expresión válida.",
                        HttpStatus.EXPECTATION_FAILED.value(),
                        HttpStatus.EXPECTATION_FAILED.getReasonPhrase()
                );

                logger.info(FIN_TITULO_BUSCAR_CONGREGANTES_SERVICE);

                return new ResponseEntity<>(serviceResponse,
                        HttpStatus.EXPECTATION_FAILED);
            }

            pagePaginacionCongregantes = page;
            logger.info("Param page ingresado: {}", pagePaginacionCongregantes);
        }

        logger.info("Validando Param: size");
        if (size == null || size.isEmpty()) {
            logger.info("Param size no ingresado");
        } else {
            logger.info("Validando Expresión del Param: size");

            if (!size.matches(RegexConstant.REGEX_ENTEROS_POSITIVOS)) {
                logger.error("Param size no válido");

                ServiceResponse serviceResponse = new ServiceResponse(
                        "Se requiere ingresar el Param size con un expresión válida.",
                        HttpStatus.EXPECTATION_FAILED.value(),
                        HttpStatus.EXPECTATION_FAILED.getReasonPhrase()
                );

                logger.info(FIN_TITULO_BUSCAR_CONGREGANTES_SERVICE);

                return new ResponseEntity<>(serviceResponse,
                        HttpStatus.EXPECTATION_FAILED);
            }

            sizePaginacionCongregantes = size;
            logger.info("Param size ingresado: {}", sizePaginacionCongregantes);
        }

        logger.info(INICIO_BUSQUEDA_CONGREGANTES);

        Set<Congregante> listCongregantes = Collections.emptySet();
        Set<CongreganteResponse> listCongregantesResponse = new LinkedHashSet<>();
        int totalRegistros = 0;

        Sort sort = Sort.by(PARAM_SORT_APELLIDO, PARAM_SORT_NOMBRE).ascending();

        if (pagePaginacionCongregantes != null && sizePaginacionCongregantes != null) {
            int pageCongregantes = Integer.parseInt(pagePaginacionCongregantes);
            int sizeCongregantes = Integer.parseInt(sizePaginacionCongregantes);

            Pageable pageable = PageRequest.of(pageCongregantes, sizeCongregantes, sort);

            if (apellidoCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByApellido(apellidoCongregante, pageable).getContent());
                totalRegistros = (int) congreganteService.findCongregantesByApellido(apellidoCongregante, pageable).getTotalElements();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (nombreCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByNombre(nombreCongregante, pageable).getContent());
                totalRegistros = (int) congreganteService.findCongregantesByNombre(nombreCongregante, pageable).getTotalElements();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (fechaNacimientoCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByFechaNacimiento(fechaNacimientoCongregante, pageable).getContent());
                totalRegistros = (int) congreganteService.findCongregantesByFechaNacimiento(fechaNacimientoCongregante, pageable).getTotalElements();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (bautizadoCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByBautizado(bautizadoCongregante, pageable).getContent());
                totalRegistros = (int) congreganteService.findCongregantesByBautizado(bautizadoCongregante, pageable).getTotalElements();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (numeroGPCCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByNumeroGPC(numeroGPCCongregante, pageable).getContent());
                totalRegistros = (int) congreganteService.findCongregantesByNumeroGPC(numeroGPCCongregante, pageable).getTotalElements();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (ministerioCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByMinisterio(ministerioCongregante, pageable).getContent());
                totalRegistros = (int) congreganteService.findCongregantesByMinisterio(ministerioCongregante, pageable).getTotalElements();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (cursoCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByCurso(cursoCongregante, pageable).getContent());
                totalRegistros = (int) congreganteService.findCongregantesByCurso(cursoCongregante, pageable).getTotalElements();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            AtomicLong indiceCongregante = new AtomicLong(1);

            for (Congregante congregante : listCongregantes) {
                listCongregantesResponse.add(new CongreganteResponse(
                        congregante.getId(),
                        String.valueOf(indiceCongregante.getAndIncrement()),
                        DataConverter.getFirstLetterAndUpperFromText(congregante.getApellido()),
                        congregante.getApellido(),
                        congregante.getNombre(),
                        congregante.getSexo(),
                        congregante.getTelefono(),
                        congregante.getDireccion(),
                        congregante.getMesCumpleanios(),
                        congregante.getFechaNacimiento(),
                        congregante.getEdad(),
                        congregante.getEstadoCivil(),
                        congregante.getCantidadHijo(),
                        congregante.getTiempoIBET(),
                        congregante.getIglesiaAnterior(),
                        congregante.getBautizado(),
                        congregante.getIglesiaBautizo(),
                        congregante.getTiempoBautizo(),
                        congregante.getEstudiando(),
                        congregante.getCurso(),
                        congregante.getCursoUltimo(),
                        congregante.getTiempoSinEstudio(),
                        congregante.getMotivoSinEstudio(),
                        congregante.getParticipandoGPC(),
                        congregante.getMotivoNoGPC(),
                        congregante.getNumeroGPC(),
                        congregante.getEnMinisterio(),
                        congregante.getMinisterio(),
                        congregante.getCargo(),
                        congregante.getEstudiandoEscuelaCiervo(),
                        congregante.getCursoEscuelaCiervo(),
                        congregante.getCursoUltimoEscuelaCiervo(),
                        congregante.getTiempoSinEstudioEscuelaCiervo(),
                        congregante.getMotivoSinEstudioEscuelaCiervo(),
                        congregante.getHabilitado()
                ));
            }

            ServiceResponse serviceResponse = mostrarListaCongregantesResponse(
                    sizeCongregantes,
                    listCongregantesResponse,
                    totalRegistros,
                    pageable.getPageNumber()
            );

            logger.info(FIN_TITULO_BUSCAR_CONGREGANTES_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.OK);
        } else {
            if (apellidoCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByApellido(apellidoCongregante, sort));
                totalRegistros = congreganteService.findCongregantesByApellido(apellidoCongregante, sort).size();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (nombreCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByNombre(nombreCongregante, sort));
                totalRegistros = congreganteService.findCongregantesByNombre(nombreCongregante, sort).size();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (fechaNacimientoCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByFechaNacimiento(fechaNacimientoCongregante, sort));
                totalRegistros = congreganteService.findCongregantesByFechaNacimiento(fechaNacimientoCongregante, sort).size();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (bautizadoCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByBautizado(bautizadoCongregante, sort));
                totalRegistros = congreganteService.findCongregantesByBautizado(bautizadoCongregante, sort).size();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (numeroGPCCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByNumeroGPC(numeroGPCCongregante, sort));
                totalRegistros = congreganteService.findCongregantesByNumeroGPC(numeroGPCCongregante, sort).size();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (ministerioCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByMinisterio(ministerioCongregante, sort));
                totalRegistros = congreganteService.findCongregantesByMinisterio(ministerioCongregante, sort).size();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            if (cursoCongregante != null) {
                listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByCurso(cursoCongregante, sort));
                totalRegistros = congreganteService.findCongregantesByCurso(cursoCongregante, sort).size();
                logger.info(TOTAL_REGISTROS, totalRegistros);
            }

            AtomicLong indiceCongregante = new AtomicLong(1);

            for (Congregante congregante : listCongregantes) {
                listCongregantesResponse.add(new CongreganteResponse(
                        congregante.getId(),
                        String.valueOf(indiceCongregante.getAndIncrement()),
                        DataConverter.getFirstLetterAndUpperFromText(congregante.getApellido()),
                        congregante.getApellido(),
                        congregante.getNombre(),
                        congregante.getSexo(),
                        congregante.getTelefono(),
                        congregante.getDireccion(),
                        congregante.getMesCumpleanios(),
                        congregante.getFechaNacimiento(),
                        congregante.getEdad(),
                        congregante.getEstadoCivil(),
                        congregante.getCantidadHijo(),
                        congregante.getTiempoIBET(),
                        congregante.getIglesiaAnterior(),
                        congregante.getBautizado(),
                        congregante.getIglesiaBautizo(),
                        congregante.getTiempoBautizo(),
                        congregante.getEstudiando(),
                        congregante.getCurso(),
                        congregante.getCursoUltimo(),
                        congregante.getTiempoSinEstudio(),
                        congregante.getMotivoSinEstudio(),
                        congregante.getParticipandoGPC(),
                        congregante.getMotivoNoGPC(),
                        congregante.getNumeroGPC(),
                        congregante.getEnMinisterio(),
                        congregante.getMinisterio(),
                        congregante.getCargo(),
                        congregante.getEstudiandoEscuelaCiervo(),
                        congregante.getCursoEscuelaCiervo(),
                        congregante.getCursoUltimoEscuelaCiervo(),
                        congregante.getTiempoSinEstudioEscuelaCiervo(),
                        congregante.getMotivoSinEstudioEscuelaCiervo(),
                        congregante.getHabilitado()
                ));
            }

            BodyResponse bodyResponse = new BodyResponse(listCongregantesResponse);

            ServiceResponse serviceResponse = new ServiceResponse(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    bodyResponse
            );

            logger.info(FIN_TITULO_BUSCAR_CONGREGANTES_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.OK);
        }
    }

    @GetMapping("/v1/{id}/detail")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENCARGADO')")
    public ResponseEntity<ServiceResponse> mostrarDetalleCongregante(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                                     @PathVariable("id") UUID id) {

        logger.info(INICIO_TITULO_BUSCAR_CONGREGANTE_POR_ID_SERVICE);

        logger.info(VALIDACION_AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_BUSCAR_CONGREGANTE_POR_ID_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.BAD_REQUEST);
        }

        String authorizationHeader = DataConverter.formatTextTrim(authorization);
        logger.info(CON_AUTHORIZATION_HEADER, authorizationHeader);

        logger.info("ID Congregante ingresado: {}", id);

        logger.info("Iniciando Búsqueda de Información del Congregante");
        Optional<Congregante> dataCongregante = congreganteService.findCongreganteById(id);

        if (dataCongregante.isEmpty()) {
            logger.error("No se encontró el Congregante con ID: {}", id);

            ServiceResponse serviceResponse = new ServiceResponse(
                    "Ocurrió un error al encontrar el Congregante ingresado.",
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase()
            );

            logger.info(FIN_TITULO_BUSCAR_CONGREGANTE_POR_ID_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.NOT_FOUND);
        }

        logger.info("Se encontró el Congregante con ID: {}", id);
        Congregante congregante = dataCongregante.get();

        if (!Boolean.TRUE.equals(congregante.getHabilitado())) {
            logger.error("El Congregante no se encuentra habilitado");

            ServiceResponse serviceResponse = new ServiceResponse(
                    "El Congregante no se encuentra habilitado para su uso.",
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase()
            );

            logger.info(FIN_TITULO_BUSCAR_CONGREGANTE_POR_ID_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.UNAUTHORIZED);
        }

        logger.info("El Congregante se encuentra habilitado");

        ServiceResponse serviceResponse = mostrarDetalleCongreganteServiceResponse(congregante);

        logger.info(FIN_TITULO_BUSCAR_CONGREGANTE_POR_ID_SERVICE);

        return new ResponseEntity<>(serviceResponse,
                HttpStatus.OK);
    }

    @PutMapping("/v1/{id}/update")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENCARGADO')")
    public ResponseEntity<ServiceResponse> actualizarCongregante(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                                 @PathVariable("id") UUID id,
                                                                 @RequestBody ActualizarCongreganteRequest actualizarCongreganteRequest) throws JsonProcessingException {

        logger.info(INICIO_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE);

        logger.info(VALIDACION_AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.BAD_REQUEST);
        }

        String authorizationHeader = DataConverter.formatTextTrim(authorization);
        logger.info(CON_AUTHORIZATION_HEADER, authorizationHeader);

        logger.info("ID Congregante ingresado: {}", id);

        logger.info("Iniciando Búsqueda de Información del Congregante");
        Optional<Congregante> dataCongregante = congreganteService.findCongreganteById(id);

        if (dataCongregante.isEmpty()) {
            logger.error("No se encontró el Congregante con ID: {}", id);

            ServiceResponse serviceResponse = new ServiceResponse(
                    "Ocurrió un error al encontrar el Congregante ingresado.",
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase()
            );

            logger.info(FIN_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.NOT_FOUND);
        }

        logger.info("Se encontró el Congregante con ID: {}", id);
        Congregante currentCongregante = dataCongregante.get();

        if (!Boolean.TRUE.equals(currentCongregante.getHabilitado())) {
            logger.error("El Congregante no se encuentra habilitado");

            ServiceResponse serviceResponse = new ServiceResponse(
                    "El Congregante no se encuentra habilitado para su uso.",
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase()
            );

            logger.info(FIN_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.UNAUTHORIZED);
        }

        logger.info("El Congregante se encuentra habilitado");

        //Get JSON Request
        logger.info("JSON Request: ");
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonBody = objectWriter.writeValueAsString(actualizarCongreganteRequest);
        logger.info(jsonBody);

        logger.info(FORMAT_REQUEST);
        String apellidoCongregante = DataConverter.formatTextTrim(actualizarCongreganteRequest.getApellido());
        String nombreCongregante = DataConverter.formatTextTrim(actualizarCongreganteRequest.getNombre());
        String sexoCongregante = actualizarCongreganteRequest.getSexo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getSexo());
        String telefonoCongregante = actualizarCongreganteRequest.getTelefono().isEmpty() ? null : DataConverter.formatTextTrim(actualizarCongreganteRequest.getTelefono());
        String direccionCongregante = actualizarCongreganteRequest.getDireccion().isEmpty() ? null : DataConverter.formatTextTrim(actualizarCongreganteRequest.getDireccion());
        String mesCumpleaniosCongregante = actualizarCongreganteRequest.getMesCumpleanios().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getMesCumpleanios());
        String fechaNacimientoCongregante = actualizarCongreganteRequest.getFechaNacimiento().isEmpty() ? null : DataConverter.formatTextTrim(actualizarCongreganteRequest.getFechaNacimiento());
        String edadCongregante = actualizarCongreganteRequest.getFechaNacimiento().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getFechaNacimiento());
        String estadoCivilCongregante = actualizarCongreganteRequest.getEstadoCivil().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getEstadoCivil());
        String cantidadHijoCongregante = actualizarCongreganteRequest.getCantidadHijo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getCantidadHijo());
        String tiempoIBETCongregante = actualizarCongreganteRequest.getTiempoIBET().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getTiempoIBET());
        String iglesiaAnteriorCongregante = actualizarCongreganteRequest.getIglesiaAnterior().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getIglesiaAnterior());
        String bautizadoCongregante = actualizarCongreganteRequest.getBautizado().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getBautizado());
        String iglesiaBautizoCongregante = actualizarCongreganteRequest.getIglesiaBautizo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getIglesiaBautizo());
        String tiempoBautizoCongregante = actualizarCongreganteRequest.getTiempoBautizo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getTiempoBautizo());
        String estudiandoCongregante = actualizarCongreganteRequest.getEstudiando().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getEstudiando());
        String cursoCongregante = actualizarCongreganteRequest.getCurso().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getCurso());
        String cursoUltimoCongregante = actualizarCongreganteRequest.getCursoUltimo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getCursoUltimo());
        String tiempoSinEstudioCongregante = actualizarCongreganteRequest.getTiempoSinEstudio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getTiempoSinEstudio());
        String motivoSinEstudioCongregante = actualizarCongreganteRequest.getMotivoSinEstudio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getMotivoSinEstudio());
        String participandoGPCCongregante = actualizarCongreganteRequest.getParticipandoGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getParticipandoGPC());
        String motivoNoGPCCongregante = actualizarCongreganteRequest.getMotivoNoGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getMotivoNoGPC());
        String numeroGPCCongregante = actualizarCongreganteRequest.getNumeroGPC().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getNumeroGPC());
        String enMinisterioCongregante = actualizarCongreganteRequest.getEnMinisterio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getEnMinisterio());
        String ministerioCongregante = actualizarCongreganteRequest.getMinisterio().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getMinisterio());
        String cargoCongregante = actualizarCongreganteRequest.getCargo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getCargo());
        String estudiandoEscuelaCiervoCongregante = actualizarCongreganteRequest.getEstudiandoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getEstudiandoEscuelaCiervo());
        String cursoEscuelaCiervoCongregante = actualizarCongreganteRequest.getCursoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getCursoEscuelaCiervo());
        String cursoUltimoEscuelaCiervoCongregante = actualizarCongreganteRequest.getCursoUltimoEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getCursoUltimoEscuelaCiervo());
        String tiempoSinEstudioEscuelaCiervoCongregante = actualizarCongreganteRequest.getTiempoSinEstudioEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getTiempoSinEstudioEscuelaCiervo());
        String motivoSinEstudioEscuelaCiervoCongregante = actualizarCongreganteRequest.getMotivoSinEstudioEscuelaCiervo().isEmpty() ? null : DataConverter.formatTextTrimAndUpper(actualizarCongreganteRequest.getMotivoSinEstudioEscuelaCiervo());

        logger.info("Iniciando Validación de Datos Clave para No Duplicidad");
        if (Objects.equals(currentCongregante.getApellido(), apellidoCongregante) && Objects.equals(currentCongregante.getNombre(), nombreCongregante)) {
            logger.info("Los Datos Clave ingresados son idénticos a los registrados");

            congreganteService.updateCongregante(
                    currentCongregante.getId(),
                    currentCongregante.getApellido(),
                    currentCongregante.getNombre(),
                    sexoCongregante,
                    telefonoCongregante,
                    direccionCongregante,
                    mesCumpleaniosCongregante,
                    fechaNacimientoCongregante,
                    edadCongregante,
                    estadoCivilCongregante,
                    cantidadHijoCongregante,
                    tiempoIBETCongregante,
                    iglesiaAnteriorCongregante,
                    bautizadoCongregante,
                    iglesiaBautizoCongregante,
                    tiempoBautizoCongregante,
                    estudiandoCongregante,
                    cursoCongregante,
                    cursoUltimoCongregante,
                    tiempoSinEstudioCongregante,
                    motivoSinEstudioCongregante,
                    participandoGPCCongregante,
                    motivoNoGPCCongregante,
                    numeroGPCCongregante,
                    enMinisterioCongregante,
                    ministerioCongregante,
                    cargoCongregante,
                    estudiandoEscuelaCiervoCongregante,
                    cursoEscuelaCiervoCongregante,
                    cursoUltimoEscuelaCiervoCongregante,
                    tiempoSinEstudioEscuelaCiervoCongregante,
                    motivoSinEstudioEscuelaCiervoCongregante,
                    GeneralConstant.FECHA_HORA_LOCAL,
                    actualizarCongreganteRequest.getHabilitado()
            );
            logger.info("Se actualizó el Congregante");

            ServiceResponse serviceResponse = new ServiceResponse(
                    "Se actualizó el Congregante correctamente.",
                    HttpStatus.ACCEPTED.value(),
                    HttpStatus.ACCEPTED.getReasonPhrase()
            );

            logger.info(FIN_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.ACCEPTED);
        } else {
            logger.info("Los Datos Clave ingresados no son idénticos a los registrados. Se inicia el proceso de Validación de Duplicados");

            logger.info(INICIO_VALIDACION);
            Set<CongreganteValidation> validationListCongregante = new HashSet<>();

            congreganteService.findCongreganteToValidate(
                            apellidoCongregante,
                            nombreCongregante)
                    .forEach(congregante -> validationListCongregante.add(new CongreganteValidation(
                            apellidoCongregante,
                            nombreCongregante
                    )));

            if (!validationListCongregante.isEmpty()) {
                logger.error(VALIDACION_CAMPOS_DUPLICIDAD_CONGREGANTE,
                        apellidoCongregante,
                        nombreCongregante
                );

                ServiceResponse serviceResponse = new ServiceResponse(
                        "El Congregante ya fue registrado previamente.",
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase()
                );

                logger.info(FIN_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE);

                return new ResponseEntity<>(serviceResponse,
                        HttpStatus.CONFLICT);
            }

            logger.info(VALIDACION_CAMPOS_SIN_DUPLICIDAD_CONGREGANTE,
                    apellidoCongregante,
                    nombreCongregante
            );
            logger.info("Se continúa con el proceso de actualización");

            congreganteService.updateCongregante(
                    currentCongregante.getId(),
                    apellidoCongregante,
                    nombreCongregante,
                    sexoCongregante,
                    telefonoCongregante,
                    direccionCongregante,
                    mesCumpleaniosCongregante,
                    fechaNacimientoCongregante,
                    edadCongregante,
                    estadoCivilCongregante,
                    cantidadHijoCongregante,
                    tiempoIBETCongregante,
                    iglesiaAnteriorCongregante,
                    bautizadoCongregante,
                    iglesiaBautizoCongregante,
                    tiempoBautizoCongregante,
                    estudiandoCongregante,
                    cursoCongregante,
                    cursoUltimoCongregante,
                    tiempoSinEstudioCongregante,
                    motivoSinEstudioCongregante,
                    participandoGPCCongregante,
                    motivoNoGPCCongregante,
                    numeroGPCCongregante,
                    enMinisterioCongregante,
                    ministerioCongregante,
                    cargoCongregante,
                    estudiandoEscuelaCiervoCongregante,
                    cursoEscuelaCiervoCongregante,
                    cursoUltimoEscuelaCiervoCongregante,
                    tiempoSinEstudioEscuelaCiervoCongregante,
                    motivoSinEstudioEscuelaCiervoCongregante,
                    GeneralConstant.FECHA_HORA_LOCAL,
                    actualizarCongreganteRequest.getHabilitado()
            );
            logger.info("Se actualizó el Congregante");

            ServiceResponse serviceResponse = new ServiceResponse(
                    "Se actualizó el Congregante correctamente.",
                    HttpStatus.ACCEPTED.value(),
                    HttpStatus.ACCEPTED.getReasonPhrase()
            );

            logger.info(FIN_TITULO_ACTUALIZAR_CONGREGANTE_SERVICE);

            return new ResponseEntity<>(serviceResponse,
                    HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/v1/congregantes/vacios")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> eliminarCongregantesVacios(
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        logger.info("Iniciando eliminación de congregantes con nombre o apellido vacío");

        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            return new ResponseEntity<>(serviceResponse, HttpStatus.BAD_REQUEST);
        }

        int eliminados = congreganteService.eliminarCongregantesNombreApellidoVacios();

        ServiceResponse serviceResponse = new ServiceResponse(
                "Se eliminaron " + eliminados + " registros con nombre o apellido vacío.",
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase()
        );

        logger.info("Eliminación finalizada. Total eliminados: {}", eliminados);
        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }

}
