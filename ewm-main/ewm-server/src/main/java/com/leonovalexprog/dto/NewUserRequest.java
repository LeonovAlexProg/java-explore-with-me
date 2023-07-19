package com.leonovalexprog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Email(message = "Incorrect email")
    @NotBlank(message = "Email must not be blank")
    private String email;
    @NotBlank(message = "Name must not be blank")
    private String name;
}
