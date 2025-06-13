package pe.dcs.registry.payload.response.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceResponse {

    //Atributos
    @JsonInclude(Include.NON_NULL)
    private String message;

    private int statusCode;
    private String statusDescription;

    @JsonInclude(Include.NON_NULL)
    private BodyResponse body;

    //Constructores
    //-- Body sin Data
    public ServiceResponse(String message, int statusCode, String statusDescription) {
        this.message = message;
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

    //-- Body con Data
    public ServiceResponse(int statusCode, String statusDescription, BodyResponse body) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.body = body;
    }

    //-- Body con Message y Data
    public ServiceResponse(String message, int statusCode, String statusDescription, BodyResponse body) {
        this.message = message;
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.body = body;
    }
}
