package top.phosky.mask.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.phosky.mask.dto.SelfRankDTO;
import top.phosky.mask.service.RankService;

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
     */
    @RequestMapping(value = "/api/getRank", method = RequestMethod.POST)
    @ResponseBody
    public String getRank(@RequestParam(name = "wxID") String wxID) {
        SelfRankDTO selfRankDTO = rankService.getRankAndScore(wxID);
        if (selfRankDTO == null) {
            return "NULL_WX_ID";
        }
        return null;
    }

    /**
     * 得到世界前5名的数据
     * <p>
     * 无传入参数
     * 返回状态类JSON
     */
    @RequestMapping(value = "/api/getRankTop5", method = RequestMethod.POST)
    @ResponseBody
    public String getRankTop5() {
        //TODO
        return null;
    }

    /**
     * 上传某个玩家的分数
     * <p>
     * 传入参数:玩家的wxID，分数:int
     * 返回状态类(state:int，content:String)的JSON字符串
     */
    @RequestMapping(value = "/api/setRank", method = RequestMethod.POST)
    @ResponseBody
    public String setRank(@RequestParam(name = "wxID") String wxID, @RequestParam(name = "marks") String marks) {
        //TODO
        return null;
    }
}
