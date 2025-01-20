package com.Gintaras.tcgtrading.card_service.business.Controller;

import com.Gintaras.tcgtrading.card_service.business.service.CardService;
import com.Gintaras.tcgtrading.card_service.business.swagger.HTMLResponseMessages;
import com.Gintaras.tcgtrading.card_service.model.Card;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Log4j2
@RequestMapping("/api/v1/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    @ApiOperation(value = "Saves Card to database",
            notes = "If valid Card body is provided it is saved in the database",
            response = Card.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> saveCard(@ApiParam(value = "Card model that we want to save", required = true)
                                      @Valid @RequestBody Card card, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }

        ResponseEntity<Card> savedCard = cardService.saveCard(card);
        if (savedCard.getStatusCode().is4xxClientError()) {
            return ResponseEntity.notFound().build();
        }

        return savedCard;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates Card in the database",
            notes = "If valid Card body is provided it is saved in the database",
            response = Card.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> updateCard(@ApiParam(value = "The updating Card model", required = true)
                                        @Valid @RequestBody Card card,
                                        @ApiParam(value = "The id of the Card", required = true)
                                        @PathVariable String id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }

        if (!Objects.equals(card.getId(), id)) {
            log.warn("Provided Card ids are not equal: {}!={}", id, card.getId());
            return ResponseEntity.badRequest().body("Unsuccessful request: provided Card ids are not equal.");
        }

        ResponseEntity<Card> cardById = cardService.getCardById(id);
        if (!cardById.getStatusCode().is2xxSuccessful()) {
            log.info("Card with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }

        ResponseEntity<Card> updatedCard = cardService.saveCard(card);
        if (updatedCard.getStatusCode().is4xxClientError()) {
            return ResponseEntity.notFound().build();
        }

        log.info("Card with id {} is updated: {}", id, updatedCard.getBody());
        return updatedCard;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes Card in database",
            notes = "If Card exists with provided Id then it is deleted from the database",
            response = Card.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITH_DATA),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> deleteCardById(@ApiParam(value = "The id of the Card", required = true)
                                            @PathVariable @NonNull String id) {
        ResponseEntity<Card> cardById = cardService.getCardById(id);
        if (!cardById.getStatusCode().is2xxSuccessful()) {
            log.warn("Card with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }

        cardService.deleteCardById(id);
        log.info("Card with id {} has been deleted", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Card object from database by Id",
            response = Card.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> getCardById(@ApiParam(value = "The id of the Card", required = true)
                                         @PathVariable String id) {
        ResponseEntity<Card> cardById = cardService.getCardById(id);
        if (!cardById.getStatusCode().is2xxSuccessful()) {
            log.info("Card with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }

        log.info("Card with id {} is found: {}", id, cardById.getBody());
        return cardById;
    }

    @GetMapping
    @ApiOperation(value = "Get a list of all Cards", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<Card>> getAllCards() {
        ResponseEntity<List<Card>> foundCards = cardService.getCardList();
        if (foundCards.getBody().isEmpty()) {
            log.warn("Card list is empty.");
            return ResponseEntity.notFound().build();
        }

        log.info("Card list size: {}", foundCards.getBody().size());
        return foundCards;
    }
}
