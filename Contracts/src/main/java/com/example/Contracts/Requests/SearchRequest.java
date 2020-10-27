package com.example.Contracts.Requests;

import lombok.Data;

@Data
public class SearchRequest
{
    private String name;
    private String emailId;
    private String contactNumber;
}
