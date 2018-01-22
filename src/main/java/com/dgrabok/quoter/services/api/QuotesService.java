package com.dgrabok.quoter.services.api;

import com.dgrabok.quoter.services.api.bo.QuoteBO;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Service for work with Quotes.
 */
public interface QuotesService {
    /**
     * Will fetch one quote by id.
     *
     * @param id quote id (1 based).
     * @return quote.
     */
    QuoteBO fetch(@Min(1) int id);

    /**
     * Fetch all quotes.
     *
     * @return all quotes.
     */
    List<QuoteBO> fetchAll();
}
