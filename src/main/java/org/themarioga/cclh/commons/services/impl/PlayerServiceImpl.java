package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.themarioga.cclh.commons.dao.intf.PlayerDao;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.DictionaryService;
import org.themarioga.cclh.commons.services.intf.PlayerService;

import java.util.Date;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    PlayerDao playerDao;

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    ConfigurationService configurationService;

    @Override
    public Player create(Player player) {
        logger.debug("Creating player: {}", player);

        player.setCreationDate(new Date());
        return playerDao.create(player);
    }

    @Override
    public Player update(Player player) {
        logger.debug("Updating player: {}", player);

        return playerDao.update(player);
    }

    @Override
    public void delete(Player player) {
        logger.debug("Delete player: {}", player);

        playerDao.delete(player);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete player by ID: {}", id);

        playerDao.deleteById(id);
    }

    @Override
    public Player findOne(long id) {
        logger.debug("Getting player with ID: {}", id);

        return playerDao.findOne(id);
    }

    @Override
    public void transferCardsFromPlayerDeckToPlayerHand(Player player) {
        logger.debug("Transferring white cards from deck to hand from player {}", player);

        int cardsInHand = Integer.parseInt(configurationService.getConfiguration("game_whitecards_in_hand"));

        if (player.getHand().size() < cardsInHand) {
            int missingCards = cardsInHand - player.getHand().size();
            List<Card> cardsToTransfer = player.getDeck().subList(0, Math.min(missingCards, player.getDeck().size()));
            player.getHand().addAll(cardsToTransfer);
            player.getDeck().removeAll(cardsToTransfer);
            playerDao.update(player);
        }
    }

}
