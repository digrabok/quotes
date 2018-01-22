package com.dgrabok.quoter.persistence.api.entities;

import lombok.*;

/**
 * Quote entity for persistence layer
 */
@Getter
@AllArgsConstructor
public class Quote {
    /**
     * Quote author.
     */
    private String author;

    /**
     * Quote text.
     */
    private String text;
}
