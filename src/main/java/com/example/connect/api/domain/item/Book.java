package com.example.connect.api.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("B")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {
    private String author;
    private String isbn;

    protected Book(String name, int price, int stockQuantity, String author, String isbn) {
        super(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    public static Book create(String name, int price, int quantity, String author, String isbn) {
        return new Book(name, price, quantity, author, isbn);
    }

    @Override
    public String getTitle() {
        return "[제목:" + getName() + "저자:" + author;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
