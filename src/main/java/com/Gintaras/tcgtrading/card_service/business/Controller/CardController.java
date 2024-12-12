package com.Gintaras.tcgtrading.card_service.business.Controller;

import com.Gintaras.tcgtrading.card_service.business.service.CardRarityService;
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
import org.springframework.http.HttpStatus;
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
    CardService cardService;

    @Autowired
    CardRarityService cardRarityService;

    @PostMapping
    @ApiOperation(value = "Saves Card to database",
            notes = "If valid Card body is provided it is saved in the database",
            response = Card.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> saveCard(@ApiParam(value = "Card model that we want to save", required = true)
                               @Valid @RequestBody Card card, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        if(cardRarityService.getCardRarityById(card.getCardRarityId()).isEmpty()){
            log.info("Card Rarity with id {} does not exist", card.getCardRarityId());
            return ResponseEntity.notFound().build();
        }
        Card savedCard = cardService.saveCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);

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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateCard(@ApiParam(value = "The updating Card model", required = true)
                                        @Valid @RequestBody Card card,
                                        @ApiParam(value = "The id of the Card", required = true)
                                        @PathVariable String id, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        if (!Objects.equals(card.getId(), id)) {
            log.warn("Provided Card  ids are not equal: {}!={}", id, card.getId());
            return ResponseEntity.badRequest().body("Unsuccessful request responds with this code." +
                    "Passed data has errors - provided Card ids are not equal.");
        }
        if(cardRarityService.getCardRarityById(card.getCardRarityId()).isEmpty()){
            log.info("Card Rarity with id {} does not exist", card.getCardRarityId());
            return ResponseEntity.notFound().build();
        }

        Optional<Card> cardById = cardService.getCardById(id);
        if (cardById.isEmpty()) {
            log.info("Card with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }
        Card updatedCard = cardService.saveCard(card);
        log.info("Card with id {} is updated: {}", id, updatedCard);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCard);
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<?> deleteCardById(@ApiParam(value = "The id of the Card", required = true)
                                           @PathVariable @NonNull String id) {
        Optional<Card> cardById = cardService.getCardById(id);
        if (cardById.isEmpty()) {
            log.warn("Card with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        cardService.deleteCardById(id);
        log.info("Card with id {} is deleted: {}", id, cardById);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @ApiOperation(
            value = "Get Card object from database by Id",
            response = Card.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/{id}")
    public ResponseEntity<?> getCardById(@ApiParam(value = "The id of the Card", required = true)
                                               @PathVariable String id) {
        Optional<Card> cardById = cardService.getCardById(id);
        if (cardById.isEmpty()) {
            log.info("Card with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        } else {
            log.info("Card with id {} is found: {}", id, cardById);
            return ResponseEntity.ok(cardById);
        }
    }

    @ApiOperation(
            value = "Get a list of all Cards",
                response = Card.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Card>> getAllCards() {
        List<Card> foundCards = cardService.getCardList();
        if (foundCards.isEmpty()) {
            log.warn("Card list is empty: {}", foundCards);
            return ResponseEntity.notFound().build();
        } else {
            log.info("Card list is: {}", foundCards::size);
            return new ResponseEntity<>(foundCards, HttpStatus.OK);
        }
    }
}
