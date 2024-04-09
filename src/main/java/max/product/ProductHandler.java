package max.product;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import max.product.exceptions.ExceptionOut;
import max.product.exceptions.NotEnoughStockException;
import max.product.exceptions.ProductNotFoundException;

@ControllerAdvice
public class ProductHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    ResponseEntity<ExceptionOut> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionOut(HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(NotEnoughStockException.class)
    ResponseEntity<ExceptionOut> handleNotEnoughStockException(NotEnoughStockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionOut(HttpStatus.BAD_REQUEST, e.getMessage(), LocalDateTime.now().toString()));
    }
    
}
