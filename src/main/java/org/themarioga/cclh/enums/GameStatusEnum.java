package org.themarioga.cclh.enums;

public enum GameStatusEnum {

    CREATED(1), CONFIGURED(2), STARTED(3);

    private int status;

    GameStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
