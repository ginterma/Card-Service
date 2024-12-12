package com.Gintaras.tcgtrading.card_service.business.service;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.model.CardRarity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CardRarityService  {

    CardRarity saveCardRarity (CardRarity cardRarity);

    Optional<CardRarity> getCardRarityById (String id);

    List<CardRarity> getCardRarityList ();

    void deleteCardRarityById(String id);
}
