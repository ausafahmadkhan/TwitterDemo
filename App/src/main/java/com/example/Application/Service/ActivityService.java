package com.example.Application.Service;

import com.example.Application.Persistence.Models.PostDAO;
import com.example.Contracts.Requests.PostRequest;

import java.util.List;

public interface ActivityService
{
    void createPost(PostRequest postRequest) throws Exception;

    void followUser(String userId) throws Exception;

    void likePost(String postId) throws Exception;

    void likeAllPosts(String userId) throws Exception;

    List<PostDAO> getFeed(String userId);
}
