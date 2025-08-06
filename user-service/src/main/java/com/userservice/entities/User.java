package com.userservice.entities;

import com.userservice.enums.DeletedEnum;
import com.userservice.enums.LockedEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User implements Serializable {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    UUID id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(columnDefinition = "TEXT", nullable = false)
    String password;

    @Column(columnDefinition = "TEXT", nullable = false)
    String salt;

    @Column( nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    String address;

    @Column( nullable = false, unique = true)
    String email;

    Boolean gender;

    @Column(unique = true)
    String phoneNumber;

    Integer locked = LockedEnum.NOT_LOCKED.getValue();

    Integer deleted = DeletedEnum.NOT_DELETED.getValue();

}