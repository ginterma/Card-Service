package com.Gintaras.tcgtrading.card_service.ServiceTest;

import com.Gintaras.tcgtrading.card_service.business.mapper.UserCardMapStruct;
import com.Gintaras.tcgtrading.card_service.business.repository.CardRepository;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.UserCardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.UserCardRepository;
import com.Gintaras.tcgtrading.card_service.business.service.impl.UserCardServiceImpl;
import com.Gintaras.tcgtrading.card_service.model.UserCard;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCardServiceTest {

    @Mock
    private UserCardRepository userCardRepository;

    @InjectMocks
    private UserCardServiceImpl userCardService;

    @Mock
    private UserCardMapStruct userCardMapStruct;

    @Mock
    CardRepository cardRepository;


    private CardDAO cardDAO;
    private UserCard userCard;
    private UserCardDAO userCardDAO;

    @BeforeEach
    public void setUp() {
        userCard = new UserCard("1", "1", "1", 2);
        userCardDAO = new UserCardDAO("1", "1", cardDAO, 2);
        cardDAO = new CardDAO("1", null, "Best card", 5.0);

    }

    @Test
    public void getUserCardByIdTest_UserCardExist(){
        when(userCardRepository.findById("1")).thenReturn(Optional.of(userCardDAO));
        when(userCardMapStruct.userCardDAOToUserCard(userCardDAO)).thenReturn(userCard);

        ResponseEntity<UserCard> responseEntity = userCardService.getUserCardById("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userCard, responseEntity.getBody());
    }
    @Test
    public void getUserCardByIdTest_UserCardNotExist(){
        when(userCardRepository.findById("1")).thenReturn(Optional.empty());
        ResponseEntity<UserCard> responseEntity = userCardService.getUserCardById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
    @Test
    public void deleteUserCardById_UserCardExist(){
        when(userCardRepository.findById("1")).thenReturn(Optional.of(userCardDAO));
        when(userCardMapStruct.userCardDAOToUserCard(userCardDAO)).thenReturn(userCard);
        doNothing().when(userCardRepository).deleteById("1");

        ResponseEntity<Void> responseEntity = userCardService.deleteUserCardById("1");

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void deleteUserCardById_UserCardNotExist(){
        when(userCardRepository.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<Void> responseEntity = userCardService.deleteUserCardById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void getAllUserCardListTest(){
        List<UserCard> userCardList = new ArrayList<>();
        List<UserCardDAO> userCardDAOList = new ArrayList<>();
        userCardList.add(userCard);
        userCardDAOList.add(userCardDAO);
        when(userCardRepository.findAll()).thenReturn(userCardDAOList);
        when(userCardMapStruct.userCardDAOToUserCard(userCardDAO)).thenReturn(userCard);

        ResponseEntity<List<UserCard>> responseEntity = userCardService.getUserCardList();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userCardList, responseEntity.getBody());
    }

    @Test
    public void getCardValueByIdTest_UserCardExist(){
        when(userCardRepository.findById("1")).thenReturn(Optional.of(userCardDAO));
        when(userCardMapStruct.userCardDAOToUserCard(userCardDAO)).thenReturn(userCard);
        when(cardRepository.findById("1")).thenReturn(Optional.of(cardDAO));

        ResponseEntity<Double> responseEntity = userCardService.getCardValueById("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(5.0, responseEntity.getBody());
    }
    @Test
    public void getCardValueByIdTest_UserCardNotExist(){
        when(userCardRepository.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<Double> responseEntity = userCardService.getCardValueById("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0.0, responseEntity.getBody());
    }

    @Test
    public void getCardAmountByIdTest_UserCardExist(){
        when(userCardRepository.findById("1")).thenReturn(Optional.of(userCardDAO));
        when(userCardMapStruct.userCardDAOToUserCard(userCardDAO)).thenReturn(userCard);

        ResponseEntity<Integer> responseEntity = userCardService.getCardAmountById("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody());

    }

    @Test
    public void getCardAmountByIdTest_UserCardNotExist(){
        when(userCardRepository.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<Integer> responseEntity = userCardService.getCardAmountById("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody());

    }
    @Test
    public void getUserCardByUserIdAndCardIdTest_ListNotEmpty(){
        List<UserCardDAO> userCardDAOList = new ArrayList<>();
        userCardDAOList.add(userCardDAO);
        when(userCardRepository.findByUserIdAndCardDAOId("1","1")).thenReturn(userCardDAOList);

        ResponseEntity<String> responseEntity = userCardService.getUserCardByUserIdAndCardId("1","1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("1", responseEntity.getBody());
    }

    @Test
    public void getUserCardByUserIdAndCardIdTest_ListEmpty(){
        List<UserCardDAO> userCardDAOList = new ArrayList<>();
        when(userCardRepository.findByUserIdAndCardDAOId("1","1")).thenReturn(userCardDAOList);

        ResponseEntity<String> responseEntity = userCardService.getUserCardByUserIdAndCardId("1","1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}