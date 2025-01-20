package com.Gintaras.tcgtrading.card_service.business.service;

import com.Gintaras.tcgtrading.card_service.model.Card;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CardService {

    ResponseEntity<Card> saveCard(Card card);

    ResponseEntity<Void> deleteCardById(String id);

    ResponseEntity<Card> getCardById(String id);

    ResponseEntity<List<Card>> getCardList();
}
