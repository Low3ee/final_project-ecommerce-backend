package com.finalproject.ecommercebackend.Models.Dao;

import com.finalproject.ecommercebackend.Models.LocalUser;
import com.finalproject.ecommercebackend.Models.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {


    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(LocalUser user);

}
