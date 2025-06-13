package pe.dcs.registry.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.dcs.registry.entity.Rol;
import pe.dcs.registry.repository.IRolDAO;
import pe.dcs.registry.service.IRolService;

import java.util.Optional;

@Service
@Transactional
public class RolServiceImpl implements IRolService {

    final IRolDAO data;

    public RolServiceImpl(IRolDAO data) {
        this.data = data;
    }

    @Override
    public Optional<Rol> findRolByDescripcion(String descripcion) {
        return data.findRolByDescripcion(descripcion);
    }
}
