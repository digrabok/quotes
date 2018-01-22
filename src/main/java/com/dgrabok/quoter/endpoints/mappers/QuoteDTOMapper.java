package com.dgrabok.quoter.endpoints.mappers;

import com.dgrabok.quoter.endpoints.dto.QuoteDTO;
import com.dgrabok.quoter.services.api.bo.QuoteBO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuoteDTOMapper {
    QuoteDTO toDTO(QuoteBO bo);
    List<QuoteDTO> toDTO(List<QuoteBO> bo);
}
