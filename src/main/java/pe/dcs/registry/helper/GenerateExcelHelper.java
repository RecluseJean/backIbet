package pe.dcs.registry.helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import pe.dcs.registry.payload.response.CongreganteResponse;
import pe.dcs.registry.util.DataConverter;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public class GenerateExcelHelper {

    private static final String SHEET_CONGREGANTES = "Congregantes";
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;

    private GenerateExcelHelper() {
    }

    public static ByteArrayInputStream generateExcelFromCongregantes(Set<CongreganteResponse> listCongregantes) {

        try {
            workbook = new XSSFWorkbook();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            generarHojaExcel();
            crearCabeceraExcel();
            crearFilasExcel(listCongregantes);
            ajustarAnchosColumnas();
            workbook.write(outputStream);

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al generar el Archivo Excel");
        }
    }

    private static void generarHojaExcel() {

        sheet = workbook.createSheet(SHEET_CONGREGANTES);
    }

    private static void crearCabeceraExcel() {

        Row row = sheet.createRow(0);

        crearCelda(row, personalizarCabeceraExcel(row), 0, "N°");
        crearCelda(row, personalizarCabeceraExcel(row), 1, "Letra");
        crearCelda(row, personalizarCabeceraExcel(row), 2, "Apellidos");
        crearCelda(row, personalizarCabeceraExcel(row), 3, "Nombres");
        crearCelda(row, personalizarCabeceraExcel(row), 4, "Sexo");
        crearCelda(row, personalizarCabeceraExcel(row), 5, "Número de Teléfono");
        crearCelda(row, personalizarCabeceraExcel(row), 6, "Dirección");
        crearCelda(row, personalizarCabeceraExcel(row), 7, "Mes de Cumpleaños");
        crearCelda(row, personalizarCabeceraExcel(row), 8, "Fecha de Nacimiento");
        crearCelda(row, personalizarCabeceraExcel(row), 9, "Edad");
        crearCelda(row, personalizarCabeceraExcel(row), 10, "Estado Civil");
        crearCelda(row, personalizarCabeceraExcel(row), 11, "Hijos");
        crearCelda(row, personalizarCabeceraExcel(row), 12, "Tiempo en la IBET");
        crearCelda(row, personalizarCabeceraExcel(row), 13, "Iglesia Anterior");
        crearCelda(row, personalizarCabeceraExcel(row), 14, "Bautizado");
        crearCelda(row, personalizarCabeceraExcel(row), 15, "Iglesia Bautizo");
        crearCelda(row, personalizarCabeceraExcel(row), 16, "Tiempo de Bautizo");
        crearCelda(row, personalizarCabeceraExcel(row), 17, "Estudiando");
        crearCelda(row, personalizarCabeceraExcel(row), 18, "Curso");
        crearCelda(row, personalizarCabeceraExcel(row), 19, "Último Curso de Estudio");
        crearCelda(row, personalizarCabeceraExcel(row), 20, "Tiempo que dejo de Estudiar");
        crearCelda(row, personalizarCabeceraExcel(row), 21, "Motivo");
        crearCelda(row, personalizarCabeceraExcel(row), 22, "Participa en una GPC");
        crearCelda(row, personalizarCabeceraExcel(row), 23, "NO - Motivo");
        crearCelda(row, personalizarCabeceraExcel(row), 24, "SI - N° GPC");
        crearCelda(row, personalizarCabeceraExcel(row), 25, "Participa en Ministerio");
        crearCelda(row, personalizarCabeceraExcel(row), 26, "Ministerio");
        crearCelda(row, personalizarCabeceraExcel(row), 27, "Cargo");
        crearCelda(row, personalizarCabeceraExcel(row), 28, "Estudiando - Escuela de Ciervos");
        crearCelda(row, personalizarCabeceraExcel(row), 29, "Curso - Escuela de Ciervos");
        crearCelda(row, personalizarCabeceraExcel(row), 30, "Último Curso - Escuela de Ciervos");
        crearCelda(row, personalizarCabeceraExcel(row), 31, "Tiempo sin Estudios - Escuela de Ciervos");
        crearCelda(row, personalizarCabeceraExcel(row), 32, "Motivo - Escuela de Ciervos");
    }

    private static void crearFilasExcel(Set<CongreganteResponse> listCongregantes) {

        int contadorFila = 1;

        for (CongreganteResponse congreganteResponse : listCongregantes) {
            Row row = sheet.createRow(contadorFila++);

            int contadorColumna = 0;

            crearCelda(row, personalizarFilaExcel(), 0, String.valueOf(congreganteResponse.getIndice()));
            crearCelda(row, personalizarFilaExcel(), 1, DataConverter.getFirstLetterAndUpperFromText(congreganteResponse.getApellido()));
            crearCelda(row, personalizarFilaExcel(), 2, congreganteResponse.getApellido());
            crearCelda(row, personalizarFilaExcel(), 3, congreganteResponse.getNombre());
            crearCelda(row, personalizarFilaExcel(), 4, congreganteResponse.getSexo());
            crearCelda(row, personalizarFilaExcel(), 5, congreganteResponse.getTelefono());
            crearCelda(row, personalizarFilaExcel(), 6, congreganteResponse.getDireccion());
            crearCelda(row, personalizarFilaExcel(), 7, congreganteResponse.getMesCumpleanios());
            crearCelda(row, personalizarFilaExcel(), 8, congreganteResponse.getFechaNacimiento());
            crearCelda(row, personalizarFilaExcel(), 9, congreganteResponse.getEdad());
            crearCelda(row, personalizarFilaExcel(), 10, congreganteResponse.getEstadoCivil());
            crearCelda(row, personalizarFilaExcel(), 11, congreganteResponse.getCantidadHijo());
            crearCelda(row, personalizarFilaExcel(), 12, congreganteResponse.getTiempoIBET());
            crearCelda(row, personalizarFilaExcel(), 13, congreganteResponse.getIglesiaAnterior());
            crearCelda(row, personalizarFilaExcel(), 14, congreganteResponse.getBautizado());
            crearCelda(row, personalizarFilaExcel(), 15, congreganteResponse.getIglesiaBautizo());
            crearCelda(row, personalizarFilaExcel(), 16, congreganteResponse.getTiempoBautizo());
            crearCelda(row, personalizarFilaExcel(), 17, congreganteResponse.getEstudiando());
            crearCelda(row, personalizarFilaExcel(), 18, congreganteResponse.getCurso());
            crearCelda(row, personalizarFilaExcel(), 19, congreganteResponse.getCursoUltimo());
            crearCelda(row, personalizarFilaExcel(), 20, congreganteResponse.getTiempoSinEstudio());
            crearCelda(row, personalizarFilaExcel(), 21, congreganteResponse.getMotivoSinEstudio());
            crearCelda(row, personalizarFilaExcel(), 22, congreganteResponse.getParticipandoGPC());
            crearCelda(row, personalizarFilaExcel(), 23, congreganteResponse.getMotivoNoGPC());
            crearCelda(row, personalizarFilaExcel(), 24, congreganteResponse.getNumeroGPC());
            crearCelda(row, personalizarFilaExcel(), 25, congreganteResponse.getEnMinisterio());
            crearCelda(row, personalizarFilaExcel(), 26, congreganteResponse.getEnMinisterio());
            crearCelda(row, personalizarFilaExcel(), 27, congreganteResponse.getCargo());
            crearCelda(row, personalizarFilaExcel(), 28, congreganteResponse.getEstudiandoEscuelaCiervo());
            crearCelda(row, personalizarFilaExcel(), 29, congreganteResponse.getCursoEscuelaCiervo());
            crearCelda(row, personalizarFilaExcel(), 30, congreganteResponse.getCursoUltimoEscuelaCiervo());
            crearCelda(row, personalizarFilaExcel(), 31, congreganteResponse.getTiempoSinEstudioEscuelaCiervo());
            crearCelda(row, personalizarFilaExcel(), 32, congreganteResponse.getMotivoSinEstudioEscuelaCiervo());
        }
    }

    private static void crearCelda(Row row, CellStyle style, int contadorColumna, String valorColumna) {
        // Comentamos o eliminamos esta línea para evitar el error
        // sheet.autoSizeColumn(contadorColumna);

        Cell cell = row.createCell(contadorColumna);
        cell.setCellValue(valorColumna);
        cell.setCellStyle(style);
    }

    private static void ajustarAnchosColumnas() {
        int numeroColumnas = 32; // Número total de columnas que usas (ajusta si cambias)
        for (int i = 0; i < numeroColumnas; i++) {
            sheet.setColumnWidth(i, 20 * 256); // 20 caracteres de ancho
        }
    }

    private static CellStyle personalizarCabeceraExcel(Row row) {

        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        //Personalización de Fuente
        font.setFontName("Arial");
        font.setFontHeight(10);
        font.setBold(true);

        //Personalización de Celdas
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setFont(font);

        row.setHeightInPoints(30);

        return style;
    }

    private static CellStyle personalizarFilaExcel() {

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        //Personalización de Fuente
        font.setFontName("Arial");
        font.setFontHeight(10);
        font.setBold(false);

        //Personalización de Celdas
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        style.setFont(font);

        return style;
    }
}
