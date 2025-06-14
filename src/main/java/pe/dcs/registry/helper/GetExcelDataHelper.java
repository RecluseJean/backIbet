package pe.dcs.registry.helper;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import pe.dcs.registry.constant.GeneralConstant;
import pe.dcs.registry.payload.request.GuardarCongreganteRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class GetExcelDataHelper {

    private GetExcelDataHelper() {
    }

    public static boolean esFormatoExcel(MultipartFile excel) {

        return GeneralConstant.TIPO_EXTENSION_EXCEL.equals(excel.getContentType());
    }

    public static Set<GuardarCongreganteRequest> getCongregantesFromExcel(InputStream inputStream) {

        try {
            Set<GuardarCongreganteRequest> listaCongregantes = new HashSet<>();

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet worksheet = workbook.getSheetAt(0);

            for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
                if (index > 0) {
                    XSSFRow row = worksheet.getRow(index);

                    DataFormatter formatter = new DataFormatter();

                    String apellido = formatter.formatCellValue(row.getCell(2));
                    String nombre = formatter.formatCellValue(row.getCell(3));
                    String sexo = formatter.formatCellValue(row.getCell(4));
                    String telefono = formatter.formatCellValue(row.getCell(5));
                    String direccion = formatter.formatCellValue(row.getCell(6));
                    String mesCumpleanios = formatter.formatCellValue(row.getCell(7));
                    String fechaNacimiento = formatter.formatCellValue(row.getCell(8));
                    String edad = formatter.formatCellValue(row.getCell(9));
                    String estadoCivil = formatter.formatCellValue(row.getCell(10));
                    String cantidadHijo = formatter.formatCellValue(row.getCell(11));
                    String tiempoIBET = formatter.formatCellValue(row.getCell(12));
                    String iglesiaAnterior = formatter.formatCellValue(row.getCell(13));
                    String bautizado = formatter.formatCellValue(row.getCell(14));
                    String iglesiaBautizo = formatter.formatCellValue(row.getCell(15));
                    String tiempoBautizo = formatter.formatCellValue(row.getCell(16));
                    String estudiando = formatter.formatCellValue(row.getCell(17));
                    String curso = formatter.formatCellValue(row.getCell(18));
                    String cursoUltimo = formatter.formatCellValue(row.getCell(19));
                    String tiempoSinEstudio = formatter.formatCellValue(row.getCell(20));
                    String motivoSinEstudio = formatter.formatCellValue(row.getCell(21));
                    String participandoGPC = formatter.formatCellValue(row.getCell(22));
                    String motivoNoGPC = formatter.formatCellValue(row.getCell(23));
                    String numeroGPC = formatter.formatCellValue(row.getCell(24));
                    String enMinisterio = formatter.formatCellValue(row.getCell(25));
                    String ministerio = formatter.formatCellValue(row.getCell(26));
                    String cargo = formatter.formatCellValue(row.getCell(27));
                    String estudiandoEscuelaCiervo = formatter.formatCellValue(row.getCell(28));
                    String cursoEscuelaCiervo = formatter.formatCellValue(row.getCell(29));
                    String cursoUltimoEscuelaCiervo = formatter.formatCellValue(row.getCell(30));
                    String tiempoSinEstudioEscuelaCiervo = formatter.formatCellValue(row.getCell(31));
                    String motivoSinEstudioEscuelaCiervo = formatter.formatCellValue(row.getCell(32));

                    GuardarCongreganteRequest congreganteRequest = new GuardarCongreganteRequest(
                            apellido,
                            nombre,
                            sexo,
                            telefono,
                            direccion,
                            mesCumpleanios,
                            fechaNacimiento,
                            edad,
                            estadoCivil,
                            cantidadHijo,
                            tiempoIBET,
                            iglesiaAnterior,
                            bautizado,
                            iglesiaBautizo,
                            tiempoBautizo,
                            estudiando,
                            curso,
                            cursoUltimo,
                            tiempoSinEstudio,
                            motivoSinEstudio,
                            participandoGPC,
                            motivoNoGPC,
                            numeroGPC,
                            enMinisterio,
                            ministerio,
                            cargo,
                            estudiandoEscuelaCiervo,
                            cursoEscuelaCiervo,
                            cursoUltimoEscuelaCiervo,
                            tiempoSinEstudioEscuelaCiervo,
                            motivoSinEstudioEscuelaCiervo
                    );

                    listaCongregantes.add(congreganteRequest);
                }
            }

            return listaCongregantes;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al analizar el Archivo Excel");
        }
    }
}
