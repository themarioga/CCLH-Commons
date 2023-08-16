package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Player;

public interface PlayerService {

    Player create(Player player);

    Player update(Player player);

    void delete(Player player);

    void deleteById(long id);

    Player findOne(long id);

}
