package com.postservice.entities;

import com.core.entities.BaseEntity;
import com.postservice.enums.PrivacyEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("deleted = false")
public class PostLogs extends BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    String userId;

    String description;

    @Enumerated(EnumType.STRING)
    PrivacyEnum privacy;

    boolean deleted = false;

}
