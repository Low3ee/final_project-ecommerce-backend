package com.finalproject.ecommercebackend.Service;

import com.finalproject.ecommercebackend.Models.Dao.UserOrderDAO;
import com.finalproject.ecommercebackend.Models.LocalUser;
import com.finalproject.ecommercebackend.Models.UserOrders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private UserOrderDAO userOrderDAO;

    public OrderService(UserOrderDAO userOrderDAO) {
        this.userOrderDAO = userOrderDAO;
    }

    public List<UserOrders> getOrders(LocalUser user){
        return userOrderDAO.findByUser(user);

    }
}
