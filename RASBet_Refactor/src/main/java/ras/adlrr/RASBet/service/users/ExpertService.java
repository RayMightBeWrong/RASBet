package ras.adlrr.RASBet.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.ExpertRepository;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.service.interfaces.users.IExpertService;

import java.util.List;

@Service
public class ExpertService implements IExpertService {
    private final ExpertRepository expertRepository;

    @Autowired
    public ExpertService(ExpertRepository expertRepository) {
        this.expertRepository = expertRepository;
    }

    /**
     * Checks for the existence of an expert with the given id. If the expert exists, returns it.
     * @param id Identification of the expert
     * @return expert if it exists, or null
     */
    public Expert getExpertById(int id){
        return expertRepository.findById(id).orElse(null);
    }

    /**
     * Checks for the existence of an expert with the given email. If the expert exists, returns it.
     * @param email Identification of the expert
     * @return expert if it exists, or null
     */
    public Expert getExpertByEmail(String email) {
        return expertRepository.findByEmail(email).orElse(null);
    }

    /**
     * Adds an expert to the repository
     * @param expert Expert to be persisted
     * @return expert updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Expert addExpert(Expert expert) throws Exception {
        expert.setId(0);
        String attributesError = UserAuxiliarMethods.validateUserAttributes(expert);
        if(attributesError != null)
            throw new Exception(attributesError);
        return expertRepository.save(expert);
    }

    /**
     * If an expert with the given id exists, removes it from the repository
     * @param id Identification of the expert
     * @throws Exception If the expert does not exist.
     */
    public void removeExpert(int id) throws Exception {
        if(!expertExistsById(id))
            throw new Exception("Admin does not exist!");
        expertRepository.deleteById(id);
    }

    /**
     * @return list of experts present in the repository
     */
    public List<Expert> getListOfExperts(){
        return expertRepository.findAll();
    }

    /**
     * Checks for the existence of an expert with the given id
     * @param expert_id Identification of the expert
     * @return true if an expert exists with the given identification
     */
    public boolean expertExistsById(int expert_id){
        return expertRepository.existsById(expert_id);
    }

    /**
     * Checks for the existence of a expert with the given email
     * @param email Identification of the expert
     * @return true if a expert exists with the given identification
     */
    public boolean expertExistsByEmail(String email){
        return expertRepository.existsByEmail(email);
    }

    /**
     * Validates all the attributes of an expert.
     * @param expert Object that extends the class User, which contains the attributes.
     * @return "null" if all attributes are valid or string mentioning error
     */
    private String validateExpertAttributes(Expert expert){
        return UserAuxiliarMethods.validateUserAttributes(expert);
    }
}
