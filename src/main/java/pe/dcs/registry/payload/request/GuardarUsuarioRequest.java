package pe.dcs.registry.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuardarUsuarioRequest {

    //Atributos
    @NotEmpty(message = "El campo es requerido.")
    @Size(min = 6, message = "Debe contener al menos 6 caracteres.")
    @Size(max = 20, message = "Debe contener como máximo 20 caracteres.")
    private String username;

    @NotEmpty(message = "El campo es requerido.")
    @Size(min = 8, message = "Debe contener al menos 8 caracteres.")
    @Size(max = 20, message = "Debe contener como máximo 20 caracteres.")
    @Pattern(regexp = "^(?=.*\\d.*)(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[@#*$:^%&].*).*$", message = "Debe contener al menos un número, una minúscula, una mayúscula y un carácter especial.")
    private String password;

    //Constructores
}
