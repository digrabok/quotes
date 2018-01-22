package com.dgrabok.quoter.services.api;

import com.dgrabok.quoter.services.api.bo.QuoteBO;

import java.util.List;

/**
 * Service for Quotes search.
 */
public interface SearchService {
    /**
     * Quotes search in text.
     *
     * @param text Text for analise.
     * @return Quotes represented in text.
     */
    List<QuoteBO> search(String text);
}
