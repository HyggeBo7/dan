package top.dearbo.log.config;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: DefaultLogConfig
 * @createDate: 2020-07-01 18:05.
 * @description: 默认
 */
public class DefaultSysLogConfig implements SysLogConfig {

    protected static final Map<String, String> OPERATE_MAP = new HashMap<>(16);

    private static final List<String> EXCLUDE_PARAM_KEY_LIST = Collections.singletonList("oauth");

    static {
        OPERATE_MAP.put("audit", "审核");
        OPERATE_MAP.put("add", "新增");
        OPERATE_MAP.put("insert", "新增");
        OPERATE_MAP.put("save", "操作");
        OPERATE_MAP.put("update", "修改");
        OPERATE_MAP.put("delete", "删除");
        OPERATE_MAP.put("remove", "删除");
        OPERATE_MAP.put("query", "获取");
        OPERATE_MAP.put("get", "获取");
        OPERATE_MAP.put("list", "查询");
    }

    @Override
    public List<String> excludeParamKey() {
        return EXCLUDE_PARAM_KEY_LIST;
    }

    @Override
    public String getType(HttpServletRequest request) {
        String[] split = request.getRequestURI().split("[/]");
        if (split.length > 1) {
            String value = split[2].toLowerCase();
            for (Map.Entry<String, String> item : OPERATE_MAP.entrySet()) {
                if (value.contains(item.getKey())) {
                    return item.getValue();
                }
            }
        }
        return "other";
    }

    @Override
    public String getSubject(HttpServletRequest request) {
        String[] split = request.getRequestURI().split("[/]");
        return split.length > 0 ? split[1] : "";
    }

    public Map<String, String> getOperateTypeMap() {
        return OPERATE_MAP;
    }

    public void addOperateType(String key, String value) {
        if (StringUtils.isNotBlank(key)) {
            OPERATE_MAP.put(key, value);
        }
    }
}
