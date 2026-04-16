package com.green.eats.auth.application.model;

import lombok.Data;

@Data
public class UserUpdateReq {
    private String name;
    private String address;
}
