package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private RoleName role;

    public Role() {
    }

    public Role(RoleName role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }

    public enum RoleName {
        USER,
        ADMIN
    }
}
