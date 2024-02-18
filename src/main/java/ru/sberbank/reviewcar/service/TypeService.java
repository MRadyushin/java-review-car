package ru.sberbank.reviewcar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.dao.impl.TypeStorageImpl;
import ru.sberbank.reviewcar.model.Type;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class TypeService {

    private final TypeStorageImpl typeDbStorage;

    public Collection<Type> getAllTypes() {
        return typeDbStorage.getAllTypes();
    }

    public Type getTypeById(int id) {
        Type type = typeDbStorage.getTypeById(id);
        if (type == null) {
            throw new NotFoundException("Type с id=" + id + "не найден");
        }
        return type;
    }
}
