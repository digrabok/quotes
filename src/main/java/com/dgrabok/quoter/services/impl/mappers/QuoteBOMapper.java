package com.dgrabok.quoter.services.impl.mappers;

import com.dgrabok.quoter.persistence.api.entities.Quote;
import com.dgrabok.quoter.services.api.bo.QuoteBO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuoteBOMapper {
    QuoteBO toBO(Quote entity);
    List<QuoteBO> toBO(List<Quote> entities);
}
