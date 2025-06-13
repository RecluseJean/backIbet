package pe.dcs.registry.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.dcs.registry.constant.GeneralConstant;
import pe.dcs.registry.entity.Congregante;
import pe.dcs.registry.helper.GenerateExcelHelper;
import pe.dcs.registry.payload.response.CongreganteResponse;
import pe.dcs.registry.payload.response.other.ServiceResponse;
import pe.dcs.registry.service.ICongreganteService;
import pe.dcs.registry.util.DataConverter;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static pe.dcs.registry.constant.ControllerConstant.*;
import static pe.dcs.registry.constant.GeneralConstant.NOMBRE_ARCHIVO_EXCEL;

@RestController
@RequestMapping("/reporte")
@CrossOrigin
public class ReporteController {

    private static final Logger logger = LogManager.getLogger(ReporteController.class);

    private static final String INICIO_TITULO_GENERAR_REPORTE_EXCEL_CONGREGANTES_SERVICE = "---- Inicio de Servicio: Reporte Excel de Congregantes ----";
    private static final String FIN_TITULO_GENERAR_REPORTE_EXCEL_CONGREGANTES_SERVICE = "---- Fin de Servicio: Reporte Excel de Congregantes ----";

    final ICongreganteService congreganteService;

    public ReporteController(ICongreganteService congreganteService) {
        this.congreganteService = congreganteService;
    }

    @GetMapping("/v1/congregantes/excel")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ENCARGADO')")
    public ResponseEntity<Object> reporteCongregantes(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(value = "apellido", required = false) String apellido,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "fechaNacimiento", required = false) String fechaNacimiento,
            @RequestParam(value = "bautizado", required = false) String bautizado,
            @RequestParam(value = "numeroGPC", required = false) String numeroGPC,
            @RequestParam(value = "ministerio", required = false) String ministerio,
            @RequestParam(value = "curso", required = false) String curso) {

        logger.info(INICIO_TITULO_GENERAR_REPORTE_EXCEL_CONGREGANTES_SERVICE);

        logger.info(VALIDACION_AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty()) {
            logger.error(SIN_AUTHORIZATION_HEADER);

            ServiceResponse serviceResponse = new ServiceResponse(
                    SIN_AUTHORIZATION_HEADER_MESSAGE,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );

            logger.info(FIN_TITULO_GENERAR_REPORTE_EXCEL_CONGREGANTES_SERVICE);

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

        //Iniciando Generación de Lista
        Set<Congregante> listCongregantes = new LinkedHashSet<>();

        Sort sort = Sort.by(PARAM_SORT_APELLIDO, PARAM_SORT_NOMBRE).ascending();

        if (apellidoCongregante == null && nombreCongregante == null && fechaNacimientoCongregante == null
                && bautizadoCongregante == null && numeroGPCCongregante == null && ministerioCongregante == null) {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantes(sort));
        }

        if (apellidoCongregante != null) {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByApellido(apellidoCongregante, sort));
        }

        if (nombreCongregante != null) {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByNombre(nombreCongregante, sort));
        }

        if (fechaNacimientoCongregante != null) {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByFechaNacimiento(fechaNacimientoCongregante, sort));
        }

        if (bautizadoCongregante != null) {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByBautizado(bautizadoCongregante, sort));
        }

        if (numeroGPCCongregante != null) {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByNumeroGPC(numeroGPCCongregante, sort));
        }

        if (ministerioCongregante != null) {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByMinisterio(ministerioCongregante, sort));
        }

        if (cursoCongregante != null) {
            listCongregantes = new LinkedHashSet<>(congreganteService.findCongregantesByCurso(cursoCongregante, sort));
        }

        Set<CongreganteResponse> listCongregantesResponse = new LinkedHashSet<>();

        AtomicLong indiceCongregante = new AtomicLong(1);

        listCongregantes
                .forEach(congregante -> listCongregantesResponse.add(new CongreganteResponse(
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
                        congregante.getMotivoSinEstudioEscuelaCiervo()
                )));

        logger.info("Cantidad de Registros: {}", listCongregantesResponse.size());

        logger.info("Generando Archivo Excel");
        ByteArrayInputStream byteArrayInputStream = GenerateExcelHelper.generateExcelFromCongregantes(listCongregantesResponse);

        InputStreamResource archivo = new InputStreamResource(byteArrayInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", NOMBRE_ARCHIVO_EXCEL);

        logger.info(FIN_TITULO_GENERAR_REPORTE_EXCEL_CONGREGANTES_SERVICE);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .headers(headers)
                .contentType(MediaType.parseMediaType(GeneralConstant.TIPO_EXTENSION_EXCEL))
                .body(archivo);
    }
}
