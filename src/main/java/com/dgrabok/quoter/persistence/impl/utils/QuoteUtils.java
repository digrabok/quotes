package com.dgrabok.quoter.persistence.impl.utils;

import com.dgrabok.quoter.persistence.api.entities.Quote;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

/**
 * Utils class for work with quotes.
 */
@Component
@Validated
@Slf4j
public class QuoteUtils {
    private final String sourceFieldsDelimiter;

    /**
     * Will parse provided string and build {@link Quote} instance from it.
     *
     * @param sourceRow source row for parsing.
     * @return {@link Quote} instance built from parsed source row.
     */
    public Quote parseQuote(@NotEmpty String sourceRow) {
        validateQuoteSource(sourceRow);

        val delimiterIndex = sourceRow.indexOf(sourceFieldsDelimiter);
        val author = sourceRow.substring(0, delimiterIndex);
        val quote = sourceRow.substring(delimiterIndex+1);

        return new Quote(author, quote);
    }

    private void validateQuoteSource(String sourceRow) {
        String errorMessage = null;

        if (sourceRow.startsWith(sourceFieldsDelimiter)) {
            errorMessage = "Author in 'sourceRow' argument should not be blank.";
        } else if (sourceRow.endsWith(sourceFieldsDelimiter)) {
            errorMessage = "Quote text in 'sourceRow' argument should not be blank.";
        } else if (!sourceRow.contains(sourceFieldsDelimiter)) {
            errorMessage = "'sourceRow' argument have incorrect format.";
        }

        if (errorMessage != null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Autowired
    public QuoteUtils(@Value("${quotes-source.fields-delimiter}") String sourceFieldsDelimiter) {
        log.debug("Initialisation service for source fields delimiter \"{}\"", sourceFieldsDelimiter);

        if ("\\t".equals(sourceFieldsDelimiter)) {
            this.sourceFieldsDelimiter = "\t";
        } else {
            final String message = StringUtils.isEmpty(sourceFieldsDelimiter)
                    ? "Fields delimiter should not be blank or null."
                    : "Unsupported fields delimiter format.";
            throw new IllegalArgumentException(message);
        }
    }
}
