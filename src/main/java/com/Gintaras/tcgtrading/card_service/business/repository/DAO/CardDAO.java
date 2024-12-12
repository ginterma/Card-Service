package com.Gintaras.tcgtrading.card_service.business.repository.DAO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDAO {

    @Id
    private String id;

    @DBRef
    private CardRarityDAO cardRarityDAO;

    @ApiModelProperty(notes = "Card name")
    private String name;

    @ApiModelProperty(notes = "Card price")
    private Double price;
}
