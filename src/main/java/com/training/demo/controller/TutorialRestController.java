package com.training.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.training.demo.dto.TutorialDto;
import com.training.demo.exception.EntityNotFoundException;
import com.training.demo.exception.IllegalEntityArgument;
import com.training.demo.exception.MalformedJsonException;
import com.training.demo.model.Tutorial;
import com.training.demo.service.TutorialService;
import com.training.demo.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/tutorial")
@RequiredArgsConstructor
public class TutorialRestController {
    private final TutorialService tutorialService;

    @PutMapping
    public ResponseEntity<String> create(@RequestBody final String jsonTutorialDto) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final String body = "{}";

        final TutorialDto newTutorial, savedTutorial;

        try {
            newTutorial = ApiUtil.getObjectMapper().readValue(jsonTutorialDto, TutorialDto.class);
        }
        catch (JsonProcessingException e) {
            throw new MalformedJsonException(e.getMessage(), body);
        }

        if (newTutorial.getId() != null) {
            throw new IllegalEntityArgument(
                    Tutorial.class,
                    "Trying to create new Tutorial with non null id = " + newTutorial.getId(),
                    body);
        }
        savedTutorial = tutorialService.create(newTutorial);

        return new ResponseEntity<>(
                ApiUtil.getObjectAsPrettyJson(savedTutorial, body, responseHeaders),
                responseHeaders,
                CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> read(@PathVariable("id") final Long id) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final String body = "{}";

        Optional<TutorialDto> foundTutorial = tutorialService.get(id);

        if (foundTutorial.isEmpty()) throw new EntityNotFoundException(Tutorial.class, id, body);
        return new ResponseEntity<>(
                ApiUtil.getObjectAsPrettyJson(foundTutorial.get(), body, responseHeaders),
                responseHeaders,
                OK);
    }

    @GetMapping
    public ResponseEntity<String> readAll(@RequestParam(required = false) final Integer page) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final String body = "[]";

        final List<TutorialDto> users = tutorialService.getAll();

        return new ResponseEntity<>(
                ApiUtil.getObjectAsPrettyJson(users, body, responseHeaders),
                responseHeaders,
                HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") final Long id, @RequestBody final String jsonTutorial) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final String body = "{}";

        final TutorialDto paramTutorial;
        try {
            paramTutorial = ApiUtil.getObjectMapper().readValue(jsonTutorial, TutorialDto.class);
        }
        catch (JsonProcessingException e) {
            throw new MalformedJsonException(e.getMessage(), body);
        }
        if (id == null) {
            throw new IllegalEntityArgument(Tutorial.class, "id = null", body);
        }
        else if (!id.equals(paramTutorial.getId())) {
            throw new IllegalEntityArgument(Tutorial.class, "id in params != id in body", body);
        }
        else if (tutorialService.get(id).isEmpty()) {
            throw new EntityNotFoundException(Tutorial.class, id, body);
        }
        final TutorialDto savedTutorial = tutorialService.update(paramTutorial);

        return new ResponseEntity<>(
                ApiUtil.getObjectAsPrettyJson(savedTutorial, "{}", responseHeaders),
                responseHeaders,
                CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") final Long id) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final String body = "{}";

        final Optional<TutorialDto> foundTutorial = tutorialService.get(id);

        if (foundTutorial.isEmpty()) throw new EntityNotFoundException(Tutorial.class, id, body);

        tutorialService.delete(id);

        return new ResponseEntity<>(
                body,
                responseHeaders,
                HttpStatus.OK
        );
    }
}
