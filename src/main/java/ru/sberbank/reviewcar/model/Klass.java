package ru.sberbank.reviewcar.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Klass {
    private Integer id;
    private String name;

    public Klass(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
