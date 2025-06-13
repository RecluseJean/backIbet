package pe.dcs.registry.service;

import pe.dcs.registry.entity.Rol;

import java.util.Optional;

public interface IRolService {

    Optional<Rol> findRolByDescripcion(String descripcion);
}
