package com.provectus.formula.alexis.controlers;

import com.provectus.formula.alexis.DAO.EnWordDAO;
import com.provectus.formula.alexis.DAO.GroupDAO;
import com.provectus.formula.alexis.DAO.RuWordDAO;
import com.provectus.formula.alexis.DAO.WordDAO;
import com.provectus.formula.alexis.models.WordReturn;
import com.provectus.formula.alexis.services.IdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
public class WordController {

    private final GroupDAO groupDAO;
    private final WordDAO wordDAO;
    private final EnWordDAO enWordDAO;
    private final RuWordDAO ruWordDAO;
    @Autowired
    private IdUserService idUserService;

    @Autowired
    public WordController(GroupDAO groupDAO, WordDAO wordDAO, EnWordDAO enWordDAO, RuWordDAO ruWordDAO) {

        this.groupDAO = groupDAO;
        this.wordDAO = wordDAO;
        this.enWordDAO = enWordDAO;
        this.ruWordDAO = ruWordDAO;
    }


    @RequestMapping(value = "/api/words/suggestion/{Lang}/{WordPrefix}",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getRelevantWordsLike(@PathVariable("Lang") String lang,
                                             @PathVariable("WordPrefix") String wordPrefix,
                                             HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        List<String> outWords = new ArrayList<>();
        if (lang.equalsIgnoreCase("en"))
            outWords = enWordDAO.findLikeEnWord(wordPrefix);
        if (lang.equalsIgnoreCase("ru"))
            outWords = ruWordDAO.findLikeRuWord(wordPrefix);

        return outWords;
    }

    @RequestMapping(value = "/home/wordgroups/{GroupId}/words",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<WordReturn> getWordsByWordGroup(@PathVariable("GroupId") Long groupId,
                                                HttpServletResponse response,
                                                Principal principal) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Long userId = idUserService.findIdUserByMail(principal.getName());
        return wordDAO.getWordsByWordGroup(groupId, userId);
    }

    @RequestMapping(value = "/home/wordgroups/{wordGroupId}/words",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public WordReturn saveWordToWordGroup(@RequestBody WordReturn word,
                                          @PathVariable("wordGroupId") Long id,
                                          Principal principal) {
        if (idUserService.findIdUserByMail(principal.getName())
                .equals(groupDAO.findGroupById(id).getUserId())) {
            return wordDAO.saveWordToWordGroup(word, id);
        } else {
            throw new CurrentUserIdException();
        }
    }


    @RequestMapping(value = "/home/wordgroups/{wordGroupId}/words/{wordId}",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteWordFromWordGroup(@PathVariable("wordGroupId") Long idGroup,
                                        @PathVariable("wordId") Long wordId,
                                        Principal principal) {
        if (idUserService.findIdUserByMail(principal.getName())
                .equals(groupDAO.findGroupById(idGroup).getUserId())) {
            wordDAO.deleteWordFromWordGroup(idGroup, wordId);
        } else {
            throw new CurrentUserIdException();
        }
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class CurrentUserIdException extends RuntimeException {
        public CurrentUserIdException() {
            super("This word group does not belong to the current user");
        }
    }


}