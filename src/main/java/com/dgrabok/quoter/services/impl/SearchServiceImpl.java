package com.dgrabok.quoter.services.impl;

import com.dgrabok.quoter.services.api.QuotesService;
import com.dgrabok.quoter.services.api.SearchService;
import com.dgrabok.quoter.services.api.bo.QuoteBO;
import com.dgrabok.quoter.services.api.search.SearchAlgorithmEnum;
import com.dgrabok.quoter.services.api.search.SearchTransformationEnum;
import com.dgrabok.quoter.services.impl.search.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dgrabok.quoter.services.api.search.SearchAlgorithmEnum.RABIN_KARP;
import static com.dgrabok.quoter.services.api.search.SearchTransformationEnum.ALPHANUMERIC_ONLY;
import static com.dgrabok.quoter.services.api.search.SearchTransformationEnum.CASE_INSENSITIVE;

/**
 * Service for Quotes search.
 */
@Service
@Validated
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Value("${quotes-search.algorithm}")
    private SearchAlgorithmEnum searchAlgorithm;
    @Value("${quotes-search.transformations}")
    private List<SearchTransformationEnum> transformations;
    @Autowired
    private QuotesService service;

    private List<PatternTransformation> patternTransformations = Collections.emptyList();
    private List<SearchPattern> searchPatternPrototypes = Collections.emptyList();
    private int searchBufferSize = 0;

    @PostConstruct
    public void init() {
        log.debug("Service initialisation.");
        List<QuoteBO> quotes = service.fetchAll();
        searchPatternPrototypes = new ArrayList<>(quotes.size());
        log.debug("{} quotes will be available for search.", quotes.size());

        patternTransformations = new ArrayList<>();
        if (this.transformations.contains(ALPHANUMERIC_ONLY)) {
            log.debug("{} transformation enabled for search.", ALPHANUMERIC_ONLY);
            patternTransformations.add(new AlphanumericOnlyTransformation());
        }
        if (this.transformations.contains(CASE_INSENSITIVE)) {
            log.debug("{} transformation enabled for search.", CASE_INSENSITIVE);
            patternTransformations.add(new CaseInsensativeTransformation());
        }

        log.debug("{} algorithm will be used for search.", RABIN_KARP);

        log.debug("Patterns prototypes preparation started.");
        for (QuoteBO quote : quotes) {
            if (RABIN_KARP.equals(searchAlgorithm)) {
                SearchPattern pattern = new RabinKarpPattern(quote, patternTransformations);
                searchPatternPrototypes.add(pattern);

                searchBufferSize = Math.max(searchBufferSize, pattern.getPattern().length());
            } else {
                throw new AssertionError("Unsupported search algorithm \"" + searchAlgorithm + "\".");
            }
        }
        log.debug("Patterns prototypes preparation done.");
    }

    /**
     * Quotes search in text.
     *
     * @param text Text for analise.
     * @return Quotes represented in text.
     */
    @Override
    @SneakyThrows
    public List<QuoteBO> search(String text) {
        log.debug("Quotes search for provided text.");
        for (PatternTransformation transformation : patternTransformations) {
            text = transformation.transform(text);
        }

        List<SearchPattern> patterns = new ArrayList<>(searchPatternPrototypes.size());
        for (SearchPattern pattern : searchPatternPrototypes) {
            patterns.add(pattern.copy());
        }

        CircularFifoQueue<Character> queue = new CircularFifoQueue<>(searchBufferSize);
        for(char c : text.toCharArray()) {
            queue.add(c);
            patterns.parallelStream()
                    .filter(p -> !p.isMatched())
                    .forEach(p -> p.onNewChar(queue));
        }

        return patterns.parallelStream()
                .filter(SearchPattern::isMatched)
                .map(SearchPattern::getQuote)
                .collect(Collectors.toList());
    }
}
