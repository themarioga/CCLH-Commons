package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Card;

public interface CardService {

    Card create(Card card);

    Card update(Card card);

    void delete(Card card);

    void deleteById(long id);

    Card findOne(long id);

}
