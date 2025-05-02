package org.themarioga.game.cah.services.intf;

import org.themarioga.game.commons.models.Lang;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.models.DictionaryCollaborator;

import java.util.List;

public interface DictionaryService {

    Dictionary create(String name, User creator);

    void setLanguage(Dictionary dictionary, Lang lang);

    void togglePublished(Dictionary dictionary);

    void toggleShared(Dictionary dictionary);

    void delete(Dictionary dictionary);

    void setName(Dictionary dictionary, String newName);

    DictionaryCollaborator addCollaborator(Dictionary dictionary, User user);

    DictionaryCollaborator acceptCollaborator(Dictionary dictionary, User user);

    DictionaryCollaborator toggleCollaborator(Dictionary dictionary, User user);

    void removeCollaborator(Dictionary dictionary, User user);

    Dictionary getDictionaryById(long id);

    List<Dictionary> getDictionariesByCreator(User creator);

    List<Dictionary> getDictionariesByCreatorOrCollaborator(User creator);

    List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults);

    Long getDictionaryCount(User creator);

    boolean isDictionaryCollaborator(Dictionary dictionary, User user);

    boolean isDictionaryActiveCollaborator(Dictionary dictionary, User user);

    Dictionary getDefaultDictionary();

    int getDictionaryMaxCollaborators();
}
