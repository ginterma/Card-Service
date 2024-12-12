package com.Gintaras.tcgtrading.card_service.business.mapper;

import com.Gintaras.tcgtrading.card_service.business.repository.DAO.CardRarityDAO;
import com.Gintaras.tcgtrading.card_service.model.CardRarity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardRarityMapStruct {

    CardRarity cardRarityDAOToCardRarity (CardRarityDAO cardRarityDAO);

    CardRarityDAO cardRarityToCardRarityDAO (CardRarity cardRarity);
}
