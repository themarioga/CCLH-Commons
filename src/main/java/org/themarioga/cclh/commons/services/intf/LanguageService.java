package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Lang;

import java.util.List;

public interface LanguageService {

    Lang getLanguage(String id);

    Lang getDefaultLanguage();

    List<Lang> getLangs();

}
