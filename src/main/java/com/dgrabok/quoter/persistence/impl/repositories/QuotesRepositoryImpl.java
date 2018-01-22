package com.dgrabok.quoter.persistence.impl.repositories;

import com.dgrabok.quoter.persistence.api.entities.Quote;
import com.dgrabok.quoter.persistence.api.repositiries.QuotesRepository;
import com.dgrabok.quoter.persistence.impl.utils.QuoteUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Min;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Quotes repository.
 */
@Repository
@Validated
@Slf4j
public class QuotesRepositoryImpl implements QuotesRepository {
    private final String quotesSourceFilePath;

    private final ResourceLoader resourceLoader;
    private final QuoteUtils utils;

    private final List<Quote> quotes = new ArrayList<>();

    /**
     * Will fetch one quote by id.
     *
     * @param id quote id (1 based).
     * @return quote.
     */
    @Override
    public Quote fetch(@Min(1) int id) {
        log.debug("Fetch quote by id={}", id);

        return quotes.get(id-1); // 1 based IDs
    }

    /**
     * Fetch all quotes.
     *
     * @return all quotes.
     */
    @Override
    public List<Quote> fetchAll() {
        log.debug("Fetch all quotes");

        return new ArrayList<>(quotes);
    }

    /**
     * Will return count of existed quotes.
     *
     * @return count of existed quotes.
     */
    @Override
    public int count() {
        log.debug("Calculate count of existed quotes");

        return quotes.size();
    }

    /**
     * Will load properties from source file during bean initialisation
     */
    @PostConstruct
    @SneakyThrows
    public void init() {
        val resource = resourceLoader.getResource(quotesSourceFilePath);

        if (resource == null) {
            throw new IllegalStateException("Source file \"" + quotesSourceFilePath + "\" could not be found");
        }

        try (val in = resource.getInputStream()) {
            IOUtils.readLines(in, StandardCharsets.UTF_8).stream()
                    .map(utils::parseQuote).forEach(quotes::add);
        }
    }

    @Autowired
    public QuotesRepositoryImpl(
            @Value("${quotes-source.file-path}") String sourceFilePath,
            ResourceLoader resourceLoader,
            QuoteUtils utils) {
        log.debug("Initialisation service for quotes source file \"{}\"", sourceFilePath);

        if (StringUtils.isEmpty(sourceFilePath)) {
            throw new IllegalArgumentException("Source file path should not be blank or null.");
        }

        quotesSourceFilePath = sourceFilePath;
        this.resourceLoader = resourceLoader;
        this.utils = utils;
    }
}
