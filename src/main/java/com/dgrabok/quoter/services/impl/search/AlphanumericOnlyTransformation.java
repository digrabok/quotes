package com.dgrabok.quoter.services.impl.search;

public class AlphanumericOnlyTransformation implements PatternTransformation {
    @Override
    public String transform(String patternText) {
        if (patternText == null) {
            throw new IllegalArgumentException("Argument 'patternText' should not be null.");
        }
        return patternText.replaceAll("[^0-9a-zA-Z]", "");
    }
}
