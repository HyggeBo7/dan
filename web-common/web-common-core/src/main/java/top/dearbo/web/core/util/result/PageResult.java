package top.dearbo.web.core.util.result;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import top.dearbo.base.bean.BaseQuery;
import top.dearbo.util.exception.AppException;
import top.dearbo.util.xt.MapperUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 结果返回
 * @version 1.0.0
 */
public class PageResult<T> implements Serializable {
	private Long total;
	/**
	 * 兼容旧项目-total
	 */
	private Long count;
	private List<T> data;
	private Map<String, Object> extraParams;

	public PageResult(Long total) {
		this.total = total;
		this.count = total;
		this.data = new ArrayList<>();
	}

	public PageResult(Long total, List<T> data) {
		this.total = total;
		this.count = total;
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public Long getCount() {
		return count;
	}

	public List<T> getData() {
		return data;
	}

	public void setTotal(Long total) {
		this.total = total;
		this.count = total;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Map<String, Object> getExtraParams() {
		return extraParams;
	}

	public void setExtraParams(Map<String, Object> extraParams) {
		this.extraParams = extraParams;
	}

	public static void startPage(BaseQuery paramQuery) {
		PageHelper.startPage(paramQuery.getPageIndex(), paramQuery.getPageSize());
	}

	public static void startPage(Integer pageIndex, Integer pageSize) {
		PageHelper.startPage(pageIndex, pageSize);
	}

	public static <E, T extends BaseQuery> PageInfo<E> getPageInfo(T query, ISelect select) {
		Integer pageIndex = query.getPageIndex();
		Integer pageSize = query.getPageSize();
		boolean count = true;
		if (pageSize.equals(Integer.MAX_VALUE) || pageSize.equals(Integer.MAX_VALUE - 1)) {
			//关闭分页：pageIndex:1,pageSize = Integer.MAX_VALUE
			pageIndex = 1;
			pageSize = Integer.MAX_VALUE;
			count = false;
		}
		String orderBy = getOrderBy(query);
		Page<E> startPage = PageHelper.startPage(pageIndex, pageSize, count);
		if (StringUtils.isNotBlank(orderBy)) {
			startPage.setOrderBy(orderBy);
		}
		PageInfo<E> pageInfo = startPage.doSelectPageInfo(select);
		if (!count) {
			pageInfo.setTotal(pageInfo.getList().size());
		}
		return pageInfo;
	}

	public static <E, T extends BaseQuery> PageResult<E> getPageResult(T query, ISelect select) {
		PageInfo<E> pageInfo = getPageInfo(query, select);
		return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
	}

	public static <T> PageResult<T> getPageResult(List<T> data) {
		PageInfo<T> pageInfo = new PageInfo<>(data);
		return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
	}

	public static <T> PageResult<T> getPageResult(List<T> data, Long total) {
		return new PageResult<>(total, data);
	}

	public static <T> PageResult<T> emptyData() {
		return new PageResult<>(0L, Collections.emptyList());
	}

	public static <T, C> PageResult<C> getPageResultChange(List<T> data, Class<C> targetClass) {
		return getPageResultChange(data, targetClass, null);
	}

	public static <T, C> PageResult<C> getPageResultChange(List<T> data, Class<C> targetClass, Callback<T, C> callback) {
		if (CollectionUtils.isEmpty(data)) {
			return emptyData();
		}
		return getPageResultChange(new PageInfo<>(data), targetClass, callback);
	}

	public static <T, C> PageResult<C> getPageResultChange(PageInfo<T> data, Class<C> targetClass) {
		return getPageResultChange(data, targetClass, null);
	}

	public static <T, C> PageResult<C> getPageResultChange(PageInfo<T> pageInfo, Class<C> targetClass, Callback<T, C> callback) {
		if (CollectionUtils.isEmpty(pageInfo.getList())) {
			return emptyData();
		}
		//PageInfo<T> pageInfo = new PageInfo<>(data);
		List<C> result = pageInfo.getList().stream().map(originalData -> {
			C targetBean;
			try {
				targetBean = targetClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new AppException("创建对象失败", e);
			}
			if (callback != null) {
				callback.transform(originalData, targetBean);
			} else {
				MapperUtil.copyProperties(originalData, targetBean);
			}
			return targetBean;
		}).collect(Collectors.toList());
		return new PageResult<>(pageInfo.getTotal(), result);
	}

	/**
	 * 总共多少页
	 */
	public long getTotalPage(BaseQuery paramQuery) {
		if (total != null && total > 0L) {
			long page = total / paramQuery.getPageSize();
			page += total - paramQuery.getPageSize() * page > 0 ? 1 : 0;
			return page;
		} else {
			return 0L;
		}
	}

	private static String getOrderBy(BaseQuery condition) {
		String[] ascArray = condition.getAsc();
		StringBuilder orderByBuild = new StringBuilder();
		if (ascArray != null && ascArray.length > 0) {
			for (int i = 0; i < ascArray.length; ++i) {
				orderByBuild.append(ascArray[i]);
				if (i < ascArray.length - 1) {
					orderByBuild.append(" ASC,");
				} else {
					orderByBuild.append(" ASC ");
				}
			}
		}
		String[] descArray = condition.getDesc();
		if (descArray != null && descArray.length > 0) {
			if (orderByBuild.length() > 0) {
				orderByBuild.append(",");
			}
			for (int i = 0; i < descArray.length; ++i) {
				orderByBuild.append(descArray[i]);
				if (i < descArray.length - 1) {
					orderByBuild.append(" DESC,");
				} else {
					orderByBuild.append(" DESC ");
				}
			}
		}
		return orderByBuild.toString();
	}

	public interface Callback<T, C> {
		void transform(T originalData, C targetData);
	}

}
