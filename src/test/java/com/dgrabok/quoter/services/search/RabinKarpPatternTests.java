package com.dgrabok.quoter.services.search;

import com.dgrabok.quoter.services.api.bo.QuoteBO;
import com.dgrabok.quoter.services.impl.search.RabinKarpPattern;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RabinKarpPatternTests {
    private final String patternText = "Great dreams of great dreamers are always transcended.";

    @Test
    public void test2() {
        String text = "AAAA bbbb CCCC" + patternText + "dddd EEEE ffff";
        RabinKarpPattern pattern = new RabinKarpPattern(new QuoteBO("Quote author", patternText), null);

        CircularFifoQueue<Character> queue = new CircularFifoQueue<>(patternText.length()+1);

        boolean success = false;
        int offset = -1;
        for(char c : text.toCharArray()) {
            queue.add(c);
            pattern.onNewChar(queue);
            if (pattern.isMatched()) {
                success = true;
                offset = pattern.getMatchedOffset();
            }
        }

        assertTrue(success);
        assertEquals("Pattern should be matched in the text",
                patternText, text.substring(offset, offset + patternText.length()));
    }
}
