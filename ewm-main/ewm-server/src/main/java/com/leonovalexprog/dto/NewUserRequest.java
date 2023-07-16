package com.leonovalexprog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Email(message = "Incorrect email")
    @NotBlank(message = "Email must not be blank")
    @Max(value = 254, message = "Email must be shorter then 254")
    @Min(value = 6, message = "Email must be greater then 6")
    private String email;
    @NotBlank(message = "Name must not be blank")
    private String name;
}
