package com.dgrabok.quoter.endpoints;

import com.dgrabok.quoter.endpoints.dto.QuoteDTO;
import com.dgrabok.quoter.endpoints.mappers.QuoteDTOMapper;
import com.dgrabok.quoter.services.api.QuotesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Endpoint for work with Quotes.
 */
@RestController
@Validated
@Slf4j
@RequestMapping("/api/quotes")
public class QuotesEndpoint {
    private final QuotesService service;
    private final QuoteDTOMapper mapper;

    /**
     * Will fetch one quote by id.
     *
     * @param id quote id (1 based).
     * @return quote.
     */
    @GetMapping("/{id}")
    public QuoteDTO fetch(@PathVariable @Min(1) int id) {
        log.debug("Fetch quote by id={}", id);

        return mapper.toDTO(service.fetch(id));
    }

    /**
     * Fetch all quotes.
     *
     * @return all quotes.
     */
    @GetMapping
    public List<QuoteDTO> fetchAll() {
        log.debug("Fetch all quotes");

        return mapper.toDTO(service.fetchAll());
    }

    @Autowired
    public QuotesEndpoint(QuotesService service, QuoteDTOMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }
}
