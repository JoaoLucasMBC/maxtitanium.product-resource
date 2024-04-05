package max.product;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter @Setter @Accessors(fluent = true, chain = true)
@AllArgsConstructor @NoArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private Double price;
    private String description;
    private String category;
    private String brand;
    private Integer stock;

}
