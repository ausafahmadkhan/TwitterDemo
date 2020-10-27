package com.example.Application.Persistence.Repository;

import com.example.Application.Persistence.Models.UserDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDAO, String>
{
    @Query(value = "{$or : [{'name' : ?0 }, {'emailId' : ?1},{'contactNumber' : ?2}]}")
    Optional<UserDAO> findByUserDetails(String name, String emailId, String contactNumber);

    @Query(value = "{'emailId' : ?0}")
    Optional<UserDAO> findUser(String emailId);
}
