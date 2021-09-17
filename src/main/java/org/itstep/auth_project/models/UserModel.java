package org.itstep.auth_project.models;

import lombok.Data;
import lombok.Getter;

@Data
public class UserModel {

    private String email;

    private String fullName;

    private String password;

    private String confirmPassword;
}
