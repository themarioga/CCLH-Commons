package org.themarioga.cclh.services.intf;

import org.themarioga.cclh.models.Player;

public interface PlayerService {

    Player create(Player player);

    Player update(Player player);

    void delete(Player player);

    void deleteById(long id);

    Player findOne(long id);

}
