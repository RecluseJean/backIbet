package pe.dcs.registry.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {

    //Atributos
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_usuario")
    private UUID id;

    @Column(name = "tx_username")
    private String username;

    @Column(name = "tx_password")
    private String password;

    @Column(name = "dt_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "dt_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "is_habilitado")
    private Boolean habilitado;

    @ManyToMany
    @JoinTable(name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "id_rol"))
    private Set<Rol> roles;

    //Constructores
    //-- Registrar Usuario
    public Usuario(String username, String password, LocalDateTime fechaRegistro, Boolean habilitado, Set<Rol> roles) {
        this.username = username;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
        this.habilitado = habilitado;
        this.roles = roles;
    }

    //-- Consultar Usuario: Validación
    public Usuario(String username) {
        this.username = username;
    }

    //-- Consultar Usuario: Anexo -> UserDetailsImpl
    public Usuario(UUID id, String username, String password, Boolean habilitado) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.habilitado = habilitado;
    }

    //-- Consultar Usuario: Anexo -> Authentication
    public Usuario(UUID id, String username) {
        this.id = id;
        this.username = username;
    }
}
