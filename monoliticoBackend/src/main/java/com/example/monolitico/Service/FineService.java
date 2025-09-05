package com.example.monolitico.Service;

import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Repositories.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    public FineEntity getFineById(Long id) {
        return fineRepository.findById(id).get();
    }

    public List<FineEntity> getAllFine() {
        return fineRepository.findAll();
    }

    public FineEntity saveFine(FineEntity fineEntity) {
        return fineRepository.save(fineEntity);
    }

    public FineEntity updateFine(FineEntity fineEntity) {
        return fineRepository.save(fineEntity);
    }

    public boolean deleteFineById(Long id) throws Exception{
        try{
            fineRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public boolean hasFinesByClientId(Long id){
        //if the list of fines of a client is empty, it means that the client hasn't have fines
        if(fineRepository.getFineEntityByClientIdAndTypeIs(id, "fine").isEmpty()){
            return false;
        }
        return true;
    }

    public boolean hasFinesOfToolReposition(Long id){
        if(fineRepository.getFineEntityByClientIdAndTypeIs(id, "reposition").isEmpty()){
            return false;
        }
        return true;
    }

    public boolean hasLessOrEqual5FinesByClientId(Long id){
        if(fineRepository.getFineEntityByClientIdAndTypeIs(id, "fine").size()<=5){
            return true;
        }
        return false;
    }
}
