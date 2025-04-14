package com.example.connect.api.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
public class Album extends Item {
    private String artist;
    private String etc;

    @Override
    public String getTitle() {
        return "제목:" + getName() + "가수:" + artist + "]";
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
