package com.dgrabok.quoter.persistence.api.repositiries;

import com.dgrabok.quoter.persistence.api.entities.Quote;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Quotes repository.
 */
public interface QuotesRepository {
    /**
     * Will fetch one quote by id.
     *
     * @param id quote id (1 based).
     * @return quote.
     */
    Quote fetch(@Min(1) int id);

    /**
     * Fetch all quotes.
     *
     * @return all quotes.
     */
    List<Quote> fetchAll();

    /**
     * Will return count of existed quotes.
     *
     * @return count of existed quotes.
     */
    int count();
}
