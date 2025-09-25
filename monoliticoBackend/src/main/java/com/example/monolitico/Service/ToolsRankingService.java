package com.example.monolitico.Service;

import com.example.monolitico.Entities.ToolsRankingEntity;
import com.example.monolitico.Repositories.ToolsRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolsRankingService {

    @Autowired
    private ToolsRankingRepository toolsRankingRepository;

    public List<ToolsRankingEntity> getAllToolsRanking(){
        return toolsRankingRepository.findAll();
    }

    public List<ToolsRankingEntity> getToolsRankingByReportId(Long reportId){
        return toolsRankingRepository.findByReportId(reportId);
    }

    public ToolsRankingEntity createToolsRanking(ToolsRankingEntity toolsRankingEntity){
        return toolsRankingRepository.save(toolsRankingEntity);
    }

    public ToolsRankingEntity updateToolsRanking(ToolsRankingEntity toolsRankingEntity){
        return toolsRankingRepository.save(toolsRankingEntity);
    }

    public boolean deleteToolsRanking(ToolsRankingEntity toolsRankingEntity)throws  Exception{
        try{
            toolsRankingRepository.delete(toolsRankingEntity);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
