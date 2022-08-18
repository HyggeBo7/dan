package top.dearbo.base.bean;


/**
 * 基类查询参数
 *
 * @author wb
 * @date 2022/08/18 15:18:52.
 */
public class BaseQuery {

	public static final Integer DEFAULT_PAGE_SIZE = 20;
	public static final Integer DEFAULT_FIRST_PAGE = 1;

	/**
	 * 当前页码
	 */
	private Integer pageIndex;

	/**
	 * 每页多少条数据
	 */
	private Integer pageSize;

	/**
	 * 排序字段
	 */
	private String orderBySort;

	private Integer count;

	public String getOrderBySort() {
		return orderBySort;
	}

	public void setOrderBySort(String orderBySort) {
		this.orderBySort = orderBySort;
	}

	/**
	 * @return Returns the currentPage.
	 */
	public Integer getPageIndex() {
		if (pageIndex == null) {
			return DEFAULT_FIRST_PAGE;
		}
		return pageIndex;
	}

	/**
	 * @param cPage The currentPage to set.
	 */
	public void setPageIndex(Integer cPage) {
		if ((cPage == null) || (cPage <= 0)) {
			this.pageIndex = null;
		} else {
			this.pageIndex = cPage;
		}
	}

	/**
	 * @return Returns the pageSize.
	 */
	public Integer getPageSize() {
		if (pageSize == null) {
			return DEFAULT_PAGE_SIZE;
		}
		return pageSize;
	}

	public boolean hasPage() {
		return pageIndex != null && pageSize != null;
	}

	/**
	 * @param pSize The pageSize to set.
	 */
	public void setPageSize(Integer pSize) {
		if ((pSize == null) || (pSize <= 0)) {
			this.pageSize = null;
		} else {
			this.pageSize = pSize;
		}
	}

	/**
	 * @return Returns the startRow.
	 */
	public int getStartRow() {
		return this.getPageSize() * (this.getPageIndex() - 1);
	}

	/**
	 * @return Returns the endRow.
	 */
	public int getEndRow() {
		return this.getPageSize();
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
