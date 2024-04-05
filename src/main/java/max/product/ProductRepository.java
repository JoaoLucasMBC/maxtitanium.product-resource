package max.product;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductModel, String> {

    public List<ProductModel> findByPriceLessThanEqual(Double price);

    public List<ProductModel> findByPriceGreaterThanEqual(Double price);

    public List<ProductModel> findByPriceBetween(Double min_price, Double max_price);
    
}
