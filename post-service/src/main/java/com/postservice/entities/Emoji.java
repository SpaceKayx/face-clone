package com.postservice.entities;

import com.core.entities.BaseEntity;
import com.postservice.enums.EmojiEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "emoji")
@Builder
public class Emoji extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    UUID postId;

    UUID userId;

    @Enumerated(EnumType.STRING)
    EmojiEnum emojiType;

}
