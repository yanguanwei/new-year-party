package cn.edu.zju.isst.party.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.edu.zju.isst.api.service.UserService;
import cn.edu.zju.isst.bccs.BccsApi;
import cn.edu.zju.isst.config.PartyConfig;
import cn.edu.zju.isst.dao.ShowDao;
import cn.edu.zju.isst.dao.SpittleDao;
import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.LoggedUser;
import cn.edu.zju.isst.entity.LotterySpittle;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Show;
import cn.edu.zju.isst.entity.User;
import cn.edu.zju.isst.pushlet.SpittleEventPullSource;

@Controller
public class PartyAdminController extends BaseController {
    @Autowired
    private ShowDao showDao;
    @Autowired
    private SpittleDao spittleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/admin.html", method = RequestMethod.GET)
    public String index(Model model, @ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            model.addAttribute("shows", showDao.retrieve());
            model.addAttribute("teachers", userDao.retrieve(1));
            model.addAttribute("isPartyEnded", PartyConfig.PARTY_ENDED);
            model.addAttribute("disabledUsers", userService.findDisabled());
            return "admin.page";
        }
        return "redirect:index.html";
    }
    
    @RequestMapping(value = "/disableUserForm.html", method = RequestMethod.GET)
    public String disabledUserForm(@ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            return "disable-user.dialog";
        }
        return "redirect:index.html";
    }
    
    @RequestMapping(value = "/enableUserForm.html", method = RequestMethod.GET)
    public String enabledUserForm(@RequestParam("userId") int userId, Model model, @ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            model.addAttribute("userId", userId);
            return "enable-user.dialog";
        }
        return "redirect:index.html";
    }
    
    @RequestMapping(value = "/disableUser", method = RequestMethod.POST)
    public @ResponseBody ResultHolder saveDisableUser(
            @RequestParam("userName") String username, 
            @ModelAttribute("user") LoggedUser user) {
        if (user.getId() != 1) {
            return new ResultHolder("无权限");
        }
        
        User u = userService.get(username);
        if (null != u) {
            userService.disableUser(u.getId());
        }
        
        return new ResultHolder(1);
    }
    
    @RequestMapping(value = "/enableUser", method = RequestMethod.POST)
    public @ResponseBody ResultHolder saveEnableUser(
            @RequestParam("userId") int userId, 
            @ModelAttribute("user") LoggedUser user) {
        if (user.getId() != 1) {
            return new ResultHolder("无权限");
        }
        
        User u = userService.get(userId);
        if (null != u) {
            userService.enableUser(u.getId());
        }
        
        return new ResultHolder(1);
    }

    @RequestMapping(value = "/notice.html", method = RequestMethod.GET)
    public String index() {
        return "notice.html";
    }
    
    @RequestMapping(value = "/crazy.html", method = RequestMethod.GET)
    public String crazy(Model model, @ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            model.addAttribute("likeCrazyUsers", spittleDao.getLikeCrazyUser(6));
            model.addAttribute("dislikeCrazyUsers", spittleDao.getDislikeCrazyUser(6));
            return "crazy.html";
        }
        return "redirect:index.html";
    }
    
    @RequestMapping("/admin/clearCached")
    @ResponseStatus(HttpStatus.OK)
    public void clearCached(@ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            userDao.clearCachedUsers();
            userService.cleanUserLoggedCounts();
        }
    }
    
    @RequestMapping("/admin/partyEnded")
    @ResponseStatus(HttpStatus.OK)
    public void partyEnded(@ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            PartyConfig.PARTY_ENDED = !PartyConfig.PARTY_ENDED;
        }
    }

    @RequestMapping(value = "/votes.html")
    public String votes(@ModelAttribute("user") LoggedUser user, Model model) {
        if (user.getId() == 0) {
            return "redirect:login.html?returnUrl=votes.html";
        } else if (user.getId() == 1) {
            List<String> showNames = new ArrayList<String>();
            List<Integer> showNameMapper = new ArrayList<Integer>();
            for (Show show : showDao.retrieve()) {
                String showName = show.getName();
                showNames.add(showName.length() > 10 ? showName.substring(0, 10) : showName);
                showNameMapper.add(show.getId());
            }

            model.addAttribute("showNames", new JSONArray(showNames));
            model.addAttribute("showNameMapper", new JSONArray(showNameMapper));
            model.addAttribute("voteData", new JSONObject(showDao.statisticalVote()));

            return "votes.html";
        }
        return "redirect:index.html";
    }
    
    @RequestMapping(value = "/screen.html")
    public String screen(@ModelAttribute("user") LoggedUser user, Model model) {
        if (user.getId() == 0) {
            return "redirect:login.html?returnUrl=screen.html";
        } else if (user.getId() == 1) {
            model.addAttribute("isPushingActived", SpittleEventPullSource.isPushingActived());
            return "screen.html";
        }
        return "redirect:index.html";
    }
    
    @RequestMapping(value = "/lottery.html")
    public String lottery(@ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 0) {
            return "redirect:login.html?returnUrl=lottery.html";
        } else if (user.getId() == 1) {
            return "lottery.html";
        }
        return "redirect:index.html";
    }
    
    @RequestMapping(value = "/announcement.html")
    public String announcement() {
        return "announcement.html";
    }

    @RequestMapping("/admin/getLotterySpittles.json")
    public @ResponseBody
    List<LotterySpittle> getLotterySpittles(@ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            return spittleDao.retrieveLotterySpittles();
        }
        return null;
    }

    @RequestMapping(value = "/admin/winPrize", method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder savePrize(@ModelAttribute("user") LoggedUser user, @RequestParam("spittleId[]") int[] spittleIds, @RequestParam("prizeType") int prizeType) {
        if (user.getId() == 1) {
            spittleDao.winPrize(spittleIds, prizeType);
            return new ResultHolder();
        } else {
            return new ResultHolder("无权限");
        }
    }

    @RequestMapping(value = "/admin/pausePushing", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void pause(@ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            SpittleEventPullSource.pausePushing();
        }
    }

    @RequestMapping(value = "/admin/resumePushing", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void resume(@ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            SpittleEventPullSource.resumePushing();
        }
    }

    @RequestMapping(value = "/admin/pushMessage", method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder pushMessage(@ModelAttribute("user") LoggedUser user, @RequestParam("title") String title,
            @RequestParam("description") String description) {
        if (user.getId() != 1) {
            return new ResultHolder("无权限");
        }
        int amount = BccsApi.pushAndroidBroadcastMessage("version", title, description);
        return new ResultHolder(amount);
    }
}
