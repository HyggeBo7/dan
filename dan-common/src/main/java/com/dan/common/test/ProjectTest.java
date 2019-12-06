package com.dan.common.test;

import com.dan.common.util.excel.ExcelWriteUtils;
import com.dan.utils.JsonUtil;
import com.dan.utils.lang.DateUtil;
import com.dan.utils.network.HttpUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: Test
 * @createDate: 2019-11-01 10:32.
 * @description:
 */
public class ProjectTest {

    @Test
    public void importExcelPackage() throws IOException {
        //导入
        ExcelWriteUtils excelWriteUtils = ExcelWriteUtils.importExcel("P:/包装-import.xls", "Sheet1");
        //一行数据为null，或者""，不添加
        excelWriteUtils.setEmptyRowFlag(false);
        //显示读取行号
        excelWriteUtils.setDebugNumFlag(true);
        //设置排除行:一.下标以逗号分隔，二：传int集合
        //excelWriteUtils.setExcludeLineList("1,2");
        //excelWriteUtils.setExcludeLineList(Arrays.asList(1, 2));
        List<List<Map<String, String>>> listList = excelWriteUtils.readExcelWithoutTitle(true, 1, false);
        for (List<Map<String, String>> listMap : listList) {
            System.out.println("listMap:" + JsonUtil.toJson(listMap));
            for (Map<String, String> map : listMap) {

                /*for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.print(entry.getKey() + "=========" + entry.getValue() + ",");
                }*/
            }
        }
    }

    @Test
    public void testDateOneOrLastDay() {
        Date date = new Date();
        String strDate = "2019-11-15 10:00:10";
        //date = DateUtil.parseDateHMS(strDate);
        //Date dateFirstDay = DateUtil.getDateFirstDay(date);
        Date dateLastDay = DateUtil.getDateLastDay(date);
        //System.out.println("dateFirstDay:" + DateUtil.parseToString(dateFirstDay));
        System.out.println("dateLastDay:" + DateUtil.parseToString(dateLastDay));
        List<Date> dateList = DateUtil.getDatesByDay(DateUtil.getStartOfDay(date), dateLastDay);
        for (Date date1 : dateList) {
            System.out.println("date1:" + DateUtil.parseToString(date1));
        }
    }

    @Test
    public void testTencentCheckIn() {
        String url = "https://vip.video.qq.com/fcgi-bin/comm_cgi?name=hierarchical_task_system&cmd=2&_=1573203663793&callback=Zepto1573203623995";
        String cookie = "tvfe_boss_uuid=02a135417eb2a9e6; video_platform=2; video_guid=10637fbc8386aa4a; pgv_pvid=1445208576; pgv_pvi=7874613248; RK=XpghHgPJNO; ptcz=b87e50cb7a83790b60797f19d2ed9cf346866a775bde7ee7454f7b8d6476ced0; ptui_loginuin=1143311620; pgv_info=ssid=s6592245476; pgv_si=s2532952064; _qpsvr_localtk=0.5369199070964024; ptisp=cm; main_login=qq; vqq_access_token=C16114C5B554FB3944EA0518BD07A44B; vqq_appid=101483052; vqq_openid=E0AC2C8BB1058083DD4CD7972802C74D; vqq_vuserid=169496460; vqq_vusession=mabhOn6SPE3aBAHy-Sycdg..; vqq_refresh_token=8C16CD4FD8DEE125141CF02283448636; login_time_init=2019-11-11 9:22:17; uid=224268488; vqq_next_refresh_time=6598; vqq_login_time_init=1573435338; login_time_last=2019-11-11 9:22:18";
        String cookie1 = "tvfe_boss_uuid=02a135417eb2a9e6; video_guid=10637fbc8386aa4a; video_platform=2; pgv_pvid=1445208576; pgv_pvi=7874613248; RK=XpghHgPJNO; ptcz=b87e50cb7a83790b60797f19d2ed9cf346866a775bde7ee7454f7b8d6476ced0; ptui_loginuin=1143311620; pgv_info=ssid=s6592245476; pgv_si=s2532952064; _qpsvr_localtk=0.5369199070964024; ptisp=cm; main_login=qq; vqq_access_token=477E2959E26072399D9509356EECED64; vqq_appid=101483052; vqq_openid=7D8F19C87694EA7B5BD4FBB2BC81CE3C; vqq_vuserid=178661733; vqq_vusession=Z2tFZaT32lwPoEnl8oP-Kw..; vqq_refresh_token=748F7621982C71192FCE0DB30B35476D; login_time_init=2019-11-11 11:46:42; uid=226231994; vqq_next_refresh_time=6598; vqq_login_time_init=1573444003; login_time_last=2019-11-11 11:46:43";
        Map<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("Cookie", cookie1);
        HttpUtils httpUtils = HttpUtils.createRequest();
        HttpUtils.ResultResponse resultResponse = httpUtils.doGet(url, null, headerMap);
        System.out.println("resultResponse:" + resultResponse.toString());

    }

    @Test
    public void testTencentHZCheckIn() {
        String url = "https://vip.qzone.qq.com/fcg-bin/v2/fcg_mobile_vip_site_checkin?t=0.46869834180487055&g_tk=364202229&qzonetoken=(function(){var%20t&uin=202515345&format=json263698854";
        String cookie = "pgv_pvi=9176929280; pgv_si=s1162546176; ptisp=cm; RK=XJIh3tgJFC; ptcz=b2fbeddbfcd2c7498602b0a2c37804921aa065dbb1366a8b6c3fdbf5b66fd1e6; uin=o0202515345; skey=@0Ui5i8LwO; p_uin=o0202515345; pt4_token=liJJdat3A4vP76Ducxm4tx*qDTntzPXD-Zfv6D1pVOI_; p_skey=rqpk4AmbB84xrrI6dU4He-hycBYx1XJx0HXlRFqhkzk_";
        Map<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("Cookie", cookie);
        //headerMap.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 5.1.1; h60-l01 Build/LYZ28N)");
        HttpUtils httpUtils = HttpUtils.createRequest();
        HttpUtils.ResultResponse resultResponse = httpUtils.doGet(url, null, headerMap);
        System.out.println("resultResponse:" + resultResponse.toString());

    }

    @Test
    public void testDate() {
        String strDate = "2019/10/20";
        Date parseToDate = DateUtil.parseToDate(strDate, "yyyy/MM/dd");
        String parseToString = DateUtil.parseToString(parseToDate);
        System.out.println("parseToString:" + parseToString);
    }

}
