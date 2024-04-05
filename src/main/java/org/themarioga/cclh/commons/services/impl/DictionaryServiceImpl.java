package org.themarioga.cclh.commons.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.themarioga.cclh.commons.dao.intf.DictionaryDao;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.exceptions.dictionary.*;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.DictionaryCollaborator;
import org.themarioga.cclh.commons.models.Lang;
import org.themarioga.cclh.commons.models.User;
import org.themarioga.cclh.commons.services.intf.CardService;
import org.themarioga.cclh.commons.services.intf.ConfigurationService;
import org.themarioga.cclh.commons.services.intf.DictionaryService;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final Logger logger = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    private final DictionaryDao dictionaryDao;
    private final ConfigurationService configurationService;
    private final CardService cardService;

    @Autowired
    public DictionaryServiceImpl(DictionaryDao dictionaryDao, ConfigurationService configurationService, CardService cardService) {
        this.dictionaryDao = dictionaryDao;
        this.configurationService = configurationService;
        this.cardService = cardService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public Dictionary create(String name, User creator) {
        logger.debug("Creating dictionary: {}", name);

        if (dictionaryDao.countDictionariesByName(name) > 0)
            throw new DictionaryAlreadyExistsException();

        Dictionary dictionary = new Dictionary();
        dictionary.setName(name);
        dictionary.setCreator(creator);
        dictionary.setLang(creator.getLang());
        dictionary.setPublished(false);
        dictionary.setShared(false);
        dictionary.setCreationDate(new Date());

        DictionaryCollaborator dictionaryCollaborator = new DictionaryCollaborator();
        dictionaryCollaborator.setDictionary(dictionary);
        dictionaryCollaborator.setUser(creator);
        dictionaryCollaborator.setAccepted(true);
        dictionaryCollaborator.setCanEdit(true);
        dictionary.getCollaborators().add(dictionaryCollaborator);

        return dictionaryDao.create(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void setName(Dictionary dictionary, String newName) {
        logger.debug("Setting name {} to dictionary {}", newName, dictionary);

        if (dictionaryDao.countDictionariesByName(newName) > 0)
            throw new DictionaryAlreadyExistsException();

        dictionary.setName(newName);

        dictionaryDao.update(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void setLanguage(Dictionary dictionary, Lang lang) {
        logger.debug("Setting lang {} to dictionary {}", lang, dictionary);

        dictionary.setLang(lang);

        dictionaryDao.update(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void togglePublished(Dictionary dictionary) {
        logger.debug("Toggling published to dictionary {}", dictionary);

        if (Boolean.TRUE.equals(dictionary.getShared()))
            throw new DictionaryAlreadySharedException();

        if (!cardService.checkDictionaryCanBePublished(dictionary))
            throw new DictionaryNotCompletedException();

        dictionary.setPublished(!dictionary.getPublished());

        dictionaryDao.update(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void toggleShared(Dictionary dictionary) {
        logger.debug("Toggling shared to dictionary {}", dictionary);

        if (Boolean.FALSE.equals(dictionary.getPublished()))
            throw new DictionaryNotPublishedException();

        dictionary.setShared(!dictionary.getShared());

        dictionaryDao.update(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void delete(Dictionary dictionary) {
        logger.debug("Delete dictionary: {}", dictionary);

        dictionaryDao.delete(dictionary);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public DictionaryCollaborator addCollaborator(Dictionary dictionary, User user) {
        logger.debug("Add dictionary collaborator: {} {}", dictionary, user);

        if (dictionary.getCollaborators().size() >= getDictionaryMaxCollaborators())
            throw new DictionaryMaxCollaboratorsReached();

        Optional<DictionaryCollaborator> collaborator = dictionary.getCollaborators().stream().filter(dictionaryCollaborator -> Objects.equals(dictionaryCollaborator.getUser().getId(), user.getId())).findFirst();
        if (collaborator.isPresent())
            throw new DictionaryCollaboratorAlreadyExists();

        DictionaryCollaborator dictionaryCollaborator = new DictionaryCollaborator();
        dictionaryCollaborator.setDictionary(dictionary);
        dictionaryCollaborator.setUser(user);
        dictionaryCollaborator.setAccepted(false);
        dictionaryCollaborator.setCanEdit(false);
        dictionary.getCollaborators().add(dictionaryCollaborator);

        dictionaryDao.update(dictionary);

        return dictionaryCollaborator;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public DictionaryCollaborator acceptCollaborator(Dictionary dictionary, User user) {
        logger.debug("Accept dictionary collaborator: {} {}", dictionary, user);

        Optional<DictionaryCollaborator> collaborator = dictionary.getCollaborators().stream().filter(dictionaryCollaborator -> Objects.equals(dictionaryCollaborator.getUser().getId(), user.getId())).findFirst();

        if (collaborator.isEmpty())
            throw new DictionaryCollaboratorDoesntExists();

        collaborator.ifPresent(dictionaryCollaborator -> dictionaryCollaborator.setAccepted(true));

        return collaborator.get();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public DictionaryCollaborator toggleCollaborator(Dictionary dictionary, User user) {
        logger.debug("Delete dictionary collaborator: {} {}", dictionary, user);

        Optional<DictionaryCollaborator> collaborator = dictionary.getCollaborators().stream().filter(dictionaryCollaborator -> Objects.equals(dictionaryCollaborator.getUser().getId(), user.getId())).findFirst();

        if (collaborator.isEmpty())
            throw new DictionaryCollaboratorDoesntExists();

        if (Boolean.FALSE.equals(collaborator.get().getAccepted()))
            throw new DictionaryCollaboratorDoesntExists();

        collaborator.ifPresent(dictionaryCollaborator -> dictionaryCollaborator.setCanEdit(!dictionaryCollaborator.getCanEdit()));

        return collaborator.get();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationException.class)
    public void removeCollaborator(Dictionary dictionary, User user) {
        logger.debug("Delete dictionary collaborator: {} {}", dictionary, user);

        Optional<DictionaryCollaborator> collaborator = dictionary.getCollaborators().stream().filter(dictionaryCollaborator -> Objects.equals(dictionaryCollaborator.getUser().getId(), user.getId())).findFirst();

        if (collaborator.isEmpty())
            throw new DictionaryCollaboratorDoesntExists();

        collaborator.ifPresent(dictionaryCollaborator -> dictionary.getCollaborators().remove(dictionaryCollaborator));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Dictionary getDictionaryById(long id) {
        logger.debug("Getting dictionary with ID: {}", id);

        return dictionaryDao.findOne(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<Dictionary> getDictionariesByCreator(User creator) {
        logger.debug("Getting dictionaries with user: {}", creator);

        return dictionaryDao.getDictionariesByCreator(creator);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<Dictionary> getDictionariesByCreatorOrCollaborator(User creator) {
        logger.debug("Getting dictionaries with user/collaborator: {}", creator);

        return dictionaryDao.getDictionariesByCreatorOrCollaborator(creator);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults) {
        logger.debug("Getting dictionary from {} to {}", firstResult, maxResults);

        return dictionaryDao.getDictionariesPaginated(creator, firstResult, maxResults);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Long getDictionaryCount(User creator) {
        logger.debug("Get dictionary count {}", creator);

        return dictionaryDao.getDictionaryCount(creator);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public boolean isDictionaryCollaborator(Dictionary dictionary, User user) {
        logger.debug("isDictionaryCollaborator {}", user);

        return dictionaryDao.isDictionaryCollaborator(dictionary, user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public boolean isDictionaryActiveCollaborator(Dictionary dictionary, User user) {
        logger.debug("isDictionaryActiveCollaborator {}", user);

        return dictionaryDao.isDictionaryActiveCollaborator(dictionary, user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public Dictionary getDefaultDictionary() {
        return dictionaryDao.findOne(Long.parseLong(configurationService.getConfiguration("game_default_dictionary_id")));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ApplicationException.class)
    public int getDictionaryMaxCollaborators() {
        return Integer.parseInt(configurationService.getConfiguration("dictionaries_max_collaborators"));
    }

}
