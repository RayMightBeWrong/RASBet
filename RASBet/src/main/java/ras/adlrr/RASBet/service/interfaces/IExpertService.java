package ras.adlrr.RASBet.service.interfaces;

import java.util.List;

import ras.adlrr.RASBet.model.Expert;

public interface IExpertService {
    public Expert getExpertById(int id);

    public Expert getExpertByEmail(String email);

    public Expert addExpert(Expert expert) throws Exception;

    public void removeExpert(int id) throws Exception;

    public List<Expert> getListOfExperts();

    public boolean expertExistsById(int expert_id);
}
