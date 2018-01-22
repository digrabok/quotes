package com.dgrabok.quoter.services.impl.search;

import com.dgrabok.quoter.services.api.bo.QuoteBO;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

/**
 * Pattern for quote search on top of Rabin Karp algorithm.
 *
 * Implemented on top of <a href="https://algs4.cs.princeton.edu/53substring/RabinKarp.java.html">external solution</a>.
 *
 */
@Slf4j
public class RabinKarpPattern extends SearchPattern {
    private final long patternHash;    // pattern hash value
    private final int patternLength;           // pattern length
    private final long q;          // a large prime, small enough to avoid long overflow
    private final int r;           // radix
    private final long rm;         // R^(M-1) % Q

    private long bufferHash = 0;
    private boolean matched = false;
    private int matchedOffset = -1;

    @Override
    public void onNewChar(CircularFifoQueue<Character> queue) {
        if (!matched) {
            if (queue.maxSize() < patternLength) {
                throw new IllegalArgumentException("Queue to small. Minimal acceptable size is " + patternLength);
            }
            if (queue.isEmpty()) {
                throw new IllegalArgumentException("Queue should not be empty");
            }

            matchedOffset++;

            int bufferSize = queue.size();
            if (bufferSize <= patternLength) {
                bufferHash = (r * bufferHash + queue.get(bufferSize - 1)) % q;
            } else {
                bufferHash = (bufferHash + q - rm * queue.get(bufferSize - patternLength - 1) % q) % q;
                bufferHash = (bufferHash * r + queue.get(bufferSize - 1)) % q;
            }

            if (bufferHash == patternHash && bufferSize >= patternLength) {
                check(queue);
            }
        }
    }

    @Override
    public boolean isMatched() {
        return matched;
    }

    @Override
    public int getMatchedOffset() {
        return matched ? matchedOffset : -1;
    }

    private RabinKarpPattern(@NonNull RabinKarpPattern prototype) {
        super(prototype);

        this.patternHash = prototype.patternHash;
        this.patternLength = prototype.patternLength;
        this.q = prototype.q;
        this.r = prototype.r;
        this.rm = prototype.rm;

        this.bufferHash = 0;
        this.matched = false;
        this.matchedOffset = -1;
    }

    @SneakyThrows
    @Override
    public RabinKarpPattern copy() {
        return new RabinKarpPattern(this);
    }

    /**
     * @param quote quote which will be searched.
     * @param transformations transformations for quote text, exactly transformed text will be used as search pattern.
     */
    public RabinKarpPattern(QuoteBO quote, List<PatternTransformation> transformations) {
        super(quote, transformations);

        this.r = 256;
        this.patternLength = getPattern().length();
        this.q = longRandomPrime();

        // precompute R^(m-1) % q for use in removing leading digit
        long rmProto = 1;
        for (int i = 1; i <= patternLength-1; i++)
            rmProto = (r * rmProto) % q;
        this.rm = rmProto;

        this.patternHash = hash(getPattern(), patternLength);
    }

    // Compute hash for key[0..m-1].
    private long hash(String key, int m) {
        long h = 0;
        for (int j = 0; j < m; j++)
            h = (r * h + key.charAt(j)) % q;
        return h;
    }

    private void check(CircularFifoQueue<Character> queue) {
        String pattern = getPattern();
        int queueSize = queue.size();

        for(int offset = patternLength; offset > 0; offset--) {
            Character queueChar = queue.get(queueSize - offset);
            char patternChar = pattern.charAt(patternLength - offset);

            if (Character.compare(queueChar, patternChar) != 0) {
                return;
            }
        }

        matched = true;
        matchedOffset = matchedOffset-(patternLength-1);
    }

    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }
}
