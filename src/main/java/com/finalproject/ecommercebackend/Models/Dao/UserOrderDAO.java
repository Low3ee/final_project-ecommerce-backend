package com.finalproject.ecommercebackend.Models.Dao;

import com.finalproject.ecommercebackend.Models.LocalUser;
import com.finalproject.ecommercebackend.Models.UserOrders;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface UserOrderDAO extends ListCrudRepository<UserOrders, Long> {

    List<UserOrders> findByUser(LocalUser user);
}
