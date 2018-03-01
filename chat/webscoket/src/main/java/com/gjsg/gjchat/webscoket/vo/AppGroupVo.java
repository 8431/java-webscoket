package com.gjsg.gjchat.webscoket.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dlf
 * @since 2018-02-08
 */
public class AppGroupVo implements Serializable {

    private static final long serialVersionUID = 1L;
	@NotEmpty(message = "appGuid不能为空")
	private String appGuid;
	@NotEmpty(message = "appSecret不能为空")
	private String appSecret;
	//所在群
	@NotEmpty(message = "appGroup不能为空")
	private String appGroup;
	//第三方guid
	@NotEmpty(message = "第三方guid不能为空")
	private String thirdGuid;

	public String getAppGuid() {
		return appGuid;
	}

	public void setAppGuid(String appGuid) {
		this.appGuid = appGuid;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAppGroup() {
		return appGroup;
	}

	public void setAppGroup(String appGroup) {
		this.appGroup = appGroup;
	}

	public String getThirdGuid() {
		return thirdGuid;
	}

	public void setThirdGuid(String thirdGuid) {
		this.thirdGuid = thirdGuid;
	}
}
