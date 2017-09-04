package com.hason.controller;

import com.hason.entity.Bird;
import com.hason.exception.EntityNotFoundException;
import com.hason.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Bird 的接口
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/4
 */
@RestController
@RequestMapping("/birds")
public class BirdController {

    @Autowired
    private BirdService birdService;

    @GetMapping(value = "/{birdId}")
    public Bird getBird(@PathVariable("birdId") Long birdId) throws EntityNotFoundException {
        return birdService.getBird(birdId);
    }

    @GetMapping(value = "/noexception/{birdId}")
    public Bird getBirdNoException(@PathVariable("birdId") Long birdId) throws EntityNotFoundException {
        return birdService.getBirdNoException(birdId);
    }

    @PostMapping
    public Bird createBird(@RequestBody @Valid Bird bird){
        return birdService.createBird(bird);
    }

}
