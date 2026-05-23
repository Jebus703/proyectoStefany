package com.saas.workspaces.dto.response;

import com.saas.workspaces.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName()
        );
    }
}
