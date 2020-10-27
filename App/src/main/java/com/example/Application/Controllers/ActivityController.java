package com.example.Application.Controllers;

import com.example.Application.Persistence.Models.PostDAO;
import com.example.Application.Service.ActivityService;
import com.example.Application.Service.UserService;
import com.example.Contracts.Requests.PostRequest;
import com.example.Contracts.Requests.SearchRequest;
import com.example.Contracts.Responses.PostResponse;
import com.example.Contracts.Responses.ResponseModel;
import com.example.Contracts.Responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController
{
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/searchUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<UserResponse>> searchUser(@RequestBody SearchRequest searchRequest)
    {
        System.out.println("method : searchUser, userRequest : " + searchRequest);
        try
        {
            UserResponse userResponse = userService.fetchUser(searchRequest);
            return ResponseEntity.ok(new ResponseModel<>(userResponse, true));
        }
        catch (Exception e)
        {
            System.out.println("Couldn't search user due to : " + e.getMessage());
            return new ResponseEntity<>(new ResponseModel<>(null, false), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @RequestMapping(value = "/createPost", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<PostResponse>> createPost(@Valid @RequestBody PostRequest postRequest)
    {
        try
        {
            activityService.createPost(postRequest);
            return ResponseEntity.ok(new ResponseModel<>(new PostResponse("Post created successfully"), true));
        }
        catch (Exception e)
        {
            System.out.println("Couldn't create post due to : " + e.getMessage());
            return new ResponseEntity<>(new ResponseModel<>(new PostResponse(e.getMessage()), false), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @RequestMapping(value = "/followUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<PostResponse>> followUser(@RequestParam("followingUserId") String followingUserId)
    {
        try
        {
            activityService.followUser(followingUserId);
            return ResponseEntity.ok(new ResponseModel<>(new PostResponse("Followed user successfully"), true));
        }
        catch (Exception e)
        {
            System.out.println("Couldn't follow due to : " + e.getMessage());
            return new ResponseEntity<>(new ResponseModel<>(new PostResponse(e.getMessage()), false), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @RequestMapping(value = "/likePost", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<PostResponse>> likePost(@RequestParam("postId") String postId)
    {
        try
        {
            activityService.likePost(postId);
            return ResponseEntity.ok(new ResponseModel<>(new PostResponse("Liked post successfully"), true));
        }
        catch (Exception e)
        {
            System.out.println("Couldn't like post due to : " + e.getMessage());
            return new ResponseEntity<>(new ResponseModel<>(new PostResponse(e.getMessage()), false), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @RequestMapping(value = "/likeAllPosts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<PostResponse>> likeAllPosts(@RequestParam("userId") String userId)
    {
        try
        {
            activityService.likeAllPosts(userId);
            return ResponseEntity.ok(new ResponseModel<>(new PostResponse("Process initiated successfully"), true));
        }
        catch (Exception e)
        {
            System.out.println("Couldn't initiated due to : " + e.getMessage());
            return new ResponseEntity<>(new ResponseModel<>(new PostResponse(e.getMessage()), false), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @RequestMapping(value = "/generateFeed", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<List<PostDAO>>> getFeed(@RequestParam("userId") String userId)
    {
        try
        {
            List<PostDAO> feed = activityService.getFeed(userId);
            return ResponseEntity.ok(new ResponseModel<>(feed, true));
        }
        catch (Exception e)
        {
            System.out.println("Couldn't fetch feed due to : " + e.getMessage());
            return new ResponseEntity<>(new ResponseModel<>(null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
