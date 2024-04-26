package com.finalproject.ecommercebackend.Service;

import com.finalproject.ecommercebackend.Exception.EmailFailureException;
import com.finalproject.ecommercebackend.Models.Dao.LocalUserDAO;
import com.finalproject.ecommercebackend.API.Model.LoginBody;
import com.finalproject.ecommercebackend.API.Model.RegistrationBody;
import com.finalproject.ecommercebackend.Exception.UserExistsException;
import com.finalproject.ecommercebackend.Exception.UserNotVerifiedException;
import com.finalproject.ecommercebackend.Models.Dao.VerificationTokenDAO;
import com.finalproject.ecommercebackend.Models.LocalUser;
import com.finalproject.ecommercebackend.Models.VerificationToken;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;
    private EmailService emailService;
    private VerificationTokenDAO verificationTokenDAO;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService,EmailService emailService, VerificationTokenDAO verificationTokenDAO) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenDAO = verificationTokenDAO;
    }


    public LocalUser registerUser( RegistrationBody registrationBody) throws UserExistsException, EmailFailureException {
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

        LocalUser savedUser = localUserDAO.save(user);
        System.out.println("user saved.");

        VerificationToken verificationToken = createVerificationToken(user);

        emailService.sendVerificationEmail(verificationToken);
        System.out.println("verification sent");

        verificationTokenDAO.save(verificationToken);
        System.out.println("token saved to db");

        return savedUser;
    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                if (user.isEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.size() == 0 ||
                            verificationTokens.get(0).getCreated_at().before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }

    /**
     * Verifies a user from the given token.
     * @param token The token to use to verify a user.
     * @return True if it was verified, false if already verified or token invalid.
     */
    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            LocalUser user = verificationToken.getUser();
            if (!user.isEmailVerified()) {
                user.setEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreated_at( new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

}
