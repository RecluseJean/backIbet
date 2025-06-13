package pe.dcs.registry.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.dcs.registry.entity.Usuario;
import pe.dcs.registry.repository.IUsuarioDAO;
import pe.dcs.registry.service.IUsuarioService;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UsuarioServiceImpl implements IUsuarioService {

    final IUsuarioDAO data;

    public UsuarioServiceImpl(IUsuarioDAO data) {
        this.data = data;
    }

    @Override
    public Set<Usuario> findUsuarioToValidate(String username) {
        return data.findUsuarioToValidate(username);
    }

    @Override
    public Optional<Usuario> findUsuarioToAuthentication(String username) {
        return data.findUsuarioToAuthentication(username);
    }

    @Override
    public void saveUsuario(Usuario usuario) {
        data.save(usuario);
    }
}
