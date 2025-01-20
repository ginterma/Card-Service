package com.Gintaras.tcgtrading.card_service.business.repository;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends MongoRepository <CardDAO, String> {

}
