package com.finalproject.ecommercebackend.Service;

import com.finalproject.ecommercebackend.Models.Dao.LocalUserDAO;
import com.finalproject.ecommercebackend.API.Model.LoginBody;
import com.finalproject.ecommercebackend.API.Model.RegistrationBody;
import com.finalproject.ecommercebackend.Exception.UserExistsException;
import com.finalproject.ecommercebackend.Models.LocalUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;


    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }


    public LocalUser registerUser( RegistrationBody registrationBody) throws UserExistsException {
      if(localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
              || localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
           throw new UserExistsException();
        }
        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstname(registrationBody.getFirstName());
        user.setLastname(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        return localUserDAO.save(user);
    }

    public String loginUser(LoginBody loginBody){
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if(opUser.isPresent()) {
            LocalUser user = opUser.get();
            if(encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){
                return jwtService.generateJWT(user);
            };
        }
        return null;
    }
}
