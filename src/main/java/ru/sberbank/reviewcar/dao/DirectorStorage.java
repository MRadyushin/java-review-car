package ru.sberbank.reviewcar.dao;

import ru.sberbank.reviewcar.model.Director;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
  Collection<Director> findAll();

  Optional<Director> findById(Integer directorId);

  Director save(Director director);

  Director update(Director director);

  void remove(Integer directorId);

  List<Director> getDirectorsByCarId(Integer carId);

}
