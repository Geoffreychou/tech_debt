//package xin.zero2one.test;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author ZJD
// * @date 2019/4/23
// */
//public class OhMyGad {
//
//    public static void main(String[] args) throws JsonProcessingException {
//        Map<String,String> map = new HashMap<>();
//        String[] split = s.split(";");
//        for(String str : split){
//            str = str.trim();
//            String[] split1 = str.split("=");
//            if (split1.length == 1){
//                map.put(split1[0],"");
//            } else {
//                map.put(split1[0], split1[1]);
//            }
//        }
//
//        ObjectMapper om = new ObjectMapper();
//        String value = om.writeValueAsString(map);
//
//        System.out.println(value);
//    }
//
//    static String s = "Ugrow-G0=370f21725a3b0b57d0baaf8dd6f16a18; login_sid_t=60fa48364ed7fa6f6c44e160ba39abcf; cross_origin_proto=SSL; YF-V5-G0=73b58b9e32dedf309da5103c77c3af4f; wb_view_log=1536*8641.25; _s_tentry=passport.weibo.com; Apache=585561697512.8641.1555990301871; SINAGLOBAL=585561697512.8641.1555990301871; ULV=1555990301898:1:1:1:585561697512.8641.1555990301871:; wb_view_log_5448293155=1536*8641.25; wb_timefeed_5448293155=1; WBStorage=201904231655|undefined; YF-Page-G0=b98b45d9bba85e843a07e69c0880151a|1556009963|1556009828; webim_unReadCount=%7B%22time%22%3A1556009964361%2C%22dm_pub_total%22%3A14%2C%22chat_group_pc%22%3A0%2C%22allcountNum%22%3A55%2C%22msgbox%22%3A0%7D; appkey=; crossidccode=CODE-gz-1HiRhI-1WwwOO-5W9mipT8rsWBx2O41f949; UOR=,,graph.qq.com; ALF=1587546067; SSOLoginState=1556010070; SCF=AvQfeBcRAkIbNCNf3B3PSEU8KFNxeKkGkd6arz0uYtc3bjCfg-zqi6_fFXZoh1srNptCLuvEbfFWte-UC7umVK8.; SUB=_2A25xuqQHDeRhGeNK71oT-S3NzjmIHXVSsZLPrDV8PUNbmtBeLU3TkW9NSXZGT2H46mxeWbRngaT7izn0BaV8oGqI; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhXU2-WQjKXS8CF-vibo7Dn5JpX5KzhUgL.Fo-XShnE1KepSK-2dJLoI0YLxKBLBonL1-eLxKBLBonL1-eLxKqLBoBL1KMLxK-L12qL1K2LxKML1hnLBo2LxK.LBKeL1KnLxKqL1-qL1KBt; SUHB=0vJIOhn2rKrMC4";
//
//}
