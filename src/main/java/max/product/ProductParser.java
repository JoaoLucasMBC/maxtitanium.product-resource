package max.product;

import java.util.ArrayList;
import java.util.List;

public class ProductParser {
    
    public static Product to(ProductIn in) {
        return Product.builder()
            .name(in.name())
            .price(in.price())
            .description(in.description())
            .category(in.category())
            .brand(in.brand())
            .stock(in.stock())
            .build();
    }

    public static ProductOut to(Product product) {
        return ProductOut.builder()
            .id(product.id())
            .name(product.name())
            .price(product.price())
            .description(product.description())
            .category(product.category())
            .brand(product.brand())
            .stock(product.stock())
            .build();
    }

    public static List<ProductOut> to(List<Product> products) {
        ArrayList<ProductOut> list = new ArrayList<>();

        for (Product product : products) {
            list.add(to(product));
        }

        return list;
    }

}
