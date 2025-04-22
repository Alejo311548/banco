package com.ejemplo.banco.mapper;

import com.ejemplo.banco.dto.TransactionDTO;
import com.ejemplo.banco.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDTO toDTO(Transaction transaction);  // ← mapea de entidad a DTO
    Transaction toEntity(TransactionDTO dto);       // ← mapea de DTO a entidad
}
