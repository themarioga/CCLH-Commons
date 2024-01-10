package org.themarioga.cclh.commons.exceptions.table;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class TableWrongStatusException extends ApplicationException {

    private final Long gameId;

    public TableWrongStatusException(long gameId) {
        super(ErrorEnum.TABLE_WRONG_STATUS);

        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }

}
