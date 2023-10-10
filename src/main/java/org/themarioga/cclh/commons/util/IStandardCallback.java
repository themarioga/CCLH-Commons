package org.themarioga.cclh.commons.util;

import org.themarioga.cclh.commons.exceptions.ApplicationException;

public interface IStandardCallback {

    void onSuccess();

    void onFailure(ApplicationException e);

}
