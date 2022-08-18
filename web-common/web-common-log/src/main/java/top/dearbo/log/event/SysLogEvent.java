package top.dearbo.log.event;

import org.springframework.context.ApplicationEvent;
import top.dearbo.log.entity.SysLogEntity;

/**
 * @author Bo
 * @version 1.0
 * @fileName SysLog
 * @createDate 2020-07-01 16:05.
 * @description 系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLogEntity source) {
        super(source);
    }

}
