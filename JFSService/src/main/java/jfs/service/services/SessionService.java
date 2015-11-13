package jfs.service.services;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class SessionService {
    public static final ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<String, String>();
}
