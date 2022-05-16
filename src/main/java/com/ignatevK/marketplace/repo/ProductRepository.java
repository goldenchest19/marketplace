package com.ignatevK.marketplace.repo;

import com.ignatevK.marketplace.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
