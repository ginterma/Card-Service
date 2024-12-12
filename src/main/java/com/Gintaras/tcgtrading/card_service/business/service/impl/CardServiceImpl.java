package com.Gintaras.tcgtrading.card_service.business.service.impl;

import com.Gintaras.tcgtrading.card_service.business.mapper.CardMapStruct;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.service.CardService;
import com.Gintaras.tcgtrading.card_service.model.Card;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardMapStruct cardMapper;

    @Override
   public Card saveCard(Card card) {
        CardDAO cardDAO = cardRepository.save(cardMapper.CardToCardDAO(card));
        log.info("New Card is saved: {}", card);
        return cardMapper.CardDAOToCard(cardDAO);
    }

    @Override
    public Optional<Card> getCardById(String id){
        Optional<Card> card = cardRepository.findById(id).map(cardMapper::CardDAOToCard);
        log.info("Card with id {} is {}", id, card.isPresent() ? card.get() : "not found");
        return card;
    }

    @Override
    public void deleteCardById(String id){
        cardRepository.deleteById(id);
        log.info("Card  with id {} has been deleted", id);
    }

    @Override
    public List<Card> getCardList (){
        List<CardDAO> cardList = cardRepository.findAll();
        log.info("Get card list. Size is: {}", cardList::size);
        return cardList.stream().map(cardMapper::CardDAOToCard).collect(Collectors.toList());

    }
}
