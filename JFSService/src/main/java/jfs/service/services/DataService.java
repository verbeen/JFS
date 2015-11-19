package jfs.service.services;

import jfs.data.connections.DataClient;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Created by lpuddu on 3-11-2015.
 */
@Startup @Singleton
public class DataService {
    private DataClient dataClient = new DataClient("jfs");

    public DataClient getDataClient(){
        return this.dataClient;
    }
}
