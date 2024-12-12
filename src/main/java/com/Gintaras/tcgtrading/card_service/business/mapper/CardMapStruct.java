package com.Gintaras.tcgtrading.card_service.business.mapper;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = CardRarityMapStruct.class)
public interface CardMapStruct {

    @Mapping(source = "cardRarityId", target = "cardRarityDAO", qualifiedByName = "cardRarityIdToCardRarityDAO")
    CardDAO CardToCardDAO (Card card);

    @Mapping(source = "cardRarityDAO.id", target = "cardRarityId")
    Card CardDAOToCard (CardDAO cardDAO);

    @Named("cardRarityIdToCardRarityDAO")
    default CardRarityDAO cardRarityIdToCardRarityDAO(String cardRarityId) {
        if (cardRarityId == null) {
            return null;
        }
        CardRarityDAO cardRarityDAO = new CardRarityDAO();
        cardRarityDAO.setId(cardRarityId);
        return cardRarityDAO;
    }
}
