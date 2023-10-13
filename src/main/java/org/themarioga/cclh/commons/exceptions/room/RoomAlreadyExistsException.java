package org.themarioga.cclh.commons.exceptions.room;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class RoomAlreadyExistsException extends ApplicationException {

    private final long id;

    public RoomAlreadyExistsException(long id) {
        super(ErrorEnum.ROOM_ALREADY_EXISTS);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
