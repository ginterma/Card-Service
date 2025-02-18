package com.Gintaras.tcgtrading.card_service.ControllerTest;

import com.Gintaras.tcgtrading.card_service.business.Controller.CardController;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.business.service.CardService;
import com.Gintaras.tcgtrading.card_service.model.Card;
import com.Gintaras.tcgtrading.card_service.model.CardRarity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CardController.class)
public class CardControllerTest {

    private final String CARD_URI = "/api/v1/card";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CardService cardService;


    private Card card;
    private CardDAO cardDAO;
    private CardRarity cardRarity;
    private CardRarityDAO cardRarityDAO;

    @BeforeEach
    public void setUp() {
        cardRarity = new CardRarity("1", "Rare");
        cardRarityDAO = new CardRarityDAO("1", "Rare");
        card = new Card("1", "1", "Best card", 5.0);
        cardDAO = new CardDAO("1", cardRarityDAO, "Best card", 5.0);

    }

    @Test
    public void saveCardControllerTest_CardIsValid() throws Exception {
        when(cardService.saveCard(card)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(card));
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(CARD_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(card))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.cardRarityId").value("1"));
    }
    @Test
    public void saveCardControllerTest_CardIsNotValid() throws Exception {
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(CARD_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void saveCardControllerTest_CardRarityNotExist() throws Exception {
        when(cardService.saveCard(card)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(CARD_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(card))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCardControllerTest_Successful() throws Exception {
        when(cardService.getCardById("1")).thenReturn(ResponseEntity.status(HttpStatus.OK).body(card));
        when(cardService.saveCard(card)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(card));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(CARD_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(card))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    @Test
    public void updateCardControllerTest_IdNotMatch() throws Exception {

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(CARD_URI + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(card))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
//    @Test
//    public void updateCardControllerTest_BadRequest() throws Exception {
//        when(cardService.getCardById("1")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
//
//        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
//                        .put(CARD_URI + "/2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JsonString(null))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    public void updateCardControllerTest_CardNotExist() throws Exception {
//
//        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
//                        .put(CARD_URI + "/2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JsonString(null))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }


    private static String JsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}