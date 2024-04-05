package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.models.Lang;

import java.util.List;

public interface LanguageDao {

    Lang getLanguage(String id);

	boolean checkLanguageExists(String id);

	List<Lang> getLanguages();

}
