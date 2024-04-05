package max.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "product")
@EqualsAndHashCode(of = "id")
@Builder @Getter @Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor @AllArgsConstructor
public class ProductModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_product")
    private String id;

    @Column(name = "tx_name")
    private String name;

    @Column(name = "nr_price")
    private Double price;

    @Column(name = "tx_description")
    private String description;

    @Column(name = "tx_category")
    private String category;

    @Column(name = "tx_brand")
    private String brand;

    @Column(name = "nr_stock")
    private Integer stock;

    public ProductModel(Product o) {
        this.id = o.id();
        this.name = o.name();
        this.price = o.price();
        this.description = o.description();
        this.category = o.category();
        this.brand = o.brand();
        this.stock = o.stock();
    }

    public Product to() {
        return Product.builder()
            .id(id)
            .name(name)
            .price(price)
            .description(description)
            .category(category)
            .brand(brand)
            .stock(stock)
            .build();
    }
}
