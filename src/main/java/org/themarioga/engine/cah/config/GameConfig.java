package org.themarioga.engine.cah.config;

import org.springframework.context.annotation.Configuration;
import org.themarioga.engine.cah.enums.PunctuationModeEnum;
import org.themarioga.engine.cah.enums.VotationModeEnum;
import org.themarioga.engine.commons.services.intf.ConfigurationService;

@Configuration
public class GameConfig {

	public static VotationModeEnum DEFAULT_VOTATION_MODE = VotationModeEnum.DEMOCRACY;
	public static PunctuationModeEnum DEFAULT_PUNCTUATION_MODE = PunctuationModeEnum.POINTS;
	public static int DEFAULT_NUMBER_OF_POINTS_TO_WIN = 5;
	public static int DEFAULT_NUMBER_OF_ROUNDS = 5;
	public static int DEFAULT_MIN_NUMBER_OF_PLAYERS = 3;
	public static int DEFAULT_MAX_NUMBER_OF_PLAYERS = 9;
	public static int NUMBER_OF_CARDS_IN_HAND = 5;

	public GameConfig(ConfigurationService configurationService) {
		DEFAULT_VOTATION_MODE = VotationModeEnum.getEnum(Integer.parseInt(configurationService.getConfiguration("game_default_game_type")));
		DEFAULT_PUNCTUATION_MODE = PunctuationModeEnum.getEnum(Integer.parseInt(configurationService.getConfiguration("game_default_game_punctuation_type")));
		DEFAULT_NUMBER_OF_POINTS_TO_WIN = Integer.parseInt(configurationService.getConfiguration("game_default_number_of_points_to_win"));
		DEFAULT_NUMBER_OF_ROUNDS = Integer.parseInt(configurationService.getConfiguration("game_default_number_of_rounds"));
		DEFAULT_MIN_NUMBER_OF_PLAYERS = Integer.parseInt(configurationService.getConfiguration("game_min_number_of_players"));
		DEFAULT_MAX_NUMBER_OF_PLAYERS = Integer.parseInt(configurationService.getConfiguration("game_max_number_of_players"));
		NUMBER_OF_CARDS_IN_HAND = Integer.parseInt(configurationService.getConfiguration("game_default_number_cards_in_hand"));
	}

}
