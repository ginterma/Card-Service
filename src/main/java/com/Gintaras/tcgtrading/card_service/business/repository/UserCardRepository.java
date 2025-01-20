package com.Gintaras.tcgtrading.card_service.business.repository;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.UserCardDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserCardRepository extends MongoRepository <UserCardDAO, String> {

        List<UserCardDAO> findByUserIdAndCardDAO_Id(String userId, String cardId);
}
