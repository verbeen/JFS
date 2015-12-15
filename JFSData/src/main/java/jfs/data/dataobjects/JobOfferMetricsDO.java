package jfs.data.dataobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 13-12-2015.
 */
public class JobOfferMetricsDO extends DataObject {
    public String companyId;
    public int listViewCount = 0;
    public int detailViewCount = 0;
    public int applyClickCount = 0;

    public JobOfferMetricsDO(String jobOfferId, String companyId) {
        super(jobOfferId);
        this.companyId = companyId;
    }
}
