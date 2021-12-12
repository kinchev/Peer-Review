package com.telerik.peer.models.dto;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterDto extends LoginDto {

    @NotBlank(message = "Confirm password can`t be empty")
    private String passwordConfirm;

    @NotBlank
//    @Email(message = "This is not a valid mail.Please try again.")
    private String email;

    @NotBlank
    @Size(min=10, max=10, message = "Number should contain 10 digits.")
    @Column(name = "phone_number")
    private String number;

    public RegisterDto() {
    }


    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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
