package top.dearbo.frame.common.util;

import top.dearbo.util.lang.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: CronUtils
 * @createDate: 2019-12-04 15:59.
 * @description: Cron表达式工具类
 */
public class CronUtils {

    private static final Logger logger = LoggerFactory.getLogger(CronUtils.class);

    /**
     * 下一次运行时间 默认 yyyy-MM-dd HH:mm:ss
     *
     * @param cron cron表达式
     */
    public static String getCronScheduledString(String cron) {

        return getCronScheduledString(cron, DateUtil.FORMAT_LONG, new Date());
    }

    public static String getCronScheduledString(String cron, Date startDate) {

        return getCronScheduledString(cron, DateUtil.FORMAT_LONG, startDate);
    }

    /**
     * 下一次运行时间
     *
     * @param cron    cron表达式
     * @param pattern 格式
     * @return
     */
    public static String getCronScheduledString(String cron, String pattern, Date startDate) {
        if (!CronExpression.isValidExpression(cron)) {
            logger.error("Cron表达式有误，Cron：{}", cron);
            return null;
        }
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("Caclulate Date").withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        Date nextDate = trigger.getFireTimeAfter(startDate);
        if (nextDate != null) {
            return DateUtil.parseToString(nextDate, pattern);
        }
        return null;
    }

    /**
     * 下一次运行时间 Date
     *
     * @param cron cron表达式
     * @return Date
     */
    public static Date getCronScheduledNextDate(String cron) {
        return getCronScheduledNextDate(cron, new Date());
    }

    /**
     * 获取下一次运行时间
     *
     * @param cron      cron表达式
     * @param startDate 开始日期
     * @return Date
     */
    public static Date getCronScheduledNextDate(String cron, Date startDate) {
        List<Date> cronDateList = getCronScheduledDate(cron, 1, startDate);
        if (cronDateList != null && cronDateList.size() > 0) {
            return cronDateList.get(0);
        }
        return null;
    }

    /**
     * 最近n次运行时间 Date
     *
     * @param cron  表达式
     * @param count 运行次数
     */
    public static List<Date> getCronScheduledDate(String cron, Integer count) {

        return getCronScheduledDate(cron, count, new Date());
    }

    public static List<Date> getCronScheduledDate(String cron, Integer count, Date startDate) {
        if (!CronExpression.isValidExpression(cron)) {
            logger.error("Cron表达式有误，Cron：{}", cron);
            return null;
        }
        if (count == null || count < 1) {
            count = 1;
        }
        List<Date> dateList = new ArrayList<>();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("Caclulate Date").withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        Date nextDate = trigger.getFireTimeAfter(startDate);
        dateList.add(nextDate);
        if (count > 1) {
            for (int i = 1; i < count; i++) {
                nextDate = trigger.getFireTimeAfter(nextDate);
                if (nextDate != null) {
                    dateList.add(nextDate);
                } else {
                    break;
                }
            }
        }
        return dateList;
    }

    /**
     * 检验表达式是否正确
     *
     * @param cron cron表达式
     * @return 正确:true
     */
    public static boolean cronValidExpressionFlag(String cron) {
        return StringUtils.isNotBlank(cron) && CronExpression.isValidExpression(cron);
    }

    /**
     * 是否还有下一次执行时间
     *
     * @param cron cron表达式
     * @return 有:true
     */
    public static boolean cronNextFlag(String cron) {
        return getCronScheduledNextDate(cron) != null;
    }
}
