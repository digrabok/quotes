package com.dgrabok.quoter.services;

import com.dgrabok.quoter.persistence.api.repositiries.QuotesRepository;
import com.dgrabok.quoter.services.api.QuotesService;
import com.dgrabok.quoter.services.api.bo.QuoteBO;
import com.dgrabok.quoter.services.impl.QuotesServiceImpl;
import com.dgrabok.quoter.services.impl.mappers.QuoteBOMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AopTestUtils;

import javax.validation.ConstraintViolationException;

import static com.dgrabok.quoter.TestsUtils.executionExceptionTest;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuotesServiceTests {
    @Autowired
    private QuotesService service;

    @Autowired
    private QuotesRepository repository;
    @Autowired
    private QuoteBOMapper mapper;

    /**
     * BEAN INITIALIZATION TESTS
     */

    @Test
    public void shouldBeUsedQuotesRepositoryImplClassAsImplementation() {
        Object instance = AopTestUtils.getUltimateTargetObject(service);
        Assert.assertTrue("Incorrect type used as implementation", instance instanceof QuotesServiceImpl);
    }

    /**
     * `fetchAll` METHOD TESTS
     */

    @Test
    public void shouldReturnAllQuotes() {
        assertEquals("should return all quotes", 5, service.fetchAll().size());
    }

    /**
     * `fetch` METHOD TESTS
     */

    @Test
    public void shouldThrowExceptionIfIdValueIsIncorrect() {
        executionExceptionTest(
                () -> service.fetch(0),
                ConstraintViolationException.class,
                null
        );

        executionExceptionTest(
                () -> service.fetch(-1),
                ConstraintViolationException.class,
                null
        );
    }

    @Test
    public void shouldReturnQuoteById() {
        QuoteBO quote = service.fetch(3);

        assertEquals("Incorrect quote was fetched", "A. P. J. Abdul Kalam", quote.getAuthor());
        assertEquals("Incorrect quote was fetched", "Great dreams of great dreamers are always transcended.", quote.getText());
    }
}
