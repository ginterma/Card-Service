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
    public ResponseEntity<CardRarity> saveCardRarity(@ApiParam(value = "Card Rarity model that we want to save", required = true)
                                                     @Valid @RequestBody CardRarity cardRarity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(null);
        }
        return cardRarityService.saveCardRarity(cardRarity);
    }

    @ApiOperation(value = "Get Card rarity object from database by Id", response = CardRarity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @GetMapping(produces = "application/json", path = "/{id}")
    public ResponseEntity<CardRarity> getCardRarityById(@ApiParam(value = "The id of the Card rarity", required = true)
                                                        @PathVariable String id) {
        return cardRarityService.getCardRarityById(id); // Now directly returns the ResponseEntity from the service
    }

    @ApiOperation(value = "Get a list of all Card Rarities", response = CardRarity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CardRarity>> getAllCardRarities() {
        return cardRarityService.getCardRarityList(); // Return ResponseEntity from service
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
    public ResponseEntity<Void> deleteCardRarityById(@ApiParam(value = "The id of the Card Rarity", required = true)
                                                     @PathVariable @NonNull String id) {
        // Directly return the ResponseEntity returned from the service
        return cardRarityService.deleteCardRarityById(id);
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
    public ResponseEntity<CardRarity> updateCardRarity(@ApiParam(value = "The id of the Card Rarity", required = true)
                                                       @PathVariable @NonNull String id,
                                                       @ApiParam(value = "The updating Card Rarity model", required = true)
                                                       @Valid @RequestBody CardRarity cardRarity, BindingResult bindingResult) {
        // Validate the input
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(null);
        }

        // Ensure the provided id in the body matches the path parameter
        if (!Objects.equals(cardRarity.getId(), id)) {
            log.warn("Provided Card Rarity ids are not equal: {}!={}", id, cardRarity.getId());
            return ResponseEntity.badRequest().body(null);
        }

        // Call the service method to get the CardRarity by ID
        ResponseEntity<CardRarity> existingCardRarityResponse = cardRarityService.getCardRarityById(id);

        // Check if the CardRarity exists
        if (existingCardRarityResponse.getStatusCode().is2xxSuccessful()) {
            // CardRarity exists, update it
            return cardRarityService.saveCardRarity(cardRarity);
        } else {
            // CardRarity not found
            log.info("Card Rarity with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }
    }

}
