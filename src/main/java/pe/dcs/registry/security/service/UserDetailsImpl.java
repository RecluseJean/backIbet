package pe.dcs.registry.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pe.dcs.registry.entity.Rol;
import pe.dcs.registry.entity.Usuario;

import java.util.*;

@Service
@Getter
public class UserDetailsImpl implements UserDetails {

    //Atributos
    private UUID id;
    private String username;
    private String password;

    @JsonIgnore
    private Boolean habilitado;

    private Collection<? extends GrantedAuthority> authorities;

    //Constructores
    @Autowired
    public UserDetailsImpl() {
    }

    public UserDetailsImpl(UUID id, String username, String password, Boolean habilitado, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.habilitado = habilitado;
        this.authorities = authorities;
    }

    //Métodos
    public static UserDetailsImpl build(Usuario usuario, Set<Rol> rolesUsuario) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        rolesUsuario
                .forEach(rol -> authorities.add(new SimpleGrantedAuthority(rol.getDescripcion())));


        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getHabilitado(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl usuario = (UserDetailsImpl) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
