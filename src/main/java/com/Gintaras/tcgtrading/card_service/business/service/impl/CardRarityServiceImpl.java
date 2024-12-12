package com.Gintaras.tcgtrading.card_service.business.service.impl;

import com.Gintaras.tcgtrading.card_service.business.mapper.CardRarityMapStruct;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRarityRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.business.service.CardRarityService;
import com.Gintaras.tcgtrading.card_service.model.Card;
import com.Gintaras.tcgtrading.card_service.model.CardRarity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CardRarity saveCardRarity(CardRarity cardRarity) {
        CardRarityDAO cardRarityDAO = cardRarityRepository.save(cardRarityMapStruct.cardRarityToCardRarityDAO(cardRarity));
        log.info("New Card Rarity is saved: {}", cardRarity);
        return cardRarityMapStruct.cardRarityDAOToCardRarity(cardRarityDAO);
    }

    @Override
    public Optional<CardRarity> getCardRarityById(String id){
        Optional<CardRarity> cardRarity = cardRarityRepository.findById(id).map(cardRarityMapStruct::cardRarityDAOToCardRarity);
        log.info("Card Rarity with id {} is {}", id, cardRarity.isPresent() ? cardRarity.get() : "not found");
        return cardRarity;
    }

    @Override
    public void deleteCardRarityById(String id){
        cardRarityRepository.deleteById(id);
        log.info("Card rarity with id {} has been deleted", id);
    }

    @Override
    public List<CardRarity> getCardRarityList (){
        List<CardRarityDAO> cardRarityList = cardRarityRepository.findAll();
        log.info("Get card rarity list. Size is: {}", cardRarityList::size);
        return cardRarityList.stream().map(cardRarityMapStruct::cardRarityDAOToCardRarity).collect(Collectors.toList());

    }
}
