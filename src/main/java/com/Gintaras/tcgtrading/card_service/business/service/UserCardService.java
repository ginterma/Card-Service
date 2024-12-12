package com.Gintaras.tcgtrading.card_service.business.service;

import com.Gintaras.tcgtrading.card_service.model.Card;
import com.Gintaras.tcgtrading.card_service.model.UserCard;

import java.util.List;
import java.util.Optional;

public interface UserCardService {

    public UserCard saveUserCard(UserCard userCard);

    public void deleteUserCardById(String id);

    public Optional<UserCard> getUserCardById(String id);

    public List<UserCard> getUserCardList();

    public UserCard updateUserCard (UserCard userCard);
}
