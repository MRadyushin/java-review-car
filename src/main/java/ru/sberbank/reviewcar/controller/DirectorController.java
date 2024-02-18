package ru.sberbank.reviewcar.controller;

import java.util.Collection;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.reviewcar.model.Director;
import ru.sberbank.reviewcar.service.DirectorService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

  private final DirectorService directorService;

  /**
   *
   * @return - возвращает всех производителей
   */
  @GetMapping
  public Collection<Director> allDirectors() {
    return directorService.getAll();
  }

  /**
   *
   * @param directorId - id пользователя
   * @return возвращает производителя по id
   */
  @GetMapping("/{id}")
  public Director directorById(@PathVariable("id") Integer directorId) {
    return directorService.getById(directorId);
  }

  /**
   *
   *Создание производителя
   */
  @PostMapping
  public Director createDirector(@Valid @RequestBody Director director) {
    return directorService.create(director);
  }

  /**
   *
   * изменение производителя
   */
  @PutMapping
  public Director updateDirector(@Valid @RequestBody Director director) {
    return directorService.update(director);
  }

  /**
   *
   * удавление поизводителя
   */
  @DeleteMapping("/{id}")
  public void deleteDirector(@PathVariable("id") Integer directorId) {
    directorService.delete(directorId);
  }
}
