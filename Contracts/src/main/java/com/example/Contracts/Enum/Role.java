package com.example.Contracts.Enum;

public enum Role
{
    USER("USER"), ADMIN("ADMIN");

    String val;

    public String getVal() {
        return val;
    }

    Role(String val) {
        this.val = val;
    }
}
