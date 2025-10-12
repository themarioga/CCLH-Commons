package org.themarioga.game.cah.services.intf;

import org.themarioga.game.commons.models.Lang;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.cah.models.Dictionary;
import org.themarioga.game.cah.models.DictionaryCollaborator;

import java.util.List;
import java.util.UUID;

public interface DictionaryService {

    Dictionary create(String name, User creator);

    Dictionary setName(Dictionary dictionary, String newName);

    Dictionary setLanguage(Dictionary dictionary, Lang lang);

    Dictionary togglePublished(Dictionary dictionary);

    Dictionary toggleShared(Dictionary dictionary);

    void delete(Dictionary dictionary);

    DictionaryCollaborator addCollaborator(Dictionary dictionary, User user);

    DictionaryCollaborator acceptCollaborator(Dictionary dictionary, User user);

    DictionaryCollaborator toggleCollaborator(Dictionary dictionary, User user);

    void removeCollaborator(Dictionary dictionary, User user);

    Dictionary getDictionaryById(UUID id);

    List<Dictionary> getDictionariesByCreator(User creator);

    List<Dictionary> getDictionariesByCreatorOrCollaborator(User creator);

    List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults);

    Long getDictionaryCount(User creator);

    boolean isDictionaryCollaborator(Dictionary dictionary, User user);

    boolean isDictionaryActiveCollaborator(Dictionary dictionary, User user);

    Dictionary getDefaultDictionary();
}
