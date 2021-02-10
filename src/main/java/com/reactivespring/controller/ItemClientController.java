package com.reactivespring.controller;

import com.reactivespring.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// this controller behaves as client
@RestController
@Slf4j
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/client/retrieve")
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient.get().uri("/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items in client project");
    }

    @GetMapping("/client/retrieve/{id}")
    public Mono<Item> getItemByIdUsingRetrieve(@PathVariable String id) {
        return webClient.get().uri("/item/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Retrieved Item in client project");
    }

    @PutMapping("/client/update/{id}")
    public Mono<Item> updateItemByIdUsingRetrieve(@RequestBody Item item, @PathVariable String id) {
        return webClient.put().uri("/item/{id}", id)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Updated Item in client project");
    }

    @DeleteMapping("/client/delete/{id}")
    public Mono<Item> deleteItemById(@PathVariable String id) {
        return webClient.delete().uri("/item/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Deleted Item in client project");
    }

    @PostMapping("/client/addItem")
    public Mono<Item> addItem(@RequestBody Item item) {
        return webClient.post().uri("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Created new Item in client project");
    }

    @GetMapping("/client/error")
    public Flux<Item> error() {
        return webClient.get().uri("/items/runtimeException")
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    Mono<String> errorMono = clientResponse.bodyToMono(String.class);
                    return errorMono.flatMap((error) -> {
                        log.error("error message: " + error);
                        throw new RuntimeException(error);
                    });
                }).bodyToFlux(Item.class);
    }

}
