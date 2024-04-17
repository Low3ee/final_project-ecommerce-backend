package com.finalproject.ecommercebackend.Models.Dao;

import com.finalproject.ecommercebackend.Models.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long> {
}
