package com.Gintaras.tcgtrading.card_service.business.service.impl;

import com.Gintaras.tcgtrading.card_service.business.mapper.CardRarityMapStruct;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRarityRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.business.service.CardRarityService;
import com.Gintaras.tcgtrading.card_service.model.CardRarity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CardRarityServiceImpl implements CardRarityService {

    @Autowired
    CardRarityRepository cardRarityRepository;

    @Autowired
    CardRarityMapStruct cardRarityMapStruct;

    @Override
    public ResponseEntity<CardRarity> saveCardRarity(CardRarity cardRarity) {
        CardRarityDAO cardRarityDAO = cardRarityRepository.save(cardRarityMapStruct.cardRarityToCardRarityDAO(cardRarity));
        log.info("New Card Rarity is saved: {}", cardRarity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardRarityMapStruct.cardRarityDAOToCardRarity(cardRarityDAO));
    }

    @Override
    public ResponseEntity<CardRarity> getCardRarityById(String id) {
        Optional<CardRarity> cardRarity = cardRarityRepository.findById(id)
                .map(cardRarityMapStruct::cardRarityDAOToCardRarity);
        if (cardRarity.isPresent()) {
            log.info("Card Rarity with id {} is found: {}", id, cardRarity.get());
            return ResponseEntity.ok(cardRarity.get());
        } else {
            log.info("Card Rarity with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteCardRarityById(String id) {
        if (cardRarityRepository.existsById(id)) {
            cardRarityRepository.deleteById(id);
            log.info("Card rarity with id {} has been deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Card rarity with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<CardRarity>> getCardRarityList() {
        List<CardRarityDAO> cardRarityList = cardRarityRepository.findAll();
        log.info("Get card rarity list. Size is: {}", cardRarityList.size());
        List<CardRarity> cardRarities = cardRarityList.stream()
                .map(cardRarityMapStruct::cardRarityDAOToCardRarity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cardRarities);
    }
}
