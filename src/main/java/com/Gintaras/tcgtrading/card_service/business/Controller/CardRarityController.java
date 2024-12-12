package com.Gintaras.tcgtrading.card_service.business.Controller;

import com.Gintaras.tcgtrading.card_service.business.service.CardRarityService;
import com.Gintaras.tcgtrading.card_service.business.swagger.HTMLResponseMessages;
import com.Gintaras.tcgtrading.card_service.model.CardRarity;
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
@RequestMapping("/api/v1/card/rarity")
public class CardRarityController {

    @Autowired
    CardRarityService cardRarityService;

    @PostMapping
    @ApiOperation(value = "Saves Card Rarity to database",
            notes = "If valid Card Rarity body is provided it is saved in the database",
            response = CardRarity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> saveCardRarity(@ApiParam(value = "Card Rarity model that we want to save", required = true)
                                     @Valid @RequestBody CardRarity cardRarity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        CardRarity savedCardRarity = cardRarityService.saveCardRarity(cardRarity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCardRarity);

    }

    @ApiOperation(
            value = "Get Card rarity object from database by Id",
            response = CardRarity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/{id}")
    public ResponseEntity<?> getCardRarityById(@ApiParam(value = "The id of the Card rarity", required = true)
                                         @PathVariable String id) {
        Optional<CardRarity> cardRarityById = cardRarityService.getCardRarityById(id);
        if (cardRarityById.isEmpty()) {
            log.info("Card Rarity with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        } else {
            log.info("Card rarity with id {} is found: {}", id, cardRarityById);
            return ResponseEntity.ok(cardRarityById);
        }
    }

    @ApiOperation(
            value = "Get a list of all Card Rarities",
            response = CardRarity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CardRarity>> getAllCardRarities() {
        List<CardRarity> foundCardRarities = cardRarityService.getCardRarityList();
        if (foundCardRarities.isEmpty()) {
            log.warn("Card Rarities list is empty: {}", foundCardRarities);
            return ResponseEntity.notFound().build();
        } else {
            log.info("Card rarities list is: {}", foundCardRarities::size);
            return new ResponseEntity<>(foundCardRarities, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes Card Rarity in database",
            notes = "If Card Rarity exists with provided Id then it is deleted from the database",
            response = CardRarity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITH_DATA),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<?> deleteCardRarityById(@ApiParam(value = "The id of the Card Rarity", required = true)
                                           @PathVariable @NonNull String id) {
        Optional<CardRarity> cardRarityById = cardRarityService.getCardRarityById(id);
        if (cardRarityById.isEmpty()) {
            log.warn("Card rarity with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        cardRarityService.deleteCardRarityById(id);
        log.info("Card Rarity with id {} is deleted: {}", id, cardRarityById);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates CardRarity in database",
            notes = "If Card Rarity exists with provided Id then it is updated according to provided body",
            response = CardRarity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> updateCardRarity(@ApiParam(value = "The id of the Card Rarity", required = true)
                                 @PathVariable @NonNull String id,
                                 @ApiParam(value = "The updating Card Rarity model", required = true)
                                 @Valid @RequestBody CardRarity cardRarity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        if (!Objects.equals(cardRarity.getId(), id)) {
            log.warn("Provided Card Rarity ids are not equal: {}!={}", id, cardRarity.getId());
            return ResponseEntity.badRequest().body("Unsuccessful request responds with this code." +
                    "Passed data has errors - provided Card Rarity ids are not equal.");
        }
        Optional<CardRarity> cardRarityById = cardRarityService.getCardRarityById(id);
        if (cardRarityById.isEmpty()) {
            log.info("Card Rarity with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }
        CardRarity updatedCardRarity = cardRarityService.saveCardRarity(cardRarity);
        log.info("Card Rarity with id {} is updated: {}", id, updatedCardRarity);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCardRarity);
    }
}
