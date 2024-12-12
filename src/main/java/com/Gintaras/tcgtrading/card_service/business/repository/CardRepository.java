package com.Gintaras.tcgtrading.card_service.business.repository;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository <CardDAO, String> {
}
