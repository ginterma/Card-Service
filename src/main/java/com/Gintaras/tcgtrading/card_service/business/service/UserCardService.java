package com.Gintaras.tcgtrading.card_service.business.service;

import com.Gintaras.tcgtrading.card_service.model.UserCard;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserCardService {

    ResponseEntity<UserCard> saveUserCard(UserCard userCard);

    ResponseEntity<Void> deleteUserCardById(String id);

    ResponseEntity<UserCard> getUserCardById(String id);

    ResponseEntity<List<UserCard>> getUserCardList();

    ResponseEntity<Double> getCardValueById(String cardId);

    ResponseEntity<Integer> getCardAmountById(String userCardId);

    ResponseEntity<String> getUserCardByUserIdAndCardId(String userId, String cardId);
}
