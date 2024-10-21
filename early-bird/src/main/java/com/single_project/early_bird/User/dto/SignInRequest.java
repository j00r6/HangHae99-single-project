package com.single_project.early_bird.User.dto;

import com.single_project.early_bird.User.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;

    public User SignInRequestToEntity(String encryptPassword) {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .phone(this.phone)
                .address(this.address)
                .build();
    }
}
