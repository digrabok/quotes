package com.dgrabok.quoter.services.api.bo;

import lombok.*;

/**
 * Quote business object for service layer
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuoteBO {
    /**
     * Quote author.
     */
    private String author;

    /**
     * Quote text.
     */
    private String text;
}
