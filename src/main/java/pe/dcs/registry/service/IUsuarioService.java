package pe.dcs.registry.service;

import pe.dcs.registry.entity.Usuario;

import java.util.Optional;
import java.util.Set;

public interface IUsuarioService {

    Set<Usuario> findUsuarioToValidate(String username);

    Optional<Usuario> findUsuarioToAuthentication(String username);

    void saveUsuario(Usuario usuario);
}
