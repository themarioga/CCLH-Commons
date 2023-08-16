package org.themarioga.cclh.services.intf;

import org.themarioga.cclh.models.Card;
import org.themarioga.cclh.models.Player;

import java.util.List;

public interface CardService {

    Card create(Card card);

    Card update(Card card);

    void delete(Card card);

    void deleteById(long id);

    Card findOne(long id);

}
