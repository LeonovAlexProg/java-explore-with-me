package com.leonovalexprog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Incorrect email")
    @Size(min = 6, max = 254, message = "Email size is out of bounds")
    private String email;
    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 250, message = "Name size is out of bounds")
    private String name;
}
