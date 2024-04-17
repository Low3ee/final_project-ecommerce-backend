package com.finalproject.ecommercebackend.API.Controller.Auth;

import com.finalproject.ecommercebackend.API.Model.LoginBody;
import com.finalproject.ecommercebackend.API.Model.LoginResponse;
import com.finalproject.ecommercebackend.API.Model.RegistrationBody;
import com.finalproject.ecommercebackend.Exception.UserExistsException;
import com.finalproject.ecommercebackend.Models.LocalUser;
import com.finalproject.ecommercebackend.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
            try {
                userService.registerUser(registrationBody);
                return ResponseEntity.ok().build();
            } catch (UserExistsException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();

        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody){
        String jwt = userService.loginUser(loginBody);
        if(jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user){
        return user;
    }
}
