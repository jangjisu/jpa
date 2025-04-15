package com.example.connect.api.domain.item;

public class PrintVisitor implements Visitor {
    @Override
    public void visit(Book book) {
        System.out.println("book.class = " + book.getClass());
    }

    @Override
    public void visit(Album album) {
        System.out.println("album.class = " + album.getClass());

    }

    @Override
    public void visit(Movie movie) {
        System.out.println("movie.class = " + movie.getClass());

    }
}
