package com.dgrabok.quoter.services.impl.search;

import com.dgrabok.quoter.services.api.bo.QuoteBO;
import lombok.SneakyThrows;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

/**
 * Pattern for quote search
 */
public abstract class SearchPattern {
    private final QuoteBO quote;
    private final String pattern;

    /**
     * Should be invoked for each new char of incoming text stream.
     *
     * @param buffer circular buffer with analysed part of text. Buffer max size should not be
     *               lower than {@link #pattern} size.
     */
    public abstract void onNewChar(CircularFifoQueue<Character> buffer);

    /**
     * Identify when pattern matched in the text.
     *
     * @return true - matched, false - not
     */
    public abstract boolean isMatched();

    /**
     * Matched quote position in the text.
     *
     * @return -1 when quote not matched, offset from text begin if matched.
     */
    public abstract int getMatchedOffset();

    /**
     * @param quote quote which will be searched.
     * @param transformations transformations for quote text, exactly transformed text will be used as search pattern.
     */
    public SearchPattern(QuoteBO quote, List<PatternTransformation> transformations) {
        if (CollectionUtils.isEmpty(transformations)) {
            transformations = Collections.emptyList();
        }

        this.quote = quote;

        String patternProto = quote.getText();
        for (PatternTransformation transformation : transformations) {
            patternProto = transformation.transform(patternProto);
        }
        this.pattern = patternProto;
    }

    protected SearchPattern(SearchPattern prototype) {
        this.quote = prototype.quote;
        this.pattern = prototype.pattern;
    }

    /**
     * Will build new instance of {@link SearchPattern} with current instance as prototype.
     *
     * @return new instance built on top of current as prototype.
     */
    @SneakyThrows
    public SearchPattern copy() {
        Constructor<? extends SearchPattern> constructor = getClass().getConstructor(SearchPattern.class);
        return constructor.newInstance(this);
    }

    /**
     * Analysed quote.
     *
     * @return quote.
     */
    public QuoteBO getQuote() {
        return quote;
    }

    /**
     * Pattern used for analyse.
     *
     * @return pattern.
     */
    public String getPattern() {
        return pattern;
    }
}
