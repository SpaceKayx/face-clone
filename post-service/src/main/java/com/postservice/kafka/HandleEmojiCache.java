package com.postservice.kafka;

import com.core.constants.FConstants;
import com.core.kafka.BaseKafkaHandler;
import com.postservice.entities.Emoji;
import com.postservice.repositories.EmojiRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HandleEmojiCache extends BaseKafkaHandler<Emoji> {

    EmojiRepository emojiRepository;

    @KafkaListener(topics = FConstants.TOPIC_EMOJI_CACHE,
            groupId = FConstants.GROUP_ID_DEFAULT)
    public void listen(String rawMessage) {
        handleMessage(rawMessage, FConstants.TOPIC_EMOJI_CACHE, Emoji.class);
    }

    @Override
    protected void processMessage(Emoji message) {
        emojiRepository.findByUserIdAndPostId(message.getUserId(), message.getPostId())
                .map(existingEmoji -> {
                    existingEmoji.setEmojiType(message.getEmojiType());
                    return emojiRepository.save(existingEmoji);
                })
                .orElseGet(() -> this.createEmoji(message));
    }

    private Emoji createEmoji(Emoji request) {
        return emojiRepository.save(request);
    }

}
