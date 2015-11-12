package jfs.service.services;

import javax.ejb.Singleton;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lpuddu on 12-11-2015.
 */
@Singleton
public class SessionService {
    public final ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<String, String>();
}
