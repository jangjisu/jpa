package com.book.jpa.domain.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class MessageTest {
    @DisplayName("저장된 메시지의 내용은 빈 값이 올 수 없다.")
    @Test
    void content() {

        // given
        Message message = createMessage("001", "1", "2");
        // when

        // then
    }

    private Message createMessage(String senderTelNo, String receiverTelNo, String content) {
        return Message.create(senderTelNo, receiverTelNo, content, LocalDateTime.now());
    }

}