package org.themarioga.cclh.commons.services.intf;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface DictionaryService {

	Dictionary create(String name, User creator);

	void setName(Dictionary dictionary, String newName);

    void delete(Dictionary dictionary);

    Dictionary findOne(long id);

	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
	List<Dictionary> getDictionariesByCreator(User creator);

	List<Dictionary> getDictionariesByCreatorOrCollaborator(User creator);

    List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults);

    Long getDictionaryCount(User creator);

    Dictionary getDefaultDictionary();
}
