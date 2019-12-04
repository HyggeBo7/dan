package com.dan.common.util;

import com.dan.utils.lang.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

import java.util.Date;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: CronUtils
 * @createDate: 2019-12-04 15:59.
 * @description: Cron表达式工具类
 */
public class CronUtils {

    /**
     * 下一次运行时间 默认 yyyy-MM-dd HH:mm:ss
     *
     * @param cron cron表达式
     */
    public static String getCronScheduledString(String cron) {

        return getCronScheduledString(cron, DateUtil.FORMAT_LONG);
    }

    /**
     * 下一次运行时间
     *
     * @param cron    cron表达式
     * @param pattern 格式
     * @return
     */
    public static String getCronScheduledString(String cron, String pattern) {
        String timeScheduled = "";
        if (!CronExpression.isValidExpression(cron)) {
            throw new AssertionError("Cron is Illegal!Cron表达式有误，cron：" + cron);
        }
        try {
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("Caclulate Date").withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            Date time0 = trigger.getStartTime();
            Date time1 = trigger.getFireTimeAfter(time0);
            timeScheduled = DateUtil.parseToString(time1, pattern);
        } catch (Exception e) {
            throw new AssertionError("nKnow Time!，cron：" + cron);
        }
        return timeScheduled;
    }

    /**
     * 下一次运行时间 Date
     *
     * @param cron cron表达式
     * @return
     */
    public static Date getCronScheduledDate(String cron) {
        Date[] cronScheduledDate = getCronScheduledDate(cron, 1);
        if (cronScheduledDate != null && cronScheduledDate.length > 0) {
            return cronScheduledDate[0];
        }
        return null;

    }

    /**
     * 最近n次运行时间 Date[]
     *
     * @param cron  表达式
     * @param count 运行次数
     */
    public static Date[] getCronScheduledDate(String cron, Integer count) {
        if (!CronExpression.isValidExpression(cron)) {
            //throw new AssertionError("定时任务Cron格式不正确,Cron：" + cron);
            System.out.println("定时任务Cron格式不正确,Cron：" + cron);
            return null;
        }
        if (count == null || count < 1) {
            count = 1;
        }
        Date[] dateArray = new Date[count];
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("Caclulate Date").withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        Date time0 = trigger.getStartTime();
        Date time1 = trigger.getFireTimeAfter(time0);
        dateArray[0] = time1;
        if (dateArray.length > 1) {
            Date timeTemp = time1;
            for (int i = 1; i < dateArray.length; i++) {
                timeTemp = trigger.getFireTimeAfter(timeTemp);
                if (timeTemp != null) {
                    dateArray[i] = timeTemp;
                } else {
                    break;
                }
            }
        }
        return dateArray;
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
        return getCronScheduledDate(cron) != null;
    }
}
