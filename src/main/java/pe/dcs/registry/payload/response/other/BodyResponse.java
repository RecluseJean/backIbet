package pe.dcs.registry.payload.response.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import pe.dcs.registry.payload.response.CongreganteResponse;
import pe.dcs.registry.payload.response.SigninResponse;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class BodyResponse {

    //Atributos
    @JsonInclude(Include.NON_NULL)
    Map<String, String> errors;

    @JsonInclude(Include.NON_NULL)
    SigninResponse signin;

    @JsonInclude(Include.NON_NULL)
    PaginationResponse pagination;

    @JsonInclude(Include.NON_NULL)
    Set<CongreganteResponse> congregantes;

    @JsonInclude(Include.NON_NULL)
    CongreganteResponse congregante;

    //Constructores
    //-- Validación de Campos
    public BodyResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    //-- Signin Response
    public BodyResponse(SigninResponse signin) {
        this.signin = signin;
    }

    //-- Congregante Response (List) con Paginacion
    public BodyResponse(PaginationResponse pagination, Set<CongreganteResponse> congregantes) {
        this.pagination = pagination;
        this.congregantes = congregantes;
    }

    //-- Congregante Response (List) sin Paginacion
    public BodyResponse(Set<CongreganteResponse> congregantes) {
        this.congregantes = congregantes;
    }

    //-- Congregante Response (Detalle)
    public BodyResponse(CongreganteResponse congregante) {
        this.congregante = congregante;
    }
}
