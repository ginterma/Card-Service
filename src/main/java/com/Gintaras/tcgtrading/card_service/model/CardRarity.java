package com.Gintaras.tcgtrading.card_service.model;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRarity {


    @ApiModelProperty(notes = "Unique id of Card Rarity")
    private String id;

    @ApiModelProperty(notes = "Card rarity name")
    private String rarityName;
}
