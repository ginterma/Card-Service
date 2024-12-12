package com.Gintaras.tcgtrading.card_service.model;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCard {

    @ApiModelProperty(notes = "Unique id of Card possession")
    private String id;

    @ApiModelProperty(notes = "Id of User this card belongs too")
    private String userId;

    @ApiModelProperty(notes = "Id of the card")
    private String cardId;

    @ApiModelProperty(notes = "Id of User this card belongs too")
    private Integer amount;

}
