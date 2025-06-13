package pe.dcs.registry.validation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CongreganteValidation {

    //Atributos
    private String apellido;
    private String nombre;

    //Constructores
    public CongreganteValidation(String apellido, String nombre) {
        this.apellido = apellido;
        this.nombre = nombre;
    }
}
