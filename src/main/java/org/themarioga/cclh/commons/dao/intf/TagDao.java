package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.models.Lang;
import org.themarioga.cclh.commons.models.Tag;

import java.util.List;

public interface TagDao {

    List<Tag> getTagsByLang(Lang lang);

}
