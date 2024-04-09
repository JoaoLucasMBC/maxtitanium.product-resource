package max.product.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ExceptionOut {
    
    private HttpStatus status;
    private String message;
    private String timestamp;

}
