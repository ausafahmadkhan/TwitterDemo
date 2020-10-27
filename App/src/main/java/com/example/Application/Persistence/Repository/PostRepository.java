package com.example.Application.Persistence.Repository;

import com.example.Application.Persistence.Models.PostDAO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<PostDAO, String> {

    @Query(value = "{'createdBy' : ?0}", fields = "{'_id' : 1}")
    List<PostDAO> findAllPostsByUserId(String userId, PageRequest pageRequest);

    @Query(value = "{'createdBy' : {$in : ?0}}")
    List<PostDAO> findAllPostsByUserIds(List<String> userIds);
}
