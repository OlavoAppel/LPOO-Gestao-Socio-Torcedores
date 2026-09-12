package com.ifsul.lpoo.sociotorcedor.core.model.converter;

import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.Categoria;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class CategoriaSocioConverter implements AttributeConverter<Categoria, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Categoria attribute) {
        return attribute.getCodigo();
    }

    @Override
    public Categoria convertToEntityAttribute(Integer dbData) {
        for(Categoria status : Categoria.values()){
            if(status.getCodigo().equals(dbData)) return status;
        }

        return null;
    }
}
