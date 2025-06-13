package pe.dcs.registry.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuardarCongreganteRequest {

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
    private String estudiandoEscuelaCiervo;
    private String cursoEscuelaCiervo;
    private String cursoUltimoEscuelaCiervo;
    private String tiempoSinEstudioEscuelaCiervo;
    private String motivoSinEstudioEscuelaCiervo;

    //Constructores
    public GuardarCongreganteRequest() {
    }

    //-- Carga Masiva
    public GuardarCongreganteRequest(String apellido, String nombre, String sexo, String telefono, String direccion,
                                     String mesCumpleanios, String fechaNacimiento, String edad, String estadoCivil,
                                     String cantidadHijo, String tiempoIBET, String iglesiaAnterior, String bautizado,
                                     String iglesiaBautizo, String tiempoBautizo, String estudiando, String curso,
                                     String cursoUltimo, String tiempoSinEstudio, String motivoSinEstudio, String participandoGPC,
                                     String motivoNoGPC, String numeroGPC, String enMinisterio, String ministerio, String cargo,
                                     String estudiandoEscuelaCiervo, String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo,
                                     String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.sexo = sexo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.mesCumpleanios = mesCumpleanios;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.estadoCivil = estadoCivil;
        this.cantidadHijo = cantidadHijo;
        this.tiempoIBET = tiempoIBET;
        this.iglesiaAnterior = iglesiaAnterior;
        this.bautizado = bautizado;
        this.iglesiaBautizo = iglesiaBautizo;
        this.tiempoBautizo = tiempoBautizo;
        this.estudiando = estudiando;
        this.curso = curso;
        this.cursoUltimo = cursoUltimo;
        this.tiempoSinEstudio = tiempoSinEstudio;
        this.motivoSinEstudio = motivoSinEstudio;
        this.participandoGPC = participandoGPC;
        this.motivoNoGPC = motivoNoGPC;
        this.numeroGPC = numeroGPC;
        this.enMinisterio = enMinisterio;
        this.ministerio = ministerio;
        this.cargo = cargo;
        this.estudiandoEscuelaCiervo = estudiandoEscuelaCiervo;
        this.cursoEscuelaCiervo = cursoEscuelaCiervo;
        this.cursoUltimoEscuelaCiervo = cursoUltimoEscuelaCiervo;
        this.tiempoSinEstudioEscuelaCiervo = tiempoSinEstudioEscuelaCiervo;
        this.motivoSinEstudioEscuelaCiervo = motivoSinEstudioEscuelaCiervo;
    }

    //-- Guardar
    public GuardarCongreganteRequest(String apellido, String nombre, String sexo, String telefono, String direccion,
                                     String fechaNacimiento, String estadoCivil, String cantidadHijo, String tiempoIBET,
                                     String iglesiaAnterior, String bautizado, String iglesiaBautizo, String tiempoBautizo,
                                     String estudiando, String curso, String cursoUltimo, String tiempoSinEstudio,
                                     String motivoSinEstudio, String participandoGPC, String motivoNoGPC, String numeroGPC,
                                     String enMinisterio, String ministerio, String cargo, String estudiandoEscuelaCiervo,
                                     String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo, String tiempoSinEstudioEscuelaCiervo,
                                     String motivoSinEstudioEscuelaCiervo) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.sexo = sexo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoCivil = estadoCivil;
        this.cantidadHijo = cantidadHijo;
        this.tiempoIBET = tiempoIBET;
        this.iglesiaAnterior = iglesiaAnterior;
        this.bautizado = bautizado;
        this.iglesiaBautizo = iglesiaBautizo;
        this.tiempoBautizo = tiempoBautizo;
        this.estudiando = estudiando;
        this.curso = curso;
        this.cursoUltimo = cursoUltimo;
        this.tiempoSinEstudio = tiempoSinEstudio;
        this.motivoSinEstudio = motivoSinEstudio;
        this.participandoGPC = participandoGPC;
        this.motivoNoGPC = motivoNoGPC;
        this.numeroGPC = numeroGPC;
        this.enMinisterio = enMinisterio;
        this.ministerio = ministerio;
        this.cargo = cargo;
        this.estudiandoEscuelaCiervo = estudiandoEscuelaCiervo;
        this.cursoEscuelaCiervo = cursoEscuelaCiervo;
        this.cursoUltimoEscuelaCiervo = cursoUltimoEscuelaCiervo;
        this.tiempoSinEstudioEscuelaCiervo = tiempoSinEstudioEscuelaCiervo;
        this.motivoSinEstudioEscuelaCiervo = motivoSinEstudioEscuelaCiervo;
    }
}
