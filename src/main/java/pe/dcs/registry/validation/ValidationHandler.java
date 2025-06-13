package pe.dcs.registry.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pe.dcs.registry.payload.response.other.BodyResponse;
import pe.dcs.registry.payload.response.other.ServiceResponse;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> fieldValidatorHandler(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(
                error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();

                    errors.put(fieldName, message);
                }
        );

        ServiceResponse serviceResponse = new ServiceResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                new BodyResponse(errors)
        );

        return new ResponseEntity<>(
                serviceResponse,
                HttpStatus.BAD_REQUEST
        );
    }
}
