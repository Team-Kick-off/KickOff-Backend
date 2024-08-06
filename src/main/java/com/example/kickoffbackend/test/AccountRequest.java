package com.example.kickoffbackend.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountRequest {
    private String email;
    private String name;
}
