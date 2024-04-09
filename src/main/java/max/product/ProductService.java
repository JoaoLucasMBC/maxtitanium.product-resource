package max.product;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import max.product.exceptions.NotEnoughStockException;
import max.product.exceptions.ProductNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @CachePut(value = "products", key = "#result.id()")
    public Product create(Product in) {
        return productRepository.save(new ProductModel(in)).to();
    }

    @Cacheable(value = "products", key = "#id", unless = "#result == null")
    public Product read(String id) {
        Product product = productRepository.findById(id).map(ProductModel::to).orElse(null);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    @CachePut(value = "products", key = "#result.id()", unless = "#result == null")
    public Product update(String id, Product in) {
        ProductModel dbProduct = productRepository.findById(id).orElse(null);

        if (dbProduct == null) {
            throw new ProductNotFoundException(id);
        }

        if (in.name() != null) {
            dbProduct.name(in.name());
        }

        if (in.price() != null) {
            dbProduct.price(in.price());
        }

        if (in.description() != null) {
            dbProduct.description(in.description());
        }

        if (in.category() != null) {
            dbProduct.category(in.category());
        }

        if (in.brand() != null) {
            dbProduct.brand(in.brand());
        }

        if (in.stock() != null) {
            dbProduct.stock(in.stock());
        }

        return productRepository.save(new ProductModel(in)).to();
    }

    @CachePut(value = "products", key = "#id")
    public Product delete(String id) {
        final ProductModel dbProduct = productRepository.findById(id).orElse(null);

        if (dbProduct == null) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
        return dbProduct.to();
    }

    public List<Product> list(Double min_price, Double max_price) {

        if (min_price != null && max_price != null) {
            return productRepository.findByPriceBetween(min_price, max_price).stream().map(ProductModel::to).collect(Collectors.toList());
        }
        else if (min_price != null) {
            return productRepository.findByPriceGreaterThanEqual(min_price).stream().map(ProductModel::to).collect(Collectors.toList());
        }
        else if (max_price != null) {
            return productRepository.findByPriceLessThanEqual(max_price).stream().map(ProductModel::to).collect(Collectors.toList());
        }
        else {
            return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(ProductModel::to)
                .collect(Collectors.toList());
        }
        
    }

    @CachePut(value = "products", key = "#result.id()", unless = "#result == null")
    public Product consume(String id, Integer quantity) {
        final ProductModel dbProduct = productRepository.findById(id).orElse(null);

        if (dbProduct == null) {
            throw new ProductNotFoundException(id);
        }

        if (dbProduct.stock() < quantity) {
            throw new NotEnoughStockException("Product with ID " + id + " has only " + dbProduct.stock() + " units in stock.");
        }

        dbProduct.stock(dbProduct.stock() - quantity);
        return productRepository.save(dbProduct).to();
    }

    @CachePut(value = "products", key = "#result.id()", unless = "#result == null")
    public Product replenish(String id, Integer quantity) {
        final ProductModel dbProduct = productRepository.findById(id).orElse(null);

        if (dbProduct == null) {
            throw new ProductNotFoundException(id);
        }

        dbProduct.stock(dbProduct.stock() + quantity);
        return productRepository.save(dbProduct).to();
    }
}
