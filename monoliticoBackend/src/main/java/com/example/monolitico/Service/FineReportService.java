package com.example.monolitico.Service;

import com.example.monolitico.Entities.FineReportEntity;
import com.example.monolitico.Entities.ToolsRankingEntity;
import com.example.monolitico.Repositories.FineReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineReportService {

    @Autowired
    private FineReportRepository fineReportRepository;

    //in this case we only gona implement create, getall and delete
    //because its a report, when save, we only can delete, not update

    public List<FineReportEntity> getAllFineReport() {
        return fineReportRepository.findAll();
    }

    public List<FineReportEntity> getToolsRankingByReportId(Long reportId){
        return fineReportRepository.findByReportId(reportId);
    }

    public FineReportEntity createFineReport(FineReportEntity fineReportEntity) {
        return fineReportRepository.save(fineReportEntity);
    }

    public FineReportEntity updateFineReport(FineReportEntity fineReportEntity) {
        return fineReportRepository.save(fineReportEntity);
    }

    public boolean deleteFineReport(Long id) throws Exception {
        try{
            fineReportRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
