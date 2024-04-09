package max.product.exceptions;

public class NotEnoughStockException extends RuntimeException{
    
        public NotEnoughStockException(String message) {
            super(message);
        }
}
