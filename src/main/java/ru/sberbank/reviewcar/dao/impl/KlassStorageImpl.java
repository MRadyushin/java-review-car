package ru.sberbank.reviewcar.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.model.Klass;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class KlassStorageImpl {

    private final JdbcTemplate jdbcTemplate;

    public Collection<Klass> getAllKlass() {
        String sqlGetAllKlass = "SELECT * FROM klass";
        List<Klass> listKlass = jdbcTemplate.query(sqlGetAllKlass, CarStorageImpl::makeKlass);
        return listKlass;
    }

    public Klass getKlassById(int id) {
        String sqlGetKlass = "SELECT * FROM klass WHERE klass_id = ?";
        List<Klass> klassById = jdbcTemplate.query(sqlGetKlass, CarStorageImpl::makeKlass, id);
        Klass klass = null;
        if (!klassById.isEmpty()) {
            klass = klassById.get(0);
        }
        return klass;
    }
}
