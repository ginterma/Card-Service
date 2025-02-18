package com.Gintaras.tcgtrading.card_service.ControllerTest;

import com.Gintaras.tcgtrading.card_service.business.Controller.CardRarityController;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.business.service.CardRarityService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(CardRarityController.class)
public class CardRarityControllerTest {

    private final String RARITY_URI = "/api/v1/card/rarity";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CardRarityService cardRarityService;


    private CardRarity cardRarity;
    private CardRarityDAO cardRarityDAO;

    @BeforeEach
    public void setUp() {
        cardRarity = new CardRarity("1", "Rare");
        cardRarityDAO = new CardRarityDAO("1", "Rare");
    }

    @Test
    public void saveCardRarityControllerTest_RequestBodyValid() throws Exception {
        when(cardRarityService.saveCardRarity(cardRarity)).thenReturn(ResponseEntity.status(HttpStatus.OK).body(cardRarity));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(RARITY_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(cardRarity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.rarityName").value("Rare"));
    }

    @Test
    public void saveCardRarityControllerTest_RequestBodyNotValid() throws Exception {
        when(cardRarityService.saveCardRarity(cardRarity)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(RARITY_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(cardRarity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getCardRarityByIdControllerTest_CardRarityExist() throws Exception {
        when(cardRarityService.getCardRarityById("1")).thenReturn(ResponseEntity.status(HttpStatus.OK).body(cardRarity));

            ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                            .get(RARITY_URI + "/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.rarityName").value("Rare"));
        }

    @Test
    public void getCardRarityByIdControllerTest_CardRarityNotExist() throws Exception {
        when(cardRarityService.getCardRarityById("1")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .get(RARITY_URI + "/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getCardRarityListControllerTest() throws Exception {
        List<CardRarity> cardRarityList = new ArrayList<>();
        cardRarityList.add(cardRarity);
        when(cardRarityService.getCardRarityList()).thenReturn(ResponseEntity.status(HttpStatus.OK).body(cardRarityList));
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .get(RARITY_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].rarityName").value("Rare"));
    }

    @Test
    public void deleteCardRarityByIdControllerTest_CardRarityExist() throws Exception {
        when(cardRarityService.deleteCardRarityById("1")).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .delete(RARITY_URI + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCardRarityByIdControllerTest_CardRarityNotExist() throws Exception {
        when(cardRarityService.deleteCardRarityById("1")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .delete(RARITY_URI + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCardRarityByIdControllerTest_ValidBody() throws Exception {
        when(cardRarityService.getCardRarityById("1")).thenReturn(ResponseEntity.status(HttpStatus.OK).body(cardRarity));
        when(cardRarityService.saveCardRarity(cardRarity)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(cardRarity));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(RARITY_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(cardRarity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.rarityName").value("Rare"));

    }
    @Test
    public void updateCardRarityByIdControllerTest_NotFound() throws Exception {
        when(cardRarityService.getCardRarityById("1")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(RARITY_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(cardRarity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void updateCardRarityByIdControllerTest_IdsNotMatch() throws Exception {

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(RARITY_URI + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(cardRarity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCardRarityByIdControllerTest_BodyNotValid() throws Exception {

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(RARITY_URI + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



    private static String JsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

