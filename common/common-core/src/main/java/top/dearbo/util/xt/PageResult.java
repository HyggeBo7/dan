package top.dearbo.util.xt;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import top.dearbo.base.bean.BaseQuery;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by bo on 2017/8/21.
 */
public class PageResult<T> {
    /**
     * 当前页，从1开始
     */
    private int currentPage = 1;

    /**
     * 每页大小
     */
    private int pageSize = 10;

    /**
     * results
     */
    private List<T> data;

    private String result;

    private int count;


    private boolean hasNext;

    /**
     * 是否有下一页
     *
     * @return
     */
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }


    private boolean isEmpty;

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    private Map<String, Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private final static PageResult EMPTY = new PageResult(0, 0, Collections.emptyList());

    @SuppressWarnings("unchecked")
    public static <T> PageResult<T> emptyPaginator() {
        return EMPTY;
    }

    public final static int TOTAL_COUNT_NOT_SUPPORTED = -1;

    public PageResult(List<T> data) {
        this.setPageSize(-1);
        this.setCurrentPage(-1);
        this.setData(data);
        this.setCount(TOTAL_COUNT_NOT_SUPPORTED);
    }

    public PageResult(int pageSize, int currentPage, List<T> data) {
        this.setPageSize(pageSize);
        this.setCurrentPage(currentPage);
        this.setData(data);
        this.setCount(TOTAL_COUNT_NOT_SUPPORTED);
    }

    public PageResult(BaseQuery baseQuery, Integer totalCount) {
        this(baseQuery.getPageSize(), baseQuery.getPageIndex(), null, totalCount);
    }


    public PageResult(BaseQuery baseQuery, List<T> data, Integer totalCount) {
        this(baseQuery.getPageSize(), baseQuery.getPageIndex(), data, totalCount);
    }

    public PageResult(int pageSize, int currentPage, List<T> data, Integer totalCount) {
        this.setPageSize(pageSize);
        this.setCurrentPage(currentPage);
        this.setData(data);
        if (totalCount == null) {
            totalCount = 0;
        }
        this.setCount(totalCount);
        this.setHasNext(count / pageSize > currentPage);
        this.setEmpty(CollectionUtils.isEmpty(data));

    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setCount(int totalCount) {
        this.count = totalCount;
    }

    public int getCount() {
        return count;
    }

    /**
     * 总共多少页
     *
     * @return
     */
    public int getTotalPage() {
        int totalCount = this.getCount();
        if (totalCount != TOTAL_COUNT_NOT_SUPPORTED) {
            int page = totalCount / this.getPageSize();
            page += totalCount - this.getPageSize() * page > 0 ? 1 : 0;
            return page;
        } else {
            return TOTAL_COUNT_NOT_SUPPORTED;
        }

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}

