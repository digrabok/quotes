package com.dgrabok.quoter.endpoints;

import com.dgrabok.quoter.endpoints.dto.QuoteDTO;
import com.dgrabok.quoter.endpoints.mappers.QuoteDTOMapper;
import com.dgrabok.quoter.services.api.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint for Quotes search.
 */
@RestController
@Validated
@Slf4j
@RequestMapping("/api/search")
public class SearchEndpoint {
    @Autowired
    private SearchService service;
    @Autowired
    private QuoteDTOMapper mapper;

    /**
     * Quotes search in text.
     *
     * @param text Text for analise.
     * @return Quotes represented in text.
     */
    @GetMapping
    public List<QuoteDTO> quotesSearch(@RequestParam String text) {
        log.debug("Quotes search for provided text.");

        return mapper.toDTO(service.search(text));
    }
}
