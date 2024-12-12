package com.Gintaras.tcgtrading.card_service.business.repository.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User_Card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCardDAO {

    @Id
    private String id;

    private String userId;

    @DBRef
    private CardDAO cardDAO;

    private Integer amount;
}
