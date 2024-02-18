package ru.sberbank.reviewcar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.reviewcar.model.Klass;
import ru.sberbank.reviewcar.service.KlassService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/klass")
public class KlassController {

    private final KlassService klassService;

    /**
     *
     * @return - получение всех классов авто
     */
    @GetMapping
    private Collection<Klass> getAllKlass() {
        return klassService.getAllKlass();
    }

    /**
     *
     * @param id - id класса
     * @return - вывод класса по id
     */
    @GetMapping(value = "/{id}")
    private Klass getKlassById(@PathVariable int id) {
        return klassService.getKlassById(id);
    }
}
