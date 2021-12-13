package com.telerik.peer.models.dto;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UserDto {

    @NotBlank
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters.")
    private String username;

    @Email(message = "This is not a valid mail.Please try again.")
    private String email;

    @NotBlank
    @Size(min=10, max=10, message = "Number should contain 10 digits.")
    private String number;


    public UserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
