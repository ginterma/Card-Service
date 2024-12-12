package com.Gintaras.tcgtrading.card_service.business.service.impl;

import com.Gintaras.tcgtrading.card_service.business.mapper.CardMapStruct;
import com.Gintaras.tcgtrading.card_service.business.mapper.UserCardMapStruct;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.UserCardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.UserCardRepository;
import com.Gintaras.tcgtrading.card_service.business.service.UserCardService;
import com.Gintaras.tcgtrading.card_service.model.Card;
import com.Gintaras.tcgtrading.card_service.model.UserCard;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserCardImpl implements UserCardService {

    @Autowired
    UserCardRepository userCardRepository;

    @Autowired
    UserCardMapStruct userCardMapper;

    @Override
    public UserCard saveUserCard(UserCard userCard) {
        UserCardDAO userCardDAO = userCardRepository.save(userCardMapper.UserCardToUserCardDAO(userCard));
        log.info("New User Card is saved: {}", userCard);
        return userCardMapper.userCardDAOToUserCard(userCardDAO);
    }

    @Override
    public UserCard updateUserCard(UserCard userCard){
        UserCardDAO userCardDAO = userCardRepository.save(userCardMapper.UserCardToUserCardDAO(userCard));
        log.info("New User Card is saved: {}", userCard);
        return userCardMapper.userCardDAOToUserCard(userCardDAO);
    }

    @Override
    public Optional<UserCard> getUserCardById(String id){
        Optional<UserCard> userCard = userCardRepository.findById(id).map(userCardMapper::userCardDAOToUserCard);
        log.info("User Card with id {} is {}", id, userCard.isPresent() ? userCard.get() : "not found");
        return userCard;
    }

    @Override
    public void deleteUserCardById(String id){
        userCardRepository.deleteById(id);
        log.info("User Card  with id {} has been deleted", id);
    }

    @Override
    public List<UserCard> getUserCardList (){
        List<UserCardDAO> userCardList = userCardRepository.findAll();
        log.info("Get User Card list. Size is: {}", userCardList::size);
        return userCardList.stream().map(userCardMapper::userCardDAOToUserCard).collect(Collectors.toList());

    }
}
