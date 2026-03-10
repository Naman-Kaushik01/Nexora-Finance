package com.Nexora.NexoraFinance.auth_users.entity;

import com.Nexora.NexoraFinance.role.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;
    private String phoneNumber;

    @Email
    @Column(unique = true ,nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    private String password;
    private String profilePictureURL;
    private boolean active = true;

    @ManyToAny(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles ;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<Account> accounts;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
