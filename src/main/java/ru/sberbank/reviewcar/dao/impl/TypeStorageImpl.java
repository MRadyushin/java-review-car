package ru.sberbank.reviewcar.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.model.Type;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class TypeStorageImpl {

    private final JdbcTemplate jdbcTemplate;

    public Collection<Type> getAllTypes() {
        String sqlGetAllTypes = "SELECT TYPE_NAME as typeName, TYPE_ID as typeId FROM types";
        List<Type> listTypes = jdbcTemplate.query(sqlGetAllTypes, CarStorageImpl::makeType);
        return listTypes;
    }

    public Type getTypeById(int id) {
        String sqlGetType = "SELECT TYPE_NAME as typeName, TYPE_ID as typeId FROM types WHERE type_id = ?";
        List<Type> typeById = jdbcTemplate.query(sqlGetType, CarStorageImpl::makeType, id);
        Type type = null;
        if (!typeById.isEmpty()) {
            type = typeById.get(0);
        }
        return type;
    }
}
