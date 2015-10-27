package jfs.service;

import jfs.service.services.JFSService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Created by lpuddu on 26-10-2015.
 */
@Singleton @Startup
public class Start {
    private static JFSService service;

    @PostConstruct
    private void init()
    {
        if(Start.service == null)
        {
            Start.service = new JFSService();
            //Start.service.init();
            //testing if develop commit triggers jenkins build
        }
    }

}
