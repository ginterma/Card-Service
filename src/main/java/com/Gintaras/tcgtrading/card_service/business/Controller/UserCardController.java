package com.Gintaras.tcgtrading.card_service.business.Controller;

import com.Gintaras.tcgtrading.card_service.business.service.UserCardService;
import com.Gintaras.tcgtrading.card_service.business.swagger.HTMLResponseMessages;
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
    private UserCardService userCardService;

    @PostMapping
    @ApiOperation(value = "Saves User Card to database", notes = "If valid User Card body is provided it is saved in the database", response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> saveUserCard(@ApiParam(value = "User Card model that we want to save", required = true) @Valid @RequestBody UserCard userCard, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }

        ResponseEntity<UserCard> response = userCardService.saveUserCard(userCard);
        if (response.getStatusCode().is4xxClientError()) {
            log.warn("User Card with id {} could not be saved", userCard.getId());
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        log.info("UserCard saved successfully: {}", userCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates User Card in the database", notes = "If valid User Card body is provided it is saved in the database", response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateUserCard(@ApiParam(value = "The updating User Card model", required = true) @Valid @RequestBody UserCard userCard,
                                            @ApiParam(value = "The id of the User Card", required = true) @PathVariable String id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }

        if (!Objects.equals(userCard.getId(), id)) {
            log.warn("Provided User Card  ids are not equal: {}!={}", id, userCard.getId());
            return ResponseEntity.badRequest().body("Provided Card ids are not equal.");
        }

        ResponseEntity<UserCard> response = userCardService.saveUserCard(userCard);
        if (response.getStatusCode().is4xxClientError()) {
            log.warn("User Card with id {} could not be updated", id);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        log.info("UserCard updated successfully: {}", response.getBody());
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes UserCard in database", notes = "If UserCard exists with provided Id then it is deleted from the database", response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITH_DATA),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteUserCardById(@ApiParam(value = "The id of the User Card", required = true) @PathVariable @NonNull String id) {
        ResponseEntity<Void> response = userCardService.deleteUserCardById(id);
        if (response.getStatusCode().is4xxClientError()) {
            log.warn("UserCard with id {} could not be deleted", id);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        log.info("UserCard with id {} has been deleted", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get User Card object from database by Id", response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> getUserCardById(@ApiParam(value = "The id of the User Card", required = true) @PathVariable String id) {
        ResponseEntity<UserCard> response = userCardService.getUserCardById(id);
        if (response.getStatusCode().is4xxClientError()) {
            log.info("User Card with id {} does not exist", id);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        log.info("User Card with id {} is found: {}", id, response.getBody());
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping
    @ApiOperation(value = "Get a list of all Users'es Cards", response = UserCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<UserCard>> getAllUserCards() {
        ResponseEntity<List<UserCard>> response = userCardService.getUserCardList();
        if (response.getStatusCode().is4xxClientError()) {
            log.warn("User Card list is empty");
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        log.info("User Card list is fetched successfully: {}", response.getBody().size());
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/value/{id}")
    @ApiOperation(value = "Get User card value by Id", response = Double.class)
    public ResponseEntity<?> getUserCardValue(@ApiParam(value = "The id of the User Card", required = true) @PathVariable String id) {
        ResponseEntity<Double> response = userCardService.getCardValueById(id);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/amount/{id}")
    @ApiOperation(value = "Get User card amount by Id", response = Integer.class)
    public ResponseEntity<?> getUserCardAmount(@ApiParam(value = "The id of the User Card", required = true) @PathVariable String id) {
        ResponseEntity<Integer> response = userCardService.getCardAmountById(id);
        return ResponseEntity.ok(response.getBody());
    }

    @PutMapping("/amount/{id}")
    @ApiOperation(value = "Updates User Card amount in the database",
            notes = "If User Card with provided Id is valid, the amount will be updated",
            response = UserCard.class)
    public ResponseEntity<?> updateUserCardAmount(@ApiParam(value = "The id of the User Card", required = true) @PathVariable String id,
                                                  @ApiParam(value = "Amount of cards", required = true) @RequestBody Integer amount) {
        if (amount < 0) {
            log.warn("Invalid amount: {} for User Card with id {}", amount, id);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }

        ResponseEntity<UserCard> userCardResponse = userCardService.getUserCardById(id);
        if (!userCardResponse.getStatusCode().is2xxSuccessful()) {
            log.warn("User Card with id {} not found", id);
            return ResponseEntity.status(userCardResponse.getStatusCode()).body(userCardResponse.getBody());
        }

        UserCard userCard = userCardResponse.getBody();
        if (userCard == null) {
            log.warn("User Card with id {} not found", id);
            return ResponseEntity.notFound().build();
        }

        userCard.setAmount(amount);

        ResponseEntity<UserCard> response = userCardService.saveUserCard(userCard);
        if (response.getStatusCode().is4xxClientError()) {
            log.warn("Failed to update the amount for User Card with id {}", id);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        log.info("Successfully updated the amount for User Card with id {}: {}", id, response.getBody());
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/unique/{userId}/{cardId}")
    @ApiOperation(value = "Check if user card with specific User Id and cardId exists", response = String.class)
    public ResponseEntity<?> checkIfCardExist(@ApiParam(value = "The id of User that owns the card", required = true) @PathVariable String userId,
                                              @ApiParam(value = "The id of the card", required = true) @PathVariable String cardId) {
        ResponseEntity<String> response = userCardService.getUserCardByUserIdAndCardId(userId, cardId);
        if (response.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        return ResponseEntity.ok(response.getBody());
    }
}
