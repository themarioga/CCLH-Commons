package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface DictionaryService {

	Dictionary create(String name, User creator);

	void setName(Dictionary dictionary, String newName);

    void delete(Dictionary dictionary);

    Dictionary findOne(long id);

    List<Dictionary> getUserDictionaries(User creator);

    List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults);

    Long getDictionaryCount(User creator);

    Dictionary getDefaultDictionary();
}
