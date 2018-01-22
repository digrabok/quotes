package com.dgrabok.quoter.persistence.impl.utils;

import com.dgrabok.quoter.persistence.api.entities.Quote;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static com.dgrabok.quoter.TestsUtils.executionExceptionTest;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuoteUtilsTests {
    @Autowired
    private QuoteUtils utils;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * BEAN INITIALIZATION TESTS
     */

    @Test
    public void shouldRaiseExceptionIfDelimiterIsNotProvidedDuringInitialization() {
        executionExceptionTest(
                () -> new QuoteUtils(null),
                IllegalArgumentException.class,
                "Fields delimiter should not be blank or null."
        );
    }

    @Test
    public void shouldRaiseExceptionIfDelimiterIsEmptyDuringInitialization() {
        executionExceptionTest(
                () -> new QuoteUtils(""),
                IllegalArgumentException.class,
                "Fields delimiter should not be blank or null."
        );
    }

    @Test
    public void shouldRaiseExceptionIfDelimiterNotAllowedDuringInitialization() {
        executionExceptionTest(
                () -> new QuoteUtils("+"),
                IllegalArgumentException.class,
                "Unsupported fields delimiter format."
        );
    }

    /**
     * `parseQuote` METHOD TESTS
     */

    @Test
    public void shouldBuildQuoteInstanceFromSourceString() {
        Quote quote = utils.parseQuote("A. A. Milne\tTiggers don't like honey.");

        assertEquals("Author parsed incorrectly", "A. A. Milne", quote.getAuthor());
        assertEquals("Quote text parsed incorrectly", "Tiggers don't like honey.", quote.getText());
    }

    @Test
    public void shouldRaiseExceptionDuringQuoteBuildIfSourceNotProvided() {
        executionExceptionTest(
                () -> utils.parseQuote(null),
                ConstraintViolationException.class,
                null
        );
    }

    @Test
    public void shouldRaiseExceptionDuringQuoteBuildInCaseOfEmptySource() {
        executionExceptionTest(
                () -> utils.parseQuote(""),
                ConstraintViolationException.class,
                null
        );
    }

    @Test
    public void shouldRaiseExceptionDuringQuoteBuildIfDelimiterOmittedInTheSource() {
        executionExceptionTest(
                () -> utils.parseQuote("A. A. Milne Tiggers don't like honey."),
                IllegalArgumentException.class,
                "'sourceRow' argument have incorrect format."
        );
    }

    @Test
    public void shouldRaiseExceptionDuringQuoteBuildIfAuthorOmittedInTheSource() {
        executionExceptionTest(
                () -> utils.parseQuote("\tTiggers don't like honey."),
                IllegalArgumentException.class,
                "Author in 'sourceRow' argument should not be blank."
        );
    }

    @Test
    public void shouldRaiseExceptionDuringQuoteBuildIfQuoteTextOmittedInTheSource() {
        executionExceptionTest(
                () -> utils.parseQuote("A. A. Milne\t"),
                IllegalArgumentException.class,
                "Quote text in 'sourceRow' argument should not be blank."
        );
    }
}
