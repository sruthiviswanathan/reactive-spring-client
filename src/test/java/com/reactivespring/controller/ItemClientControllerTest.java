package com.reactivespring.controller;

import com.reactivespring.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
class ItemClientControllerTest {

    @Autowired
    WebTestClient webTestClient;


    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/client/addItem")
                .body(BodyInserters.fromValue(Item.builder().id("FRIDGE124").description("Fridge").price(30000.00).build()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Fridge")
                .jsonPath("$.price").isEqualTo(30000.00);
    }

    @Test
    void getAllItemsUsingRetrieve() {
        webTestClient.get().uri("/client/retrieve")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBodyList(Item.class);
    }

    @Test
    void addItem() {
        webTestClient.post().uri("/client/addItem")
                .body(BodyInserters.fromValue(Item.builder().description("Smart TV").price(30000.00).build()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Smart TV")
                .jsonPath("$.price").isEqualTo(30000.00);
    }

    @Test
    void getItemById() {
        webTestClient.get().uri("client/retrieve/FRIDGE124")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(Item.class);

    }

}