package pe.dcs.registry.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol {

    //Atributos
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_rol")
    private UUID id;

    @Column(name = "tx_descripcion")
    private String descripcion;

    @Column(name = "is_habilitado")
    private Boolean habilitado;

    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios;

    //Constructores
    //-- Consultar Rol: Existencia
    public Rol() {
    }

    public Rol(UUID id, Boolean habilitado) {
        this.id = id;
        this.habilitado = habilitado;
    }

    //-- Consultar Roles: Anexo -> UserDetailsImpl
    public Rol(UUID id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }
}
