package com.example.Application.Service;

import com.example.Application.Persistence.Models.FollowersDAO;
import com.example.Application.Persistence.Models.PostDAO;
import com.example.Application.Persistence.Models.PostReactionsDAO;
import com.example.Application.Persistence.Repository.FollowersRepository;
import com.example.Application.Persistence.Repository.PostRepository;
import com.example.Application.Persistence.Repository.ReactionsRepository;
import com.example.Contracts.Requests.PostRequest;
import com.example.Contracts.Requests.SearchRequest;
import com.example.Contracts.Responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService
{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowersRepository followersRepository;

    @Autowired
    private ReactionsRepository reactionsRepository;

    @Override
    public void createPost(PostRequest postRequest) throws Exception {

        //Generally we keep user data cached so that we don't hit db for such calls.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String username = (String)usernamePasswordAuthenticationToken.getPrincipal();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setName(username);
        UserResponse userResponse = userService.fetchUser(searchRequest);

        PostDAO postDAO = PostDAO.builder()
                                 .createdBy(userResponse.getId())
                                 .creationTime(System.currentTimeMillis())
                                 .text(postRequest.getText())
                                 .build();
        postRepository.save(postDAO);
        System.out.println("New post created " + postDAO);
    }

    @Override
    public void followUser(String userId) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String username = (String)usernamePasswordAuthenticationToken.getPrincipal();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setName(username);
        UserResponse userResponse = userService.fetchUser(searchRequest);

        FollowersDAO followersDAO = FollowersDAO.builder()
                                                .followedBy(userId)
                                                .following(userResponse.getId())
                                                .followedAt(System.currentTimeMillis())
                                                .build();

        followersRepository.save(followersDAO);
    }

    @Override
    public void likePost(String postId) throws Exception {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String username = (String)usernamePasswordAuthenticationToken.getPrincipal();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setName(username);
        UserResponse userResponse = userService.fetchUser(searchRequest);

        PostReactionsDAO postReactionsDAO = PostReactionsDAO.builder()
                                                            .postId(postId)
                                                            .likedBy(userResponse.getId())
                                                            .likedAt(System.currentTimeMillis())
                                                            .build();
        reactionsRepository.save(postReactionsDAO);
    }

    @Override
    public void likeAllPosts(String userId) throws Exception
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String username = (String)usernamePasswordAuthenticationToken.getPrincipal();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setName(username);
        UserResponse userResponse = userService.fetchUser(searchRequest);

        likeBulkPosts(userResponse.getId(), userId);

    }

    @Async
    private void likeBulkPosts(String callingUserId, String userId)
    {
        int skip = 0, limit = 50;
        PageRequest pageRequest = new PageRequest(skip, limit);
        List<PostDAO> postDAOS = postRepository.findAllPostsByUserId(userId, pageRequest);

        while (postDAOS != null && !postDAOS.isEmpty())
        {
            List<PostReactionsDAO> reactionsDAOS = new ArrayList<>();
            for (PostDAO post: postDAOS) {

                PostReactionsDAO postReactionsDAO = PostReactionsDAO.builder()
                                                                    .postId(post.get_id())
                                                                    .likedBy(callingUserId)
                                                                    .likedAt(System.currentTimeMillis())
                                                                    .build();

                reactionsDAOS.add(postReactionsDAO);
            }
            reactionsRepository.saveAll(reactionsDAOS);
            skip++;
            pageRequest = new PageRequest(skip, limit);
            postDAOS = postRepository.findAllPostsByUserId(userId, pageRequest);
        }
    }

    @Override
    public List<PostDAO> getFeed(String userId) {
        List<FollowersDAO> following = followersRepository.getFollowings(userId);
        List<PostDAO> postDAOS = new ArrayList<>();
        if (following!= null && !following.isEmpty())
        {
            List<String> followings = following.stream()
                    .map(FollowersDAO::getFollowing)
                    .collect(Collectors.toList());
            postDAOS = postRepository.findAllPostsByUserIds(followings);
        }
        return postDAOS;
    }
}
