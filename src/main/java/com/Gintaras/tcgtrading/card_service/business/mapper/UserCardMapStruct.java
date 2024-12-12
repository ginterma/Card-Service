package com.Gintaras.tcgtrading.card_service.business.mapper;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardDAO;
import com.Gintaras.tcgtrading.card_service.business.repository.DAO.UserCardDAO;
import com.Gintaras.tcgtrading.card_service.model.UserCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = CardMapStruct.class)
public interface UserCardMapStruct {

    @Mapping(source = "cardId", target = "cardDAO", qualifiedByName = "cardIdToCardDAO")
    UserCardDAO UserCardToUserCardDAO(UserCard userCard);

    @Mapping(source = "cardDAO.id" , target = "cardId")
    UserCard userCardDAOToUserCard (UserCardDAO userCardDAO);

    @Named("cardIdToCardDAO")
    default CardDAO cardIdToCardDAO (String cardId){
        if (cardId == null) {
            return null;
        }
        CardDAO cardDAO = new CardDAO();
        cardDAO.setId(cardId);
        return cardDAO;

    }
}
