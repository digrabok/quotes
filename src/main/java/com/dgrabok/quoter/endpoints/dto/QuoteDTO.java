package com.dgrabok.quoter.endpoints.dto;

import lombok.*;

/**
 * Quote representation for REST API layer
 */
@Getter
@Setter
@NoArgsConstructor
public class QuoteDTO {
    /**
     * Quote author.
     */
    private String author;

    /**
     * Quote text.
     */
    private String text;
}
