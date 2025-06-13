package pe.dcs.registry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.dcs.registry.entity.Usuario;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, UUID> {

    @Query("SELECT NEW Usuario(u.username) " +
            "FROM Usuario u " +
            "WHERE u.username LIKE ?1")
    Set<Usuario> findUsuarioToValidate(String username);

    @Query("SELECT NEW Usuario(u.id, u.username, u.password, u.habilitado) " +
            "FROM Usuario u " +
            "WHERE u.username LIKE ?1")
    Optional<Usuario> findUsuarioToUserDetailsImpl(String username);

    @Query("SELECT NEW Usuario(u.id, u.username) " +
            "FROM Usuario u " +
            "WHERE u.username LIKE ?1")
    Optional<Usuario> findUsuarioToAuthentication(String username);
}
