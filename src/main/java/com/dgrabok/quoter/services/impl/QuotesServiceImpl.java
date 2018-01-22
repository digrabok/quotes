package com.dgrabok.quoter.services.impl;

import com.dgrabok.quoter.persistence.api.repositiries.QuotesRepository;
import com.dgrabok.quoter.services.api.QuotesService;
import com.dgrabok.quoter.services.api.bo.QuoteBO;
import com.dgrabok.quoter.services.impl.mappers.QuoteBOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Service for work with Quotes.
 */
@Service
@Validated
@Slf4j
public class QuotesServiceImpl implements QuotesService {
    private final QuotesRepository repository;
    private final QuoteBOMapper mapper;

    /**
     * Will fetch one quote by id.
     *
     * @param id quote id (1 based).
     * @return quote.
     */
    @Override
    public QuoteBO fetch(@Min(1) int id) {
        log.debug("Fetch quote by id={}", id);

        return mapper.toBO(repository.fetch(id));
    }

    /**
     * Fetch all quotes.
     *
     * @return all quotes.
     */
    @Override
    public List<QuoteBO> fetchAll() {
        log.debug("Fetch all quotes");

        return mapper.toBO(repository.fetchAll());
    }

    @Autowired
    public QuotesServiceImpl(QuotesRepository repository, QuoteBOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
}
