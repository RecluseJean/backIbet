package pe.dcs.registry.validation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioValidation {

    //Atributos
    private String username;

    //Constructores
    public UsuarioValidation(String username) {
        this.username = username;
    }
}
