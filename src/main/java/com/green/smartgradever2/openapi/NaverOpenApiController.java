package com.green.smartgradever2.openapi;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/professor")
public class NaverOpenApiController {

    private final NaverOpenApiService service;


    @GetMapping("/find-book")
    @Operation(summary = "isbn 입력해서 title + img")
    public BookVo getTimetable(@RequestParam String isbn){
        return service.getTimetable(isbn);
    }

}
