package com.Gintaras.tcgtrading.card_service.model;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @ApiModelProperty(notes = "Unique id of Card")
    private String id;

    @ApiModelProperty(notes = "Card Rarity Id")
    private String cardRarityId;

    @ApiModelProperty(notes = "Card name")
    private String name;

    @ApiModelProperty(notes = "Card price")
    private Double price;

}
