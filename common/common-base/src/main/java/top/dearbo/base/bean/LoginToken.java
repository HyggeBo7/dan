package top.dearbo.base.bean;

/**
 * 登录
 *
 * @author wb
 */
public class LoginToken {
	private int userId;
	private String userName;
	private String appToken;
	private boolean superFlag;
	private String loginIp;

	private String module;

	public boolean isSuperFlag() {
		return superFlag;
	}

	public void setSuperFlag(boolean superFlag) {
		this.superFlag = superFlag;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAppToken() {
		return appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
}
