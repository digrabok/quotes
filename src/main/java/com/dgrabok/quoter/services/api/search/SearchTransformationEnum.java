package com.dgrabok.quoter.services.api.search;

/**
 * Search tet & patterns transformation rules.
 */
public enum SearchTransformationEnum {
    /**
     * Only alphabetical and numeral characters will be analysed.
     */
    ALPHANUMERIC_ONLY,
    /**
     * Analyse will be case insensitive.
     */
    CASE_INSENSITIVE
}
