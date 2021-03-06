package jfs.service.services;

import jfs.service.sessions.Session;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lpuddu on 12-11-2015.
 *
 * Service class for handling current sessions
 *
 */
public class SessionService {
    public static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
}
