package top.dearbo.base.bean;

/**
 * @author wb
 */
public class PageQuery<T> extends BaseQuery {

	/**
	 * 查询条件
	 */
	private T condition;

	public T getCondition() {
		return condition;
	}

	public void setCondition(T condition) {
		this.condition = condition;
	}

	public T getCondition(Class<T> nullCreate) {
		if (condition == null) {
			try {
				condition = nullCreate.newInstance();
			} catch (InstantiationException | IllegalAccessException ignored) {
			}
		}
		return condition;
	}

}
