package pe.dcs.registry.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CongreganteResponse {

    //Atributos
    private UUID id;

    @JsonInclude(Include.NON_NULL)
    private String indice;

    @JsonInclude(Include.NON_NULL)
    private String letraInicial;

    private String apellido;
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

    @JsonInclude(Include.NON_NULL)
    private LocalDate fechaRegistro;

    @JsonInclude(Include.NON_NULL)
    private LocalDate fechaModificacion;

    @JsonInclude(Include.NON_NULL)
    private Boolean habilitado;

    //Constructores
    //-- Lista Congregantes
    public CongreganteResponse(UUID id, String indice, String letraInicial, String apellido, String nombre, String sexo,
                               String telefono, String direccion, String mesCumpleanios, String fechaNacimiento, String edad,
                               String estadoCivil, String cantidadHijo, String tiempoIBET, String iglesiaAnterior, String bautizado,
                               String iglesiaBautizo, String tiempoBautizo, String estudiando, String curso, String cursoUltimo,
                               String tiempoSinEstudio, String motivoSinEstudio, String participandoGPC, String motivoNoGPC,
                               String numeroGPC, String enMinisterio, String ministerio, String cargo, String estudiandoEscuelaCiervo,
                               String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo, String tiempoSinEstudioEscuelaCiervo,
                               String motivoSinEstudioEscuelaCiervo, Boolean habilitado) {
        this.id = id;
        this.indice = indice;
        this.letraInicial = letraInicial;
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
        this.habilitado = habilitado;
    }

    //-- Detalle Congregante
    public CongreganteResponse(UUID id, String apellido, String nombre, String sexo, String telefono, String direccion,
                               String mesCumpleanios, String fechaNacimiento, String edad, String estadoCivil, String cantidadHijo,
                               String tiempoIBET, String iglesiaAnterior, String bautizado, String iglesiaBautizo, String tiempoBautizo,
                               String estudiando, String curso, String cursoUltimo, String tiempoSinEstudio, String motivoSinEstudio,
                               String participandoGPC, String motivoNoGPC, String numeroGPC, String enMinisterio, String ministerio,
                               String cargo, String estudiandoEscuelaCiervo, String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo,
                               String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo, LocalDate fechaRegistro,
                               LocalDate fechaModificacion, Boolean habilitado) {
        this.id = id;
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
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.habilitado = habilitado;
    }

    //-- Reporte Congregantes
    public CongreganteResponse(String indice, String letraInicial, String apellido, String nombre, String sexo, String telefono,
                               String direccion, String mesCumpleanios, String fechaNacimiento, String edad, String estadoCivil,
                               String cantidadHijo, String tiempoIBET, String iglesiaAnterior, String bautizado, String iglesiaBautizo,
                               String tiempoBautizo, String estudiando, String curso, String cursoUltimo, String tiempoSinEstudio,
                               String motivoSinEstudio, String participandoGPC, String motivoNoGPC, String numeroGPC, String enMinisterio,
                               String ministerio, String cargo, String estudiandoEscuelaCiervo, String cursoEscuelaCiervo,
                               String cursoUltimoEscuelaCiervo, String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo) {
        this.indice = indice;
        this.letraInicial = letraInicial;
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
}
