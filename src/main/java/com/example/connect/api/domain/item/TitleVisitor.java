package com.example.connect.api.domain.item;

public class TitleVisitor implements Visitor {
    @Override
    public void visit(Book book) {
        System.out.println("book.title = " + book.getTitle());

    }

    @Override
    public void visit(Album album) {
        System.out.println("album.title = " + album.getTitle());

    }

    @Override
    public void visit(Movie movie) {
        System.out.println("movie.title = " + movie.getTitle());

    }
}
