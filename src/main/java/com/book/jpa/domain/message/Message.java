package com.book.jpa.domain.message;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String senderTelNo;
    private String receiverTelNo;
    private String content;
    private LocalDateTime sendDateTime;

    private Message(String senderTelNo, String receiverTelNo, String content, LocalDateTime sendDateTime) {
        this.senderTelNo = senderTelNo;
        this.receiverTelNo = receiverTelNo;
        this.content = content;
        this.sendDateTime = sendDateTime;
    }

    public static Message create(String senderTelNo, String receiverTelNo, String content, LocalDateTime sendDateTime) {
        return new Message(senderTelNo, receiverTelNo, content, sendDateTime);
    }
}
