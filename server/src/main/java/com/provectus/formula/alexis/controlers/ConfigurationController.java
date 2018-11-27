package com.provectus.formula.alexis.controlers;

import com.provectus.formula.alexis.DAO.ConfigurationDAO;
import com.provectus.formula.alexis.DAO.GroupDAO;
import com.provectus.formula.alexis.models.ConfigurationReturn;
import com.provectus.formula.alexis.models.GroupReturn;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.services.IdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ConfigurationController {

    private final GroupDAO groupDAO;
    private final ConfigurationDAO configurationDAO;
    @Autowired
    private IdUserService idUserService;

    @Autowired
    public ConfigurationController(GroupDAO groupDAO, ConfigurationDAO configurationDAO) {
        this.groupDAO = groupDAO;
        this.configurationDAO = configurationDAO;
    }


    @RequestMapping(value = "/api/alexa/configuration",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ConfigurationReturn getUserSettings(HttpServletResponse response, Principal principal) {
        Long iduser = idUserService.findIdUserByMail(principal.getName());
        response.setHeader("Access-Control-Allow-Origin", "*");
        return configurationDAO.getConfigurationByUserId(iduser);
    }

    @RequestMapping(value = "/api/alexa/configuration",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUserSettings(@RequestBody ConfigurationReturn configurationReturn, Principal principal) {
        Long iduser = idUserService.findIdUserByMail(principal.getName());
        configurationDAO.updateConfiguration(configurationReturn, iduser);
    }

    @RequestMapping(value = "/api/alexa/configuration/groups",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GroupReturn> findByUserAndState(HttpServletResponse response, Principal principal) {
        Long iduser = idUserService.findIdUserByMail(principal.getName());
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<GroupEntity> returnGroup = groupDAO.findGroupsByUserIdAndActiveState(iduser, true);

        return groupDAO.GroupToReturn(returnGroup);
    }


    @RequestMapping(value = "/api/skill/configuration",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<ConfigurationReturn> getUserSettingsForAlexa(@RequestParam String awsId) {
        Optional<ConfigurationReturn> configurationReturn = configurationDAO.getConfigurationByAlexaId(awsId);
        return configurationReturn.isPresent() ? new ResponseEntity<>(configurationReturn.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
