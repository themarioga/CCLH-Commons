package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Game;

public interface GameService {

    Game create(Game game);

    Game update(Game game);

    void delete(Game game);

    void deleteById(long id);

    Game findOne(long id);

}
