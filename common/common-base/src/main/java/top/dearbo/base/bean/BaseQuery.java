package top.dearbo.base.bean;


import java.beans.Transient;
import java.io.Serializable;

/**
 * 基类查询参数
 *
 * @author wb
 */
public class BaseQuery implements Serializable {

	public static final Integer DEFAULT_PAGE_SIZE = 20;
	public static final Integer DEFAULT_FIRST_PAGE = 1;

	/**
	 * 当前页码
	 */
	private transient Integer pageIndex;

	/**
	 * 每页多少条数据
	 */
	private transient Integer pageSize;

	/**
	 * 升序排序字段
	 */
	private transient String[] asc;
	/**
	 * 降序排序字段
	 */
	private transient String[] desc;

	public String[] getAsc() {
		return asc;
	}

	public void setAsc(String[] asc) {
		this.asc = asc;
	}

	public String[] getDesc() {
		return desc;
	}

	public void setDesc(String[] desc) {
		this.desc = desc;
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
	 * @param pageIndex The currentPage to set.
	 */
	public void setPageIndex(Integer pageIndex) {
		if ((pageIndex == null) || (pageIndex <= 0)) {
			this.pageIndex = null;
		} else {
			this.pageIndex = pageIndex;
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

	@Transient
	public boolean hasPage() {
		return pageIndex != null && pageSize != null;
	}

	/**
	 * @param pageSize The pageSize to set.
	 */
	public void setPageSize(Integer pageSize) {
		if ((pageSize == null) || (pageSize <= 0)) {
			this.pageSize = null;
		} else {
			this.pageSize = pageSize;
		}
	}

	/**
	 * @return Returns the startRow.
	 */
	@Transient
	public int getStartRow() {
		return this.getPageSize() * (this.getPageIndex() - 1);
	}

	/**
	 * @return Returns the endRow.
	 */
	@Transient
	public int getEndRow() {
		return this.getPageSize();
	}

}
