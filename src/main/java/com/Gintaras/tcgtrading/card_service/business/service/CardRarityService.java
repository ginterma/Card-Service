package com.Gintaras.tcgtrading.card_service.business.service;

import com.Gintaras.tcgtrading.card_service.model.CardRarity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CardRarityService {

    ResponseEntity<CardRarity> saveCardRarity(CardRarity cardRarity);

    ResponseEntity<CardRarity> getCardRarityById(String id);

    ResponseEntity<List<CardRarity>> getCardRarityList();

    ResponseEntity<Void> deleteCardRarityById(String id);
}
