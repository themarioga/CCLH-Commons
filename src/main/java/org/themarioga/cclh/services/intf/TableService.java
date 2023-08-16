package org.themarioga.cclh.services.intf;

import org.themarioga.cclh.models.Table;

public interface TableService {

    Table create(Table table);

    Table update(Table table);

    void delete(Table table);

    void deleteById(long id);

    Table findOne(long id);

}
