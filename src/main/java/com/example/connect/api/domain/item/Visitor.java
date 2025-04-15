package com.example.connect.api.domain.item;

public interface Visitor {
    void visit(Book book);
    void visit(Album album);
    void visit(Movie movie);
}
