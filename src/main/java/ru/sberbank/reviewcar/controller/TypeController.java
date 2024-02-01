package ru.sberbank.reviewcar.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.reviewcar.model.Type;
import ru.sberbank.reviewcar.service.TypeService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(value = "/types")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;


    @GetMapping
    private Collection<Type> getAllTypes() {


            return typeService.getAllTypes();

    }

    @GetMapping(value = "/{id}")
    private Type getTypeById(@PathVariable int id) {
        return typeService.getTypeById(id);
    }

}