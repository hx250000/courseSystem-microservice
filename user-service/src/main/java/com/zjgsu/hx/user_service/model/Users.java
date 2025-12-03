package com.zjgsu.hx.user_service.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.UUID;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "user")
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Data
public abstract class Users {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;
    private String username;
    private String email;
}




