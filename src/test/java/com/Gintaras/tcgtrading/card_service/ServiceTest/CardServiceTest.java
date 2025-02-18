package com.Gintaras.tcgtrading.card_service.ServiceTest;

import com.Gintaras.tcgtrading.card_service.business.mapper.CardMapStruct;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRarityRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.business.service.impl.CardServiceImpl;
import com.Gintaras.tcgtrading.card_service.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Mock
    private CardMapStruct cardMapStruct;

    @Mock CardRarityRepository cardRarityRepository;

    private Card card;
    private CardDAO cardDAO;
    private CardRarityDAO cardRarityDAO;

    @BeforeEach
    public void setUp() {
        cardRarityDAO = new CardRarityDAO("1", "Rare");
        card = new Card("1", "1", "Best card", 5.0);
        cardDAO = new CardDAO("1", cardRarityDAO, "Best card", 5.0);

    }

    @Test
    public void saveCardTest_CardRarityExist(){
        when(cardRarityRepository.findById("1")).thenReturn(Optional.of(cardRarityDAO));
        when(cardRepository.save(cardDAO)).thenReturn(cardDAO);
        when(cardMapStruct.CardToCardDAO(card)).thenReturn(cardDAO);
        when(cardMapStruct.CardDAOToCard(cardDAO)).thenReturn(card);

        ResponseEntity<Card> responseEntity = cardService.saveCard(card);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(card, responseEntity.getBody());
    }

    @Test
    public void saveCardTest_CardRarityNotExist(){
        when(cardRarityRepository.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<Card> responseEntity = cardService.saveCard(card);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void getCardByIdTest_CardExist(){
        when(cardRepository.findById("1")).thenReturn(Optional.of(cardDAO));
        when(cardMapStruct.CardDAOToCard(cardDAO)).thenReturn(card);

        ResponseEntity<Card> responseEntity = cardService.getCardById("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(card, responseEntity.getBody());

    }
    @Test
    public void getCardByIdTest_CardNotExist(){
        when(cardRepository.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<Card> responseEntity = cardService.getCardById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());

    }

    @Test
    public void deleteCardByIdTest_CardExist(){
        when(cardRepository.existsById("1")).thenReturn(true);
        doNothing().when(cardRepository).deleteById("1");

        ResponseEntity<Void> responseEntity = cardService.deleteCardById("1");

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void deleteCardByIdTest_CardNotExist(){
        when(cardRepository.existsById("1")).thenReturn(false);

        ResponseEntity<Void> responseEntity = cardService.deleteCardById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
    @Test
    public void getCardListTest(){
        List<Card> cardList = new ArrayList<>();
        List<CardDAO> cardDAOList = new ArrayList<>();
        cardList.add(card);
        cardDAOList.add(cardDAO);
        when(cardRepository.findAll()).thenReturn(cardDAOList);
        when(cardMapStruct.CardDAOToCard(cardDAO)).thenReturn(card);

        ResponseEntity<List<Card>> responseEntity = cardService.getCardList();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cardList, responseEntity.getBody());

    }
}