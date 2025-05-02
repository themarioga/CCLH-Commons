package org.themarioga.game.cah.dao.intf;

import org.themarioga.game.commons.dao.InterfaceHibernateDao;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.cah.models.Dictionary;

import java.util.List;

public interface DictionaryDao extends InterfaceHibernateDao<Dictionary> {

    List<Dictionary> getDictionariesByCreator(User user);

    List<Dictionary> getDictionariesByCreatorOrCollaborator(User creator);

    List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults);

    Long getDictionaryCount(User creator);

    Long countDictionariesByName(String name);

    boolean isDictionaryCollaborator(Dictionary dictionary, User user);

    boolean isDictionaryActiveCollaborator(Dictionary dictionary, User user);

}
