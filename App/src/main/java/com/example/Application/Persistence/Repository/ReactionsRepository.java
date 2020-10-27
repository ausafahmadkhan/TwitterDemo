package com.example.Application.Persistence.Repository;

import com.example.Application.Persistence.Models.PostReactionsDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReactionsRepository extends MongoRepository<PostReactionsDAO, String> {
}
