package com.dan.utils.xt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Dan on 2017/8/21.
 */
public class BaseQuery {

    private Integer isDelete;

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    private static final long serialVersionUID = 8809142367875458974L;
    private static final Integer defaultPageSize = new Integer(20);
    private static final Integer defaultFristPage = new Integer(1);
    private static final Integer defaultTotalItem = new Integer(0);

    /**
     * 总共多少条
     */
    private Integer count;
    /**
     * 每页多少条数据
     */
    private Integer pageSize;
    /**
     * 当前页码
     */
    private Integer pageIndex;
    private int startRow = -1;
    private int endRow = -1;

    /**
     * 排序字段
     */
    private String property;
    /**
     * 排序方向
     * ASC 升序
     * DESC 降序
     */
    private String direction;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    protected Integer getDefaultPageSize() {
        return defaultPageSize;
    }

    /**
     * @return Returns the currentPage.
     */
    public Integer getPageIndex() {
        if (pageIndex == null) {
            return defaultFristPage;
        }

        return pageIndex;
    }

    /**
     * @param cPage The currentPage to set.
     */
    public void setPageIndex(Integer cPage) {
        if ((cPage == null) || (cPage.intValue() <= 0)) {
            this.pageIndex = null;
        } else {
            this.pageIndex = cPage;
        }
    }

    private void setStartEndRow() {
        this.startRow = this.getPageSize().intValue() * (this.getPageIndex().intValue() - 1);
        this.endRow = this.startRow + this.getPageSize().intValue();
    }

    /**
     * @return Returns the pageSize.
     */
    public Integer getPageSize() {
        if (pageSize == null) {
            return getDefaultPageSize();
        }

        return pageSize;
    }

    public boolean hasSetPageSize() {
        return pageSize != null;
    }

    /**
     * @param pSize The pageSize to set.
     */
    public void setPageSize(Integer pSize) {
        if ((pSize == null) || (pSize.intValue() <= 0)) {
            this.pageSize = null;
        } else {
            this.pageSize = pSize;
        }
        setStartEndRow();
    }

    /**
     * @return Returns the totalItem.
     */
    public Integer getCount() {
        if (count == null) {
            return defaultTotalItem;
        }

        return count;
    }

    /**
     * @param tItem The totalItem to set.
     */
    public void setCount(Integer tItem) {
        if (tItem == null) {
            throw new IllegalArgumentException("TotalItem can't be null.");
        }

        this.count = tItem;

        int current = this.getPageIndex().intValue();
        int lastPage = this.getTotalPage();

        if (current > lastPage) {
            this.setPageIndex(new Integer(lastPage));
        }
    }

    public int getTotalPage() {
        int pgSize = this.getPageSize().intValue();
        int total = this.getCount().intValue();
        int result = total / pgSize;

        if ((total == 0) || ((total % pgSize) != 0)) {
            result++;
        }

        return result;
    }

    /**
     * @return Returns the endRow.
     */
    public int getEndRow() {
        if (endRow == -1) {
            setStartEndRow();
        }
        return endRow;
    }

    /**
     * @param endRow The endRow to set.
     */
    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    /**
     * @return Returns the startRow.
     */
    public int getStartRow() {
        if (startRow == -1) {
            setStartEndRow();
        }
        return startRow;
    }

    /**
     * @param startRow The startRow to set.
     */
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
