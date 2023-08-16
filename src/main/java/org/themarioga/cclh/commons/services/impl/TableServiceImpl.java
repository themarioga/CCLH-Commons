package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.themarioga.cclh.commons.daos.intf.TableDao;
import org.themarioga.cclh.commons.models.Table;
import org.themarioga.cclh.commons.services.intf.TableService;

public class TableServiceImpl implements TableService {

    private Logger logger = LoggerFactory.getLogger(TableService.class);

    @Autowired
    TableDao tableDao;

    @Override
    public Table create(Table table) {
        logger.debug("Creating table: {}", table);

        return tableDao.create(table);
    }

    @Override
    public Table update(Table table) {
        logger.debug("Updating table: {}", table);

        return tableDao.update(table);
    }

    @Override
    public void delete(Table table) {
        logger.debug("Delete table: {}", table);

        tableDao.delete(table);
    }

    @Override
    public void deleteById(long id) {
        logger.debug("Delete table by ID: {}", id);

        tableDao.deleteById(id);
    }

    @Override
    public Table findOne(long id) {
        logger.debug("Getting table with ID: {}", id);

        return tableDao.findOne(id);
    }

}
