package org.themarioga.engine.cah.services.impl.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.engine.cah.dao.intf.game.RoundDao;
import org.themarioga.engine.cah.enums.CardTypeEnum;
import org.themarioga.engine.cah.enums.RoundStatusEnum;
import org.themarioga.engine.cah.enums.VotationModeEnum;
import org.themarioga.engine.cah.exceptions.card.CardAlreadyPlayedException;
import org.themarioga.engine.cah.exceptions.card.CardDoesntExistsException;
import org.themarioga.engine.cah.exceptions.card.CardNotPlayedException;
import org.themarioga.engine.cah.exceptions.round.RoundWrongStatusException;
import org.themarioga.engine.cah.exceptions.player.PlayerAlreadyPlayedCardException;
import org.themarioga.engine.cah.exceptions.player.PlayerAlreadyVotedCardException;
import org.themarioga.engine.cah.exceptions.player.PlayerCannotVoteCardException;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.game.*;
import org.themarioga.engine.cah.services.intf.game.PlayerService;
import org.themarioga.engine.cah.services.intf.game.RoundService;
import org.themarioga.engine.commons.enums.ErrorEnum;
import org.themarioga.engine.commons.exceptions.ApplicationException;
import org.themarioga.engine.commons.util.Assert;

import java.util.Date;

@Service
public class RoundServiceImpl implements RoundService {

    private final Logger logger = LoggerFactory.getLogger(RoundServiceImpl.class);

    private final RoundDao roundDao;
    private final PlayerService playerService;

    @Autowired
    public RoundServiceImpl(RoundDao roundDao, PlayerService playerService) {
        this.roundDao = roundDao;
        this.playerService = playerService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Round createRound(Game game, int roundNumber) {
        logger.debug("Creating round for game {}", game);

        // Check round exists
        Assert.assertNotNull(game, ErrorEnum.GAME_NOT_FOUND);

        // Create the round object
        Round round = new Round();
        round.setGame(game);
        round.setRoundNumber(roundNumber);
        round.setStatus(RoundStatusEnum.PLAYING);
        round.setCreationDate(new Date());

        // Set current black card
        round.setRoundBlackCard(getBlackCardFromGameDeck(game));

        // Set current president if needed
        if (game.getVotationMode() == VotationModeEnum.DICTATORSHIP) {
            round.setRoundPresident(playerService.findPlayerByGameAndUser(game, game.getCreator()));
        } else if (round.getGame().getVotationMode() == VotationModeEnum.CLASSIC) {
            round.setRoundPresident(getPresidentForNextRound(round));
        }

        return roundDao.createOrUpdate(round);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void deleteRound(Round round) {
        logger.debug("Deleting round {}", round);

        // Check round exists
        Assert.assertNotNull(round, ErrorEnum.GAME_NOT_FOUND);

        // Check if the round is ready to end
        if (round.getStatus() != RoundStatusEnum.ENDING)
            throw new RoundWrongStatusException();

        round.getGame().setCurrentRound(null);
        roundDao.delete(round);
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
	public Round setStatus(Round round, RoundStatusEnum status) {
		logger.debug("Setting round {} to status {}", round, status);

		// Check round exists
		Assert.assertNotNull(round, ErrorEnum.GAME_NOT_FOUND);

		// Set status
		round.setStatus(status);

		return roundDao.createOrUpdate(round);
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Round addCardToPlayedCards(Round round, Player player, Card card) {
        logger.debug("Player {} playing card {} for the round {}", player, card, round);

        // Check round exists
        Assert.assertNotNull(round, ErrorEnum.ROUND_NOT_STARTED);

        // Check if the round is ready to start
        if (round.getStatus() != RoundStatusEnum.PLAYING)
            throw new RoundWrongStatusException();

        // Check if the player already played
        if (round.getPlayedCards().stream().anyMatch(playedCard -> playedCard.getPlayer().getId().equals(player.getId())))
            throw new PlayerAlreadyPlayedCardException();

        // Check if the card was already played
        if (round.getPlayedCards().stream().anyMatch(playedCard -> playedCard.getCard().getId().equals(card.getId())))
            throw new CardAlreadyPlayedException();

        // Set the played card
        PlayedCard playedCard = new PlayedCard();
        playedCard.setRound(round);
        playedCard.setPlayer(player);
        playedCard.setCard(card);
        round.getPlayedCards().add(playedCard);

        return roundDao.createOrUpdate(round);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Round voteCard(Round round, Player player, Card card) {
        logger.debug("Player {} voting card {} for the round {}", player, card, round);

        // Check round exists
        Assert.assertNotNull(round, ErrorEnum.GAME_NOT_FOUND);

        // Check if the round is ready to start
        if (round.getStatus() != RoundStatusEnum.VOTING)
            throw new RoundWrongStatusException();

        // Check if the player can vote
        if ((round.getGame().getVotationMode().equals(VotationModeEnum.DICTATORSHIP) || round.getGame().getVotationMode().equals(VotationModeEnum.CLASSIC)) && !player.getId().equals(round.getRoundPresident().getId()))
            throw new PlayerCannotVoteCardException();

        // Check if the player already voted
        if (round.getVotedCards().stream().anyMatch(votedCard -> votedCard.getPlayer().getId().equals(player.getId())))
            throw new PlayerAlreadyVotedCardException();

        // Check if the card have been played this round
        if (round.getPlayedCards().stream().noneMatch(playedCard -> playedCard.getCard().getId().equals(card.getId())))
            throw new CardNotPlayedException();

        // Set the player vote
        VotedCard votedCard = new VotedCard();
        votedCard.setRound(round);
        votedCard.setPlayer(player);
        votedCard.setCard(card);
        round.getVotedCards().add(votedCard);

        return roundDao.createOrUpdate(round);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Round setNextBlackCard(Round round, Card nextBlackCard) {
        logger.debug("Setting next black card to round {}", round);

		// Check the card type is black
		if (nextBlackCard.getType() != CardTypeEnum.BLACK)
			throw new CardDoesntExistsException();

		// Set the round black card
        round.setRoundBlackCard(nextBlackCard);

		return roundDao.createOrUpdate(round);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public VotedCard getMostVotedCard(Round round) {
        logger.debug("Getting most voted card of the game of the round {}", round);

        return roundDao.getMostVotedCard(round);
    }

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
	public PlayedCard getPlayedCardByCard(Round round, Card card) {
		logger.debug("Getting played card from round {} and card {}", round, card);

		return roundDao.getPlayedCardByCard(round, card);
	}

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public boolean checkIfEveryoneHavePlayedACard(Round round) {
        int cardsNeededToVote = round.getGame().getPlayers().size();
        if (round.getGame().getVotationMode() == VotationModeEnum.DICTATORSHIP || round.getGame().getVotationMode() == VotationModeEnum.CLASSIC)
            cardsNeededToVote--;

        return roundDao.countPlayedCards(round) == cardsNeededToVote;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public boolean checkIfEveryoneHaveVotedACard(Round round) {
        int votesNeededToEnd = round.getGame().getPlayers().size();
        if (round.getGame().getVotationMode() == VotationModeEnum.DICTATORSHIP || round.getGame().getVotationMode() == VotationModeEnum.CLASSIC)
            votesNeededToEnd = 1;

        return roundDao.countVotedCards(round) == votesNeededToEnd;
    }

    private Card getBlackCardFromGameDeck(Game game) {
        logger.debug("Getting black card from deck to game {}", game);

        Card nextBlackCard = game.getBlackCardsDeck().get(0);
        game.getBlackCardsDeck().remove(nextBlackCard);

        return nextBlackCard;
    }

    private Player getPresidentForNextRound(Round round) {
        int playerIndex = 0;

        for (int i = 0; i < round.getGame().getPlayers().size(); i++) {
            if (round.getGame().getPlayers().get(i).equals(round.getRoundPresident()))
                playerIndex = i;
        }

        if (playerIndex + 1 < round.getGame().getPlayers().size()) {
            playerIndex += 1;
        } else {
            playerIndex = 0;
        }

        return round.getGame().getPlayers().get(playerIndex);
    }

}
