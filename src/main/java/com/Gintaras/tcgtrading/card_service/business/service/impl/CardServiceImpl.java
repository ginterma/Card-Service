package com.Gintaras.tcgtrading.card_service.business.service.impl;

import com.Gintaras.tcgtrading.card_service.business.mapper.CardMapStruct;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRarityRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.service.CardService;
import com.Gintaras.tcgtrading.card_service.model.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapStruct cardMapper;
    private final CardRarityRepository cardRarityRepository;

    public CardServiceImpl(CardRepository cardRepository, CardMapStruct cardMapper,
                           CardRarityRepository cardRarityRepository) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.cardRarityRepository = cardRarityRepository;
    }

    @Override
    public ResponseEntity<Card> saveCard(Card card) {
        if (cardRarityRepository.findById(card.getCardRarityId()).isEmpty()) {
            log.warn("Card rarity with id {} does not exist", card.getCardRarityId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return 400 if rarity doesn't exist
        }

        CardDAO cardDAO = cardRepository.save(cardMapper.CardToCardDAO(card));
        log.info("New Card is saved: {}", card);

        return new ResponseEntity<>(cardMapper.CardDAOToCard(cardDAO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Card> getCardById(String id) {
        Optional<Card> card = cardRepository.findById(id).map(cardMapper::CardDAOToCard);

        if (card.isPresent()) {
            log.info("Card with id {} found: {}", id, card.get());
            return new ResponseEntity<>(card.get(), HttpStatus.OK);
        } else {
            log.info("Card with id {} is not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Void> deleteCardById(String id) {
        if (cardRepository.existsById(id)) {
            cardRepository.deleteById(id);
            log.info("Card with id {} has been deleted", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("Card with id {} does not exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<Card>> getCardList() {
        List<CardDAO> cardList = cardRepository.findAll();
        log.info("Get card list. Size is: {}", cardList.size());

        List<Card> cards = cardList.stream().map(cardMapper::CardDAOToCard).collect(Collectors.toList());

        return new ResponseEntity<>(cards, HttpStatus.OK);
    }
}
