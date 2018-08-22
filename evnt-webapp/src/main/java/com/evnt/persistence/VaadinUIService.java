package com.evnt.persistence;

/**
 * @author Roland Krüger
 */
public interface VaadinUIService {
    void postNavigationEvent(Object source, String target);

    boolean isUserAnonymous();
}