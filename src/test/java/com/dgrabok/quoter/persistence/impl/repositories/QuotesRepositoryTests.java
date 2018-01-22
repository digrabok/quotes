package com.dgrabok.quoter.persistence.impl.repositories;

import com.dgrabok.quoter.persistence.api.entities.Quote;
import com.dgrabok.quoter.persistence.api.repositiries.QuotesRepository;
import com.dgrabok.quoter.persistence.impl.utils.QuoteUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static com.dgrabok.quoter.TestsUtils.executionExceptionTest;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuotesRepositoryTests {
    @Autowired
    private QuotesRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private QuoteUtils quoteUtils;

    @Value("${quotes-source.file-path}")
    private String sourceFilePath;

    @Mock
    private ResourceLoader resourceLoaderMock;
    @Mock
    private QuoteUtils quoteUtilsMock;

    @Before
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * BEAN INITIALIZATION TESTS
     */

    @Test
    public void shouldBeUsedQuotesRepositoryImplClassAsImplementation() {
        Object repositoryInstance = AopTestUtils.getUltimateTargetObject(repository);
        assertTrue("Incorrect type used as implementation", repositoryInstance instanceof QuotesRepositoryImpl);
    }

    @Test
    public void shouldLoadPredefinedListOfQuotes() {
        QuotesRepositoryImpl repository = new QuotesRepositoryImpl(sourceFilePath, resourceLoader, quoteUtils);

        assertEquals("Quotes list should be empty before repository initialization", 0, repository.count());
        repository.init();
        assertEquals("Quotes should be loaded during repository initialization", 5, repository.count());
    }

    @Test
    public void shouldThrowExceptionIfSourceFilePathNotProvided() {
        executionExceptionTest(
                () -> new QuotesRepositoryImpl(null, resourceLoaderMock, quoteUtilsMock),
                IllegalArgumentException.class,
                "Source file path should not be blank or null."
        );
    }

    @Test
    public void shouldThrowExceptionIfSourceFilePathIsEmpty() {
        executionExceptionTest(
                () -> new QuotesRepositoryImpl("", resourceLoaderMock, quoteUtilsMock),
                IllegalArgumentException.class,
                "Source file path should not be blank or null."
        );
    }

    @Test
    public void shouldThrowExceptionIfSourceFIleCouldNotBeFound() {
        executionExceptionTest(
                () -> {
                    QuotesRepositoryImpl repository = new QuotesRepositoryImpl("q.txt", resourceLoaderMock, quoteUtilsMock);
                    repository.init();
                },
                IllegalStateException.class,
                "Source file \"q.txt\" could not be found"
        );
    }

    @Test
    public void couldThrowExceptionDuringFIleReading() throws IOException {
        Resource resource = mock(Resource.class);
        Mockito.doThrow(new IOException("Exception error message")).when(resource).getInputStream();

        when(resourceLoaderMock.getResource(anyString())).thenReturn(resource);

        executionExceptionTest(
                () -> {
                    QuotesRepositoryImpl repository = new QuotesRepositoryImpl("q.txt", resourceLoaderMock, quoteUtilsMock);
                    repository.init();
                },
                IOException.class,
                "Exception error message"
        );
    }

    /**
     * `count` METHOD TESTS
     */

    @Test
    public void shouldReturnQuotesCount() {
        assertEquals("should return quotes count", 5, repository.count());
    }

    /**
     * `fetchAll` METHOD TESTS
     */

    @Test
    public void shouldReturnAllQuotes() {
        assertEquals("should return all quotes", 5, repository.fetchAll().size());
    }

    @Test
    public void returnedListShouldBeDifferentFromSourceList() {
        QuotesRepositoryImpl repositoryInstance = AopTestUtils.getUltimateTargetObject(repository);
        Object quotes = ReflectionTestUtils.getField(repositoryInstance, "quotes");

        assertFalse("returned list should be different from source list", quotes == repository.fetchAll());
    }

    /**
     * `fetch` METHOD TESTS
     */

    @Test
    public void shouldThrowExceptionIfIdValueIsIncorrect() {
        executionExceptionTest(
                () -> repository.fetch(0),
                ConstraintViolationException.class,
                null
        );

        executionExceptionTest(
                () -> repository.fetch(-1),
                ConstraintViolationException.class,
                null
        );
    }

    @Test
    public void shouldReturnQuoteById() {
        Quote quote = repository.fetch(3);

        assertEquals("Incorrect quote was fetched", "A. P. J. Abdul Kalam", quote.getAuthor());
        assertEquals("Incorrect quote was fetched", "Great dreams of great dreamers are always transcended.", quote.getText());
    }
}
