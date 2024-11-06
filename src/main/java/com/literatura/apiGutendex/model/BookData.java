package com.literatura.apiGutendex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
        String title,
        List<AuthorData> authors,
        List<String> languages,
        Integer download_count
) {
}
