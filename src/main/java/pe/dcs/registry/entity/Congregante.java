package pe.dcs.registry.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "congregante")
@Getter
@Setter
public class Congregante {

    //Atributos
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_congregante")
    private UUID id;

    @Column(name = "tx_apellido")
    private String apellido;

    @Column(name = "tx_nombre")
    private String nombre;

    @Column(name = "tx_sexo")
    private String sexo;

    @Column(name = "tx_telefono")
    private String telefono;

    @Column(name = "tx_direccion")
    private String direccion;

    @Column(name = "tx_mescumpleaños")
    private String mesCumpleanios;

    @Column(name = "tx_fechanacimiento")
    private String fechaNacimiento;

    @Column(name = "tx_edad")
    private String edad;

    @Column(name = "tx_estadocivil")
    private String estadoCivil;

    @Column(name = "tx_cantidadhijo")
    private String cantidadHijo;

    @Column(name = "tx_tiempoibet")
    private String tiempoIBET;

    @Column(name = "tx_iglesiaanterior")
    private String iglesiaAnterior;

    @Column(name = "tx_bautizado")
    private String bautizado;

    @Column(name = "tx_iglesiabautizo")
    private String iglesiaBautizo;

    @Column(name = "tx_tiempobautizo")
    private String tiempoBautizo;

    @Column(name = "tx_estudiando")
    private String estudiando;

    @Column(name = "tx_curso")
    private String curso;

    @Column(name = "tx_cursoultimo")
    private String cursoUltimo;

    @Column(name = "tx_tiemposinestudio")
    private String tiempoSinEstudio;

    @Column(name = "tx_motivosinestudio")
    private String motivoSinEstudio;

    @Column(name = "tx_participandogpc")
    private String participandoGPC;

    @Column(name = "tx_motivonogpc")
    private String motivoNoGPC;

    @Column(name = "tx_numerogpc")
    private String numeroGPC;

    @Column(name = "tx_enministerio")
    private String enMinisterio;

    @Column(name = "tx_ministerio")
    private String ministerio;

    @Column(name = "tx_cargo")
    private String cargo;

    @Column(name = "tx_estudiando_escuela_ciervo")
    private String estudiandoEscuelaCiervo;

    @Column(name = "tx_curso_escuela_ciervo")
    private String cursoEscuelaCiervo;

    @Column(name = "tx_cursoultimo_escuela_ciervo")
    private String cursoUltimoEscuelaCiervo;

    @Column(name = "tx_tiemposinestudio_escuela_ciervo")
    private String tiempoSinEstudioEscuelaCiervo;

    @Column(name = "tx_motivosinestudio_escuela_ciervo")
    private String motivoSinEstudioEscuelaCiervo;

    @Column(name = "dt_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "dt_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "is_habilitado")
    private Boolean habilitado;

    //Constructores
    //-- Registrar Congregante
    public Congregante(String apellido, String nombre, String sexo, String telefono, String direccion, String mesCumpleanios,
                       String fechaNacimiento, String edad, String estadoCivil, String cantidadHijo, String tiempoIBET,
                       String iglesiaAnterior, String bautizado, String iglesiaBautizo, String tiempoBautizo, String estudiando,
                       String curso, String cursoUltimo, String tiempoSinEstudio, String motivoSinEstudio, String participandoGPC,
                       String motivoNoGPC, String numeroGPC, String enMinisterio, String ministerio, String cargo,
                       String estudiandoEscuelaCiervo, String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo,
                       String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo, LocalDateTime fechaRegistro,
                       Boolean habilitado) {
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
        this.habilitado = habilitado;
    }

    //-- Consultar Congregantes: Lista
    public Congregante(UUID id, String apellido, String nombre, String sexo, String telefono, String direccion,
                       String mesCumpleanios, String fechaNacimiento, String edad, String estadoCivil, String cantidadHijo,
                       String tiempoIBET, String iglesiaAnterior, String bautizado, String iglesiaBautizo, String tiempoBautizo,
                       String estudiando, String curso, String cursoUltimo, String tiempoSinEstudio, String motivoSinEstudio,
                       String participandoGPC, String motivoNoGPC, String numeroGPC, String enMinisterio, String ministerio,
                       String cargo, String estudiandoEscuelaCiervo, String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo,
                       String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo, Boolean habilitado) {
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
        this.habilitado = habilitado;
    }

    //-- Consultar Congregante: Detalle
    public Congregante(UUID id, String apellido, String nombre, String sexo, String telefono, String direccion,
                       String mesCumpleanios, String fechaNacimiento, String edad, String estadoCivil, String cantidadHijo,
                       String tiempoIBET, String iglesiaAnterior, String bautizado, String iglesiaBautizo, String tiempoBautizo,
                       String estudiando, String curso, String cursoUltimo, String tiempoSinEstudio, String motivoSinEstudio,
                       String participandoGPC, String motivoNoGPC, String numeroGPC, String enMinisterio, String ministerio,
                       String cargo, String estudiandoEscuelaCiervo, String cursoEscuelaCiervo, String cursoUltimoEscuelaCiervo,
                       String tiempoSinEstudioEscuelaCiervo, String motivoSinEstudioEscuelaCiervo, LocalDateTime fechaRegistro,
                       LocalDateTime fechaModificacion, Boolean habilitado) {
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

    //-- Consultar Congregante: Validación
    public Congregante(String apellido, String nombre) {
        this.apellido = apellido;
        this.nombre = nombre;
    }
}
