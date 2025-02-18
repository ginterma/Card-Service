package com.Gintaras.tcgtrading.card_service.ServiceTest;

import com.Gintaras.tcgtrading.card_service.business.mapper.CardRarityMapStruct;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRarityRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.business.service.impl.CardRarityServiceImpl;
import com.Gintaras.tcgtrading.card_service.model.CardRarity;
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
public class CardRarityServiceTest {

    @Mock
    private CardRarityRepository cardRarityRepository;

    @InjectMocks
    private CardRarityServiceImpl cardRarityService;

    @Mock
    private CardRarityMapStruct cardRarityMapStruct;

    private CardRarity cardRarity;
    private CardRarityDAO cardRarityDAO;

    @BeforeEach
    public void setUp(){
        cardRarity = new CardRarity("1", "Rare");
        cardRarityDAO = new CardRarityDAO("1", "Rare");
    }

    @Test
    public void saveCardRarityTest(){
        when(cardRarityRepository.save(cardRarityDAO)).thenReturn(cardRarityDAO);
        when(cardRarityMapStruct.cardRarityDAOToCardRarity(cardRarityDAO)).thenReturn(cardRarity);
        when(cardRarityMapStruct.cardRarityToCardRarityDAO(cardRarity)).thenReturn(cardRarityDAO);

        ResponseEntity<CardRarity> responseEntity = cardRarityService.saveCardRarity(cardRarity);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(cardRarity, responseEntity.getBody());
    }

    @Test
    public void getCardRarityByIdTest_CardRarityExist(){
        when(cardRarityRepository.findById("1")).thenReturn(Optional.of(cardRarityDAO));
        when(cardRarityMapStruct.cardRarityDAOToCardRarity(cardRarityDAO)).thenReturn(cardRarity);

        ResponseEntity<CardRarity> responseEntity = cardRarityService.getCardRarityById("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cardRarity,responseEntity.getBody());


    }

    @Test
    public void getCardRarityByIdTest_CardRarityNotExist(){
        when(cardRarityRepository.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<CardRarity> responseEntity = cardRarityService.getCardRarityById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null,responseEntity.getBody());


    }
    @Test
    public void deleteCardRarityById_CardRarityExist(){
        when(cardRarityRepository.existsById("1")).thenReturn(true);
        doNothing().when(cardRarityRepository).deleteById("1");

        ResponseEntity<Void> responseEntity = cardRarityService.deleteCardRarityById("1");

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void deleteCardRarityById_CardRarityNotExist(){
        when(cardRarityRepository.existsById("1")).thenReturn(false);

        ResponseEntity<Void> responseEntity = cardRarityService.deleteCardRarityById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void getCardRarityListTest(){
        List<CardRarity> cardRarityList = new ArrayList<>();
        List<CardRarityDAO> cardRarityDAOList = new ArrayList<>();
        cardRarityList.add(cardRarity);
        cardRarityDAOList.add(cardRarityDAO);

        when(cardRarityRepository.findAll()).thenReturn(cardRarityDAOList);
        when(cardRarityMapStruct.cardRarityDAOToCardRarity(cardRarityDAO)).thenReturn(cardRarity);

        ResponseEntity<List<CardRarity>> responseEntity = cardRarityService.getCardRarityList();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cardRarityList, responseEntity.getBody());

    }

}


