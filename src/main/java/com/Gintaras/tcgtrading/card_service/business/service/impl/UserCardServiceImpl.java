package com.Gintaras.tcgtrading.card_service.business.service.impl;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserCardServiceImpl implements UserCardService {

    @Autowired
    private UserCardRepository userCardRepository;

    @Autowired
    private UserCardMapStruct userCardMapper;

    @Autowired
    private CardRepository cardRepository;

    private final RestClient restClient;

    public UserCardServiceImpl() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:9098/api/v1/user").build();
    }

    @Override
    public ResponseEntity<UserCard> saveUserCard(UserCard userCard) {
        if (cardRepository.findById(userCard.getCardId()).isEmpty()) {
            log.warn("Card with id {} does not exist", userCard.getCardId());
            return ResponseEntity.badRequest().build();
        }

        Optional<?> user = restClient.get().uri("/{id}", userCard.getUserId())
                .retrieve().body(Optional.class);

        if (user.isEmpty()) {
            log.warn("User with id {} does not exist", userCard.getUserId());
            return ResponseEntity.badRequest().build();
        }

        if (userCard.getAmount() < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<UserCardDAO> userCards = userCardRepository.findByUserIdAndCardDAO_Id(userCard.getUserId(), userCard.getCardId());
        if (userCards.size() == 1) {
            userCard.setId(userCards.get(0).getId());
        }

        UserCardDAO userCardDAO = userCardRepository.save(userCardMapper.UserCardToUserCardDAO(userCard));
        log.info("New User Card is saved: {}", userCard);
        return ResponseEntity.ok(userCardMapper.userCardDAOToUserCard(userCardDAO));
    }

    @Override
    public ResponseEntity<UserCard> getUserCardById(String id) {
        Optional<UserCard> userCard = userCardRepository.findById(id).map(userCardMapper::userCardDAOToUserCard);
        log.info("User Card with id {} is {}", id, userCard.isPresent() ? userCard.get() : "not found");
        return userCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteUserCardById(String id) {
        Optional<UserCard> userCard = userCardRepository.findById(id).map(userCardMapper::userCardDAOToUserCard);
        if (userCard.isEmpty()) {
            log.warn("User Card with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        userCardRepository.deleteById(id);
        log.info("User Card with id {} has been deleted", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<UserCard>> getUserCardList() {
        List<UserCardDAO> userCardList = userCardRepository.findAll();
        log.info("Get User Card list. Size is: {}", userCardList.size());
        return ResponseEntity.ok(userCardList.stream()
                .map(userCardMapper::userCardDAOToUserCard)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Double> getCardValueById(String userCardId) {
        Optional<UserCard> userCard = userCardRepository.findById(userCardId).map(userCardMapper::userCardDAOToUserCard);
        if (userCard.isEmpty()) {
            return ResponseEntity.ok(0.0);
        }
        Optional<CardDAO> card = cardRepository.findById(userCard.get().getCardId());
        if (card.isEmpty()) {
            return ResponseEntity.ok(0.0);
        }
        return ResponseEntity.ok(card.get().getPrice());
    }

    @Override
    public ResponseEntity<Integer> getCardAmountById(String userCardId) {
        Optional<UserCard> userCard = userCardRepository.findById(userCardId).map(userCardMapper::userCardDAOToUserCard);
        if (userCard.isEmpty()) {
            return ResponseEntity.ok(0);
        }
        return ResponseEntity.ok(userCard.get().getAmount());
    }

    @Override
    public ResponseEntity<String> getUserCardByUserIdAndCardId(String userId, String cardId) {
        List<UserCardDAO> cardList = userCardRepository.findByUserIdAndCardDAO_Id(userId, cardId);
        if (cardList.isEmpty()) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(cardList.get(0).getId());
    }
}
