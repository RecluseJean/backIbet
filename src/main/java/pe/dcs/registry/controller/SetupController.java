package pe.dcs.registry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.dcs.registry.entity.Rol;
import pe.dcs.registry.entity.Usuario;
import pe.dcs.registry.repository.IRolDAO;
import pe.dcs.registry.repository.IUsuarioDAO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/setup")
public class SetupController {

    @Autowired
    private IRolDAO rolDAO;

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/init")
    public String initDatabase() {
        // Crear roles
        Rol adminRole = new Rol(UUID.fromString("1e7a4288-2ff8-4077-84e7-6d9adb75c0c1"), "ROLE_ADMIN");
        adminRole.setHabilitado(true);

        Rol encargadoRole = new Rol(UUID.fromString("0a08a32a-f1b7-4e24-ac21-e6fb19cde117"), "ROLE_ENCARGADO");
        encargadoRole.setHabilitado(true);

        rolDAO.saveAll(List.of(adminRole, encargadoRole));

        // Crear usuario
        Usuario user = new Usuario();
        user.setId(UUID.fromString("747f8f82-f915-45e9-92b6-5dfcc7bc435c"));
        user.setUsername("master_registry");
        user.setPassword(passwordEncoder.encode("123456")); // puedes cambiar esto
        user.setFechaRegistro(LocalDateTime.now());
        user.setHabilitado(true);
        user.setRoles(Set.of(adminRole)); // asignamos ROLE_ADMIN

        usuarioDAO.save(user);

        return "Datos iniciales insertados con éxito.";
    }
}
