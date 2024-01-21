package org.themarioga.cclh.commons.exceptions.table;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class TableWrongStatusException extends ApplicationException {

    public TableWrongStatusException() {
        super(ErrorEnum.TABLE_WRONG_STATUS);
    }

}
