package org.themarioga.engine.cah.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.themarioga.engine.cah.enums.PunctuationModeEnum;
import org.themarioga.engine.cah.enums.VotationModeEnum;

import java.util.UUID;

@Configuration
@ConfigurationProperties(prefix = "cah.game")
@Validated
public class GameConfig {

    private VotationModeEnum defaultVotationMode;
    private PunctuationModeEnum defaultPunctuationMode;
    private Integer defaultNumberOfPointsToWin;
    private Integer defaultNumberOfRoundsToEnd;
    private Integer defaultMinNumberOfPlayers;
    private Integer defaultMaxNumberOfPlayers;
    private UUID defaultDictionaryId;
    private Integer numberOfCardsInHand;

    public PunctuationModeEnum getDefaultPunctuationMode() {
        return defaultPunctuationMode;
    }

    public void setDefaultPunctuationMode(PunctuationModeEnum defaultPunctuationMode) {
        this.defaultPunctuationMode = defaultPunctuationMode;
    }

    public VotationModeEnum getDefaultVotationMode() {
        return defaultVotationMode;
    }

    public void setDefaultVotationMode(VotationModeEnum defaultVotationMode) {
        this.defaultVotationMode = defaultVotationMode;
    }

    public Integer getDefaultMaxNumberOfPlayers() {
        return defaultMaxNumberOfPlayers;
    }

    public void setDefaultMaxNumberOfPlayers(Integer defaultMaxNumberOfPlayers) {
        this.defaultMaxNumberOfPlayers = defaultMaxNumberOfPlayers;
    }

    public Integer getDefaultMinNumberOfPlayers() {
        return defaultMinNumberOfPlayers;
    }

    public void setDefaultMinNumberOfPlayers(Integer defaultMinNumberOfPlayers) {
        this.defaultMinNumberOfPlayers = defaultMinNumberOfPlayers;
    }

    public UUID getDefaultDictionaryId() {
        return defaultDictionaryId;
    }

    public void setDefaultDictionaryId(UUID defaultDictionaryId) {
        this.defaultDictionaryId = defaultDictionaryId;
    }

    public Integer getDefaultNumberOfPointsToWin() {
        return defaultNumberOfPointsToWin;
    }

    public void setDefaultNumberOfPointsToWin(Integer defaultNumberOfPointsToWin) {
        this.defaultNumberOfPointsToWin = defaultNumberOfPointsToWin;
    }

    public Integer getDefaultNumberOfRoundsToEnd() {
        return defaultNumberOfRoundsToEnd;
    }

    public void setDefaultNumberOfRoundsToEnd(Integer defaultNumberOfRoundsToEnd) {
        this.defaultNumberOfRoundsToEnd = defaultNumberOfRoundsToEnd;
    }

    public Integer getNumberOfCardsInHand() {
        return numberOfCardsInHand;
    }

    public void setNumberOfCardsInHand(Integer numberOfCardsInHand) {
        this.numberOfCardsInHand = numberOfCardsInHand;
    }
}
