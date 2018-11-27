package com.provectus.formula.alexis.controlers;

import com.provectus.formula.alexis.DAO.GroupDAO;
import com.provectus.formula.alexis.models.GroupReturn;
import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.services.IdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
public class GroupController {


    private final GroupDAO groupDAO;
    @Autowired
    private IdUserService idUserService;

    @Autowired
    public GroupController(GroupDAO groupDAO) {

        this.groupDAO = groupDAO;
    }


    @RequestMapping(value = "/home/wordgroups",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GroupReturn> findByUser(HttpServletResponse response, Principal principal) {
        Long iduser = idUserService.findIdUserByMail(principal.getName());
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<GroupEntity> returnGroup = groupDAO.findGroupsByUserId(iduser);
        return groupDAO.GroupToReturn(returnGroup);
    }

    @RequestMapping(value = "/home/wordgroups/{wordGroupId}",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GroupReturn findByIdAndUser(@PathVariable("wordGroupId") Long groupId,
                                       HttpServletResponse response, Principal principal) {
        Long iduser = idUserService.findIdUserByMail(principal.getName());
        response.setHeader("Access-Control-Allow-Origin", "*");

        return groupDAO.findGroupByIdAndUserId(groupId, iduser);
    }


    @RequestMapping(value = "/home/wordgroups/{wordGroupId}",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("wordGroupId") Long id, Principal principal) {
        Long iduser = idUserService.findIdUserByMail(principal.getName());
        groupDAO.deleteGroup(id, iduser);
    }

    @RequestMapping(value = "/home/wordgroups", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GroupReturn saveWordGroup(@RequestBody GroupEntity wordGroup,
                                     HttpServletResponse response,
                                     Principal principal) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Long iduser = idUserService.findIdUserByMail(principal.getName());
        return groupDAO.saveGroup(wordGroup, iduser);
    }

    @RequestMapping(value = "/home/wordgroups", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody GroupEntity wordGroup, Principal principal) {
        Long iduser = idUserService.findIdUserByMail(principal.getName());
        groupDAO.updateGroup(wordGroup, iduser);
    }

}