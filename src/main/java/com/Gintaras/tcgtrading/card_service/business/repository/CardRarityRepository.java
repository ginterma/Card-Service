package com.Gintaras.tcgtrading.card_service.business.repository;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRarityRepository extends MongoRepository <CardRarityDAO, String> {
}
