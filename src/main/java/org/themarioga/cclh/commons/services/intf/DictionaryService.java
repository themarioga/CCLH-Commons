package org.themarioga.cclh.commons.services.intf;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.DictionaryCollaborator;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface DictionaryService {

	Dictionary create(String name, User creator);

	void setName(Dictionary dictionary, String newName);

    void delete(Dictionary dictionary);

	DictionaryCollaborator addCollaborator(Dictionary dictionary, User user);

	DictionaryCollaborator acceptCollaborator(Dictionary dictionary, User user);

	DictionaryCollaborator toggleCollaborator(Dictionary dictionary, User user);

	void removeCollaborator(Dictionary dictionary, User user);

    Dictionary findOne(long id);

	List<Dictionary> getDictionariesByCreator(User creator);

	List<Dictionary> getDictionariesByCreatorOrCollaborator(User creator);

    List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults);

    Long getDictionaryCount(User creator);

	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
	boolean isDictionaryCollaborator(Dictionary dictionary, User user);

	boolean isDictionaryActiveCollaborator(Dictionary dictionary, User user);

	Dictionary getDefaultDictionary();

	@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
	int getDictionaryMaxCollaborators();
}
