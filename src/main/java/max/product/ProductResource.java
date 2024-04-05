package max.product;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ProductResource implements ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/info")
	public ResponseEntity<Map<String, String>> info() {
        return new ResponseEntity<Map<String, String>>(
            Map.ofEntries(
                Map.entry("microservice.name", ProductApplication.class.getSimpleName()),
                Map.entry("os.arch", System.getProperty("os.arch")),
                Map.entry("os.name", System.getProperty("os.name")),
                Map.entry("os.version", System.getProperty("os.version")),
                Map.entry("file.separator", System.getProperty("file.separator")),
                Map.entry("java.class.path", System.getProperty("java.class.path")),
                Map.entry("java.home", System.getProperty("java.home")),
                Map.entry("java.vendor", System.getProperty("java.vendor")),
                Map.entry("java.vendor.url", System.getProperty("java.vendor.url")),
                Map.entry("java.version", System.getProperty("java.version")),
                Map.entry("line.separator", System.getProperty("line.separator")),
                Map.entry("path.separator", System.getProperty("path.separator")),
                Map.entry("user.dir", System.getProperty("user.dir")),
                Map.entry("user.home", System.getProperty("user.home")),
                Map.entry("user.name", System.getProperty("user.name")),
                Map.entry("jar", new java.io.File(
                    ProductApplication.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath()
                    ).toString())
            ), HttpStatus.OK
        );
	}

	@Override
	public ResponseEntity<ProductOut> read(String id) {

        final Product dbProduct = productService.read(id);

        if (dbProduct == null) {
            throw new RuntimeException("Produict id not found");
        }

		return ResponseEntity.ok(
            ProductParser.to(dbProduct)
        );
	}

	@Override
	public ResponseEntity<ProductOut> create(ProductIn productIn) {
        // parser
        Product product = ProductParser.to(productIn);

        // insert using service
        product = productService.create(product);

        return ResponseEntity.created(
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.id())
                .toUri())
            .body(ProductParser.to(product));
	}

	@Override
	public ResponseEntity<ProductOut> update(String id, ProductIn productIn) {
        // parser
        Product product = ProductParser.to(productIn);

        // insert using service
        product = productService.update(id, product);

        if (product == null) {
            throw new RuntimeException("Product id not found");
        }

        return ResponseEntity.ok(
            ProductParser.to(product)
        );
	}

	@Override
	public ResponseEntity<ProductOut> delete(String id) {

        final Product dbProduct = productService.delete(id);

        if (dbProduct == null) {
            throw new RuntimeException("Product id not found");
        }

        return ResponseEntity.ok(
            ProductParser.to(dbProduct)
        );
    }

    @Override
    public ResponseEntity<List<ProductOut>> list(Double min_price, Double max_price) {

        List<Product> products = productService.list(min_price, max_price);

        return ResponseEntity.ok(
            ProductParser.to(products)
        );
    }

    @Override
    public ResponseEntity<ProductOut> consume(StockIn stockIn) {

        final Product dbProduct = productService.consume(stockIn.id(), stockIn.quantity());

        if (dbProduct == null) {
            throw new RuntimeException("Product id not found");
        }

        return ResponseEntity.ok(
            ProductParser.to(dbProduct)
        );
    }

    @Override
    public ResponseEntity<ProductOut> replenish(StockIn stockIn) {

        final Product dbProduct = productService.replenish(stockIn.id(), stockIn.quantity());

        if (dbProduct == null) {
            throw new RuntimeException("Product id not found");
        }

        return ResponseEntity.ok(
            ProductParser.to(dbProduct)
        );
    }
    
}
