package com.finalproject.ecommercebackend.Service;

import com.finalproject.ecommercebackend.Models.Dao.ProductDAO;
import com.finalproject.ecommercebackend.Models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getProducts(){
        return productDAO.findAll();
    }
}
