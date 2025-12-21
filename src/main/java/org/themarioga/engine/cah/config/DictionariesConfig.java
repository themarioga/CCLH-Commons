package org.themarioga.engine.cah.config;

import org.springframework.context.annotation.Configuration;
import org.themarioga.engine.commons.services.intf.ConfigurationService;

@Configuration
public class DictionariesConfig {

	public static int MIN_NUMBER_OF_WHITE_CARDS = 405;
	public static int MAX_NUMBER_OF_WHITE_CARDS = 600;
	public static int MIN_NUMBER_OF_BLACK_CARDS = 50;
	public static int MAX_NUMBER_OF_BLACK_CARDS = 200;
	public static int MIN_WHITE_CARD_LENGTH = 2;
	public static int MAX_WHITE_CARD_LENGTH = 90;
	public static int MIN_BLACK_CARD_LENGTH = 2;
	public static int MAX_BLACK_CARD_LENGTH = 130;
	public static int MAX_NUMBER_OF_COLLABORATORS = 10;
	public static int MAX_NUMBER_OF_UNFINISHED_DICTIONARIES = 10;

	public DictionariesConfig(ConfigurationService configurationService) {
		MIN_NUMBER_OF_WHITE_CARDS = Integer.parseInt(configurationService.getConfiguration("dictionaries_min_whitecards"));
		MAX_NUMBER_OF_WHITE_CARDS = Integer.parseInt(configurationService.getConfiguration("dictionaries_max_whitecards"));
		MIN_NUMBER_OF_BLACK_CARDS = Integer.parseInt(configurationService.getConfiguration("dictionaries_min_blackcards"));
		MAX_NUMBER_OF_BLACK_CARDS = Integer.parseInt(configurationService.getConfiguration("dictionaries_max_blackcards"));
		MIN_WHITE_CARD_LENGTH = Integer.parseInt(configurationService.getConfiguration("dictionaries_min_whitecard_length"));
		MAX_WHITE_CARD_LENGTH = Integer.parseInt(configurationService.getConfiguration("dictionaries_max_whitecard_length"));
		MIN_BLACK_CARD_LENGTH = Integer.parseInt(configurationService.getConfiguration("dictionaries_min_blackcard_length"));
		MAX_BLACK_CARD_LENGTH = Integer.parseInt(configurationService.getConfiguration("dictionaries_max_blackcard_length"));
		MAX_NUMBER_OF_COLLABORATORS = Integer.parseInt(configurationService.getConfiguration("dictionaries_max_collaborators"));
		MAX_NUMBER_OF_UNFINISHED_DICTIONARIES = Integer.parseInt(configurationService.getConfiguration("dictionaries_max_unfinished_number"));
	}

}
