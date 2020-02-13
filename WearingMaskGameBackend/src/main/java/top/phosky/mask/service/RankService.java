package top.phosky.mask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import top.phosky.mask.dao.file.UserDAO;
import top.phosky.mask.dto.SelfRankDTO;
import top.phosky.mask.entity.User;

@Controller
public class RankService {
    @Autowired
    private UserDAO userDAO;



    public SelfRankDTO getRankAndScore(String wxID) {
        User user = userDAO.select(wxID);
        if (user == null) {
            return null;
        }
        //TODO


        return new SelfRankDTO(user.getMaxMarks(), 0);
    }
}
