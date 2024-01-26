package ru.sberbank.reviewcar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.dao.impl.KlassStorageImpl;
import ru.sberbank.reviewcar.model.Klass;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class KlassService {

    private final KlassStorageImpl klassDbStorage;

    public Collection<Klass> getAllKlass() {
        return klassDbStorage.getAllKlass();
    }

    public Klass getKlassById(int id) {
        Klass klass = klassDbStorage.getKlassById(id);
        if (klass == null) {
            throw new NotFoundException("Klass с id=" + id + "не найден");
        }
        return klass;
    }
}
