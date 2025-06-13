package pe.dcs.registry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.dcs.registry.entity.Rol;
import pe.dcs.registry.entity.Usuario;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface IRolDAO extends JpaRepository<Rol, UUID> {

    @Query("SELECT NEW Rol(r.id, r.descripcion) " +
            "FROM Rol r " +
            "JOIN r.usuarios u " +
            "WHERE u = ?1 " +
            "AND r.habilitado = TRUE")
    Set<Rol> findRolesByUsuario(Usuario usuario);

    @Query("SELECT NEW Rol(r.id, r.habilitado) " +
            "FROM Rol r " +
            "WHERE r.descripcion LIKE ?1")
    Optional<Rol> findRolByDescripcion(String descripcion);
}
