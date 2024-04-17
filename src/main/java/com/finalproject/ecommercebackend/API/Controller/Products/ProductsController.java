package com.finalproject.ecommercebackend.API.Controller.Products;

import com.finalproject.ecommercebackend.Models.Product;
import com.finalproject.ecommercebackend.Service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductsController {
    private ProductService productService;

    public List<Product> getProducts(){
        return productService.getProducts();
    }
}
