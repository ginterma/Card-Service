package com.Gintaras.tcgtrading.card_service.business.service;

import com.Gintaras.tcgtrading.card_service.model.Card;

import java.util.List;
import java.util.Optional;

public interface CardService {

    public Card saveCard(Card card);

    public void deleteCardById(String id);

    public Optional<Card> getCardById(String id);

    public List<Card> getCardList();
}
