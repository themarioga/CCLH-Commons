package org.themarioga.engine.cah.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "cah.dictionaries")
@Validated
public class DictionariesConfig {

    private Integer minNumberOfWhiteCards;
    private Integer maxNumberOfWhiteCards;
    private Integer minNumberOfBlackCards;
    private Integer maxNumberOfBlackCards;
    private Integer minWhiteCardLength;
    private Integer maxWhiteCardLength;
    private Integer minBlackCardLength;
    private Integer maxBlackCardLength;
    private Integer maxNumberOfCollaborators;
    private Integer maxNumberOfUnfinishedDictionaries;

    public Integer getMaxBlackCardLength() {
        return maxBlackCardLength;
    }

    public void setMaxBlackCardLength(Integer maxBlackCardLength) {
        this.maxBlackCardLength = maxBlackCardLength;
    }

    public Integer getMaxNumberOfBlackCards() {
        return maxNumberOfBlackCards;
    }

    public void setMaxNumberOfBlackCards(Integer maxNumberOfBlackCards) {
        this.maxNumberOfBlackCards = maxNumberOfBlackCards;
    }

    public Integer getMaxNumberOfCollaborators() {
        return maxNumberOfCollaborators;
    }

    public void setMaxNumberOfCollaborators(Integer maxNumberOfCollaborators) {
        this.maxNumberOfCollaborators = maxNumberOfCollaborators;
    }

    public Integer getMaxNumberOfUnfinishedDictionaries() {
        return maxNumberOfUnfinishedDictionaries;
    }

    public void setMaxNumberOfUnfinishedDictionaries(Integer maxNumberOfUnfinishedDictionaries) {
        this.maxNumberOfUnfinishedDictionaries = maxNumberOfUnfinishedDictionaries;
    }

    public Integer getMaxNumberOfWhiteCards() {
        return maxNumberOfWhiteCards;
    }

    public void setMaxNumberOfWhiteCards(Integer maxNumberOfWhiteCards) {
        this.maxNumberOfWhiteCards = maxNumberOfWhiteCards;
    }

    public Integer getMaxWhiteCardLength() {
        return maxWhiteCardLength;
    }

    public void setMaxWhiteCardLength(Integer maxWhiteCardLength) {
        this.maxWhiteCardLength = maxWhiteCardLength;
    }

    public Integer getMinBlackCardLength() {
        return minBlackCardLength;
    }

    public void setMinBlackCardLength(Integer minBlackCardLength) {
        this.minBlackCardLength = minBlackCardLength;
    }

    public Integer getMinNumberOfBlackCards() {
        return minNumberOfBlackCards;
    }

    public void setMinNumberOfBlackCards(Integer minNumberOfBlackCards) {
        this.minNumberOfBlackCards = minNumberOfBlackCards;
    }

    public Integer getMinNumberOfWhiteCards() {
        return minNumberOfWhiteCards;
    }

    public void setMinNumberOfWhiteCards(Integer minNumberOfWhiteCards) {
        this.minNumberOfWhiteCards = minNumberOfWhiteCards;
    }

    public Integer getMinWhiteCardLength() {
        return minWhiteCardLength;
    }

    public void setMinWhiteCardLength(Integer minWhiteCardLength) {
        this.minWhiteCardLength = minWhiteCardLength;
    }

}
