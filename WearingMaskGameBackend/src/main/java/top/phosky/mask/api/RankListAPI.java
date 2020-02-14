package top.phosky.mask.api;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.phosky.mask.dto.SelfRankDTO;
import top.phosky.mask.dto.Top5DTO;
import top.phosky.mask.entity.User;
import top.phosky.mask.service.RankService;

import java.util.ArrayList;

@Controller
public class RankListAPI {
    @Autowired
    private RankService rankService;

    /**
     * 获得某个玩家的分数及排名
     * <p>
     * 传入参数:玩家的wxID
     * 返回JSON字符串：marks:int rank:int
     * 未根据id找到数据返回"NULL_WX_ID"
     * 排名查询错误返回"RANK_SEARCH_MISTAKE"
     * 未知错误返回"UNKNOWN_MISTAKE"
     */
    @RequestMapping(value = "/api/getRank", method = RequestMethod.POST)
    @ResponseBody
    public String getRank(@RequestParam(name = "wxID") String wxID) {
        try {
            SelfRankDTO selfRankDTO = rankService.getRankAndScore(wxID);
            if (selfRankDTO == null) {
                return "NULL_WX_ID";
            } else if (selfRankDTO.getRank() == -1) {
                return "RANK_SEARCH_MISTAKE";
            }
            return JSON.toJSONString(selfRankDTO);
        } catch (Exception err) {
            err.printStackTrace();
        }
        return "UNKNOWN_MISTAKE";
    }

    /**
     * 得到世界前5名的数据
     * <p>
     * 无传入参数
     * 返回包含五个User属性的JSON字符串
     * <p>
     * 若游玩总人数不足5，则返回的JSON字符串中不包含完整的5个属性
     * <p>
     * 未知错误返回UNKNOWN_FAULT
     */
    @RequestMapping(value = "/api/getRankTop5", method = RequestMethod.POST)
    @ResponseBody
    public String getRankTop5() {
        try {
            ArrayList<User> usersTop5 = rankService.getRankTop5();
            Top5DTO top5DTO = new Top5DTO(null, null, null, null, null);
            for (int i = usersTop5.size(); i < 5; i++) {//DEBUG
                usersTop5.add(null);//防止get时空指针异常
            }
            top5DTO.setTop1(usersTop5.get(0));
            top5DTO.setTop2(usersTop5.get(1));
            top5DTO.setTop3(usersTop5.get(2));
            top5DTO.setTop4(usersTop5.get(3));
            top5DTO.setTop5(usersTop5.get(4));
            return JSON.toJSONString(top5DTO);
        } catch (Exception err) {
            err.printStackTrace();
        }
        return "UNKNOWN_FAULT";
    }

    /**
     * 上传某个玩家的分数
     * <p>
     * 传入参数:玩家的wxID，分数:int
     * 返回状态字符串
     * 未找到此id的用户，返回CANNOT_FIND_USER
     * 修改破纪录并修改成功，返回MODIFIED_SUCCESS
     * 分数没有刷新记录，返回DOESNT_REFRESH_RECORD
     * 未知错误，返回UNKNOWN_FAULT
     */
    @RequestMapping(value = "/api/setRank", method = RequestMethod.POST)
    @ResponseBody
    public String setRank(@RequestParam(name = "wxID") String wxID, @RequestParam(name = "marks") int marks) {
        int status = rankService.setRank(wxID, marks);
        switch (status) {
            case 1:
                return "MODIFIED_SUCCESS";
            case 0:
                return "DOESNT_REFRESH_RECORD";
            case -1:
                return "CANNOT_FIND_USER";
            case -2:
                return "UNKNOWN_FAULT";
        }
        return "UNKNOWN_FAULT";
    }
}
