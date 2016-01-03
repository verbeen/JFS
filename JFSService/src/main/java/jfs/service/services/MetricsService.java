package jfs.service.services;

import jfs.data.dataobjects.JobOfferMetricsDO;
import jfs.data.stores.JobOfferMetricsStore;
import jfs.transferdata.transferobjects.JobOfferMetricsDTO;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 14-12-2015.
 *
 * Job offer metrics wrapper for jobOfferMetricsStore
 *
 */
@Singleton
public class MetricsService {
    private JobOfferMetricsStore store = JobOfferMetricsStore.jobOfferMetricsStore;

    public List<JobOfferMetricsDTO> getJobOfferMetricsByCompany(String companyId){
        List<JobOfferMetricsDO> metricsDOList = this.store.getAllMetricsByCompanyId(companyId);

        List<JobOfferMetricsDTO> metrics = new ArrayList<>();
        for(JobOfferMetricsDO metricsDO : metricsDOList){
            metrics.add(this.createDTO(metricsDO));
        }
        return metrics;
    }

    private JobOfferMetricsDTO createDTO(JobOfferMetricsDO metricsDO){
        JobOfferMetricsDTO metricsDTO = new JobOfferMetricsDTO();
        metricsDTO.offerId = metricsDO._id;
        metricsDTO.applyClickCount = metricsDO.applyClickCount;
        metricsDTO.detailViewCount = metricsDO.detailViewCount;
        metricsDTO.listViewCount = metricsDO.listViewCount;
        return metricsDTO;
    }

}
