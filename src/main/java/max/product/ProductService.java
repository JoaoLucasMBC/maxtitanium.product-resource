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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductCreate")
    @CachePut(value = "products", key = "#result.id()")
    public Product create(Product in) {
        return productRepository.save(new ProductModel(in)).to();
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductRead")
    @Cacheable(value = "products", key = "#id", unless = "#result == null")
    public Product read(String id) {
        Product product = productRepository.findById(id).map(ProductModel::to).orElse(null);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductUpdate")
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

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductDelete")
    @CachePut(value = "products", key = "#id")
    public Product delete(String id) {
        final ProductModel dbProduct = productRepository.findById(id).orElse(null);

        if (dbProduct == null) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
        return dbProduct.to();
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductList")
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

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductConsume")
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

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductReplenish")
    @CachePut(value = "products", key = "#result.id()", unless = "#result == null")
    public Product replenish(String id, Integer quantity) {
        final ProductModel dbProduct = productRepository.findById(id).orElse(null);

        if (dbProduct == null) {
            throw new ProductNotFoundException(id);
        }

        dbProduct.stock(dbProduct.stock() + quantity);
        return productRepository.save(dbProduct).to();
    }

    public void fallbackProductCreate(Product in, Throwable t) {
        throw new RuntimeException("Failed to create product", t);
    }

    public void fallbackProductRead(String id, Throwable t) {
        throw new RuntimeException("Failed to read product", t);
    }

    public void fallbackProductUpdate(String id, Product in, Throwable t) {
        throw new RuntimeException("Failed to update product", t);
    }
    public void fallbackProductDelete(String id, Throwable t) {
        throw new RuntimeException("Failed to delete product", t);
    }
    public void fallbackProductList(Double min_price, Double max_price, Throwable t) {
        throw new RuntimeException("Failed to list products", t);
    }
    public void fallbackProductConsume(String id, Integer quantity, Throwable t) {
        throw new RuntimeException("Failed to consume product", t);
    }
    public void fallbackProductReplenish(String id, Integer quantity, Throwable t) {
        throw new RuntimeException("Failed to replenish product", t);
    }
}
