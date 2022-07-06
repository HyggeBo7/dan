package top.dearbo.base.bean;

import java.beans.Transient;
import java.io.Serializable;

/**
 * 基类枚举
 *
 * @author wb
 * @date 2022-07-06 10:17.
 */
public interface BaseEnum<K, V> extends Serializable {

	/**
	 * 获取枚举key值
	 *
	 * @return k
	 */
	@Transient
	K getKey();

	/**
	 * 获取枚举value值
	 *
	 * @return v
	 */
	@Transient
	V getValue();

	/**
	 * 获取枚举名称
	 *
	 * @return name
	 */
	String getName();


}
