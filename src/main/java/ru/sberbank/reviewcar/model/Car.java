package ru.sberbank.reviewcar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Car {

  private Integer id;
  private final String name;
  private final String description;
  private final LocalDate releaseDate;
  private final Integer price;
  private final Integer rate;//лайки от пользователей
  private Klass klass;
  private List<Type> types;
  @JsonIgnore
  private Set<Integer> likes;
  private List<Director> directors = new ArrayList<>();
}
