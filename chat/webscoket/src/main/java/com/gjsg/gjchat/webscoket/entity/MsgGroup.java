package com.gjsg.gjchat.webscoket.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dlf
 * @since 2018-02-08
 */
@TableName("msg_group")
public class MsgGroup extends Model<MsgGroup> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	@TableField("guid")
	private String guid;
	@TableField("app_guid")
	private String appGuid;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getAppGuid() {
		return appGuid;
	}

	public void setAppGuid(String appGuid) {
		this.appGuid = appGuid;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
