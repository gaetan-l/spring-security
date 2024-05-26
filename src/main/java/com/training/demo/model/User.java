package com.training.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, AnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;

    private String  firstName;
    private String  lastName;

    @Column(unique = true)
    private String  email;
    private String  password;

    private Boolean enabled;
    private Boolean accountExpired;
    private Boolean accountLocked;
    private Boolean credentialsExpired;

    /**
     * 1 User peut écrire 0 à n Tutorial
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @ToString.Exclude // Comme il y a une ref User -> Tuto et Tuto -> User il y a une boucle infinie avec le toString()
    private List<Tutorial> tutorials;

    @ToString.Include
    private List<String> tutorialsShort() {
        return tutorials.stream()
                .map(tutorial ->
                        String.format(
                                "Tutorial(id=%d, title=%s)",
                                tutorial.getId(),
                                tutorial.getTitle()))
                .toList();
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}