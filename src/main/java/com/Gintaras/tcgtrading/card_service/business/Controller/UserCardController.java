package com.Gintaras.tcgtrading.card_service.business.Controller;

import com.Gintaras.tcgtrading.card_service.business.service.CardRarityService;
import com.Gintaras.tcgtrading.card_service.business.service.CardService;
import com.Gintaras.tcgtrading.card_service.business.service.UserCardService;
import com.Gintaras.tcgtrading.card_service.business.swagger.HTMLResponseMessages;
import com.Gintaras.tcgtrading.card_service.model.Card;
import com.Gintaras.tcgtrading.card_service.model.UserCard;
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
@RequestMapping("/api/v1/user/card")
public class UserCardController {

    @Autowired
    UserCardService userCardService;

    @Autowired
    CardService cardService;

    @PostMapping
    @ApiOperation(value = "Saves User Card to database",
            notes = "If valid User Card body is provided it is saved in the database",
            response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> saveUserCard(@ApiParam(value = "User Card model that we want to save", required = true)
                               @Valid @RequestBody UserCard userCard, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        if(cardService.getCardById(userCard.getCardId()).isEmpty()){
            log.info("Card  with id {} does not exist", userCard.getCardId());
            return ResponseEntity.notFound().build();
        }
        UserCard savedUserCard = userCardService.saveUserCard(userCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserCard);

    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates User Card in the database",
            notes = "If valid User Card body is provided it is saved in the database",
            response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateUserCard(@ApiParam(value = "The updating User Card model", required = true)
                                        @Valid @RequestBody UserCard userCard,
                                        @ApiParam(value = "The id of the User Card", required = true)
                                        @PathVariable String id, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        if (!Objects.equals(userCard.getId(), id)) {
            log.warn("Provided User Card  ids are not equal: {}!={}", id, userCard.getId());
            return ResponseEntity.badRequest().body("Unsuccessful request responds with this code." +
                    "Passed data has errors - provided Card ids are not equal.");
        }
        if(cardService.getCardById(userCard.getCardId()).isEmpty()){
            log.info("Card with id {} does not exist", userCard.getCardId());
            return ResponseEntity.notFound().build();
        }

        Optional<UserCard> userCardById = userCardService.getUserCardById(id);
        if (userCardById.isEmpty()) {
            log.info("User Card with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }
        UserCard updatedUserCard = userCardService.saveUserCard(userCard);
        log.info("UserCard with id {} is updated: {}", id, updatedUserCard);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUserCard);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes UserCard in database",
            notes = "If UserCard exists with provided Id then it is deleted from the database",
            response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITH_DATA),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<?> deleteUserCardById(@ApiParam(value = "The id of the User Card", required = true)
                                     @PathVariable @NonNull String id) {
        Optional<UserCard> userCardByiD = userCardService.getUserCardById(id);
        if (userCardByiD.isEmpty()) {
            log.warn("UserCard with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        userCardService.deleteUserCardById(id);
        log.info("UserCard with id {} is deleted: {}", id, userCardByiD);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @ApiOperation(
            value = "Get User Card object from database by Id",
            response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/{id}")
    public ResponseEntity<?> getUserCardById(@ApiParam(value = "The id of the User Card", required = true)
                                         @PathVariable String id) {
        Optional<UserCard> userCardById = userCardService.getUserCardById(id);
        if (userCardById.isEmpty()) {
            log.info("User Card with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        } else {
            log.info("User Card with id {} is found: {}", id, userCardById);
            return ResponseEntity.ok(userCardById);
        }
    }

    @ApiOperation(
            value = "Get a list of all Users'es  Cards",
            response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserCard>> getAllUserCards() {
        List<UserCard> foundUserCards = userCardService.getUserCardList();
        if (foundUserCards.isEmpty()) {
            log.warn("User Card list is empty: {}", foundUserCards);
            return ResponseEntity.notFound().build();
        } else {
            log.info("User Card list is: {}", foundUserCards::size);
            return new ResponseEntity<>(foundUserCards, HttpStatus.OK);
        }
    }
}
