package org.themarioga.cclh.services.intf;

import org.themarioga.cclh.models.Dictionary;

public interface DictionaryService {

    Dictionary create(Dictionary dictionary);

    Dictionary update(Dictionary dictionary);

    void delete(Dictionary dictionary);

    void deleteById(long id);

    Dictionary findOne(long id);

}
