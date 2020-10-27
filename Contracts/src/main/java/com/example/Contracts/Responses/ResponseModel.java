package com.example.Contracts.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseModel<T>
{
    private T data;
    private boolean success;
}
