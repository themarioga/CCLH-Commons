package org.themarioga.cclh.services.intf;

import org.themarioga.cclh.models.Game;

public interface GameService {

    Game create(Game game);

    Game update(Game game);

    void delete(Game game);

    void deleteById(long id);

    Game findOne(long id);

}
