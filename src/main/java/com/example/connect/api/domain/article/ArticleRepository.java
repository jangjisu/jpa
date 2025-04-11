package com.example.connect.api.domain.article;

import com.example.connect.api.domain.CreateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, CreateRepository<Article> {
}
