package pe.dcs.registry.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarCongreganteRequest {

    //Atributos
    @NotEmpty(message = "El campo es requerido.")
    private String apellido;

    @NotEmpty(message = "El campo es requerido.")
    private String nombre;

    private String sexo;
    private String telefono;
    private String direccion;
    private String mesCumpleanios;
    private String fechaNacimiento;
    private String edad;
    private String estadoCivil;
    private String cantidadHijo;
    private String tiempoIBET;
    private String iglesiaAnterior;
    private String bautizado;
    private String iglesiaBautizo;
    private String tiempoBautizo;
    private String estudiando;
    private String curso;
    private String cursoUltimo;
    private String tiempoSinEstudio;
    private String motivoSinEstudio;
    private String participandoGPC;
    private String motivoNoGPC;
    private String numeroGPC;
    private String enMinisterio;
    private String ministerio;
    private String cargo;
    private Boolean habilitado;
    private String estudiandoEscuelaCiervo;
    private String cursoEscuelaCiervo;
    private String cursoUltimoEscuelaCiervo;
    private String tiempoSinEstudioEscuelaCiervo;
    private String motivoSinEstudioEscuelaCiervo;

    //Constructores
}
