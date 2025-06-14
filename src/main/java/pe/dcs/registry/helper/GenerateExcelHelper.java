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

            crearCelda(row, personalizarFilaExcel(), contadorColumna++, String.valueOf(congreganteResponse.getIndice()));
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, DataConverter.getFirstLetterAndUpperFromText(congreganteResponse.getApellido()));
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getApellido());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getNombre());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getSexo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getTelefono());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getDireccion());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getMesCumpleanios());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getFechaNacimiento());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getEdad());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getEstadoCivil());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getCantidadHijo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getTiempoIBET());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getIglesiaAnterior());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getBautizado());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getIglesiaBautizo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getTiempoBautizo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getEstudiando());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getCurso());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getCursoUltimo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getTiempoSinEstudio());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getMotivoSinEstudio());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getParticipandoGPC());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getMotivoNoGPC());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getNumeroGPC());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getEnMinisterio());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getEnMinisterio());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getCargo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getEstudiandoEscuelaCiervo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getCursoEscuelaCiervo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getCursoUltimoEscuelaCiervo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna++, congreganteResponse.getTiempoSinEstudioEscuelaCiervo());
            crearCelda(row, personalizarFilaExcel(), contadorColumna, congreganteResponse.getMotivoSinEstudioEscuelaCiervo());
        }
    }

    private static void crearCelda(Row row, CellStyle style, int contadorColumna, String valorColumna) {

        sheet.autoSizeColumn(contadorColumna);

        Cell cell = row.createCell(contadorColumna);

        cell.setCellValue(valorColumna);

        cell.setCellStyle(style);
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
