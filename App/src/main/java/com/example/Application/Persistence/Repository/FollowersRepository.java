package com.example.Application.Persistence.Repository;

import com.example.Application.Persistence.Models.FollowersDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FollowersRepository extends MongoRepository<FollowersDAO, String> {

    @Query(value = "{'followedBy' : ?0}", fields = "{'following' : 1}")
    List<FollowersDAO> getFollowings(String userId);
}
