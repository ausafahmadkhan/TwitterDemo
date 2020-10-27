package com.example.Contracts.Requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PostRequest
{
    @NotNull
    @Size(min = 10, max = 140)
    private String text;

}
