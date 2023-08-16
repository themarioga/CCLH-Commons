package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Table;

public interface TableService {

    Table create(Table table);

    Table update(Table table);

    void delete(Table table);

    void deleteById(long id);

    Table findOne(long id);

}
