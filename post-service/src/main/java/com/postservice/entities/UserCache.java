package com.postservice.entities;

import com.core.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "user-cache")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
//@SQLRestriction("deleted = 0")
public class UserCache extends BaseEntity implements Serializable {

    @Id
    UUID id;

    String username;

    String firstName;

    String lastName;

    String address;

    String email;

    Boolean gender;

    String phoneNumber;

    Integer locked;

    Integer deleted;

}
