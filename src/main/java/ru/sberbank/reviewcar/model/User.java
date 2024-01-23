package ru.sberbank.reviewcar.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {

    private Integer id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
}