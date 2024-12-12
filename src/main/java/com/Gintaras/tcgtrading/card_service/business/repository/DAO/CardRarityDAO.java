package com.Gintaras.tcgtrading.card_service.business.repository.DAO;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Card_Rarity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRarityDAO {

    @Id
    private String id;

    private String rarityName;
}
