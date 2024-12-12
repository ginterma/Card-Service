package com.Gintaras.tcgtrading.card_service.business.repository;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.UserCardDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

    public interface UserCardRepository extends MongoRepository <UserCardDAO, String> {
}
