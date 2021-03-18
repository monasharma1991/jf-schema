package com.ril.fabric.schema.pojo;

import lombok.Data;
import org.springframework.data.annotation.*;

import java.util.Date;

@Data
public abstract class AbstractEntity{

	@Id
	protected String id;
	@CreatedDate
	protected Date createdDate;
	@CreatedBy
	protected String createdBy;
	@LastModifiedDate
	protected Date updatedDate;
	@LastModifiedBy
	protected String updatedBy;
	@Version
	protected Long version;
	
	public AbstractEntity() {
		this.createdDate = new Date();
		this.createdBy = "SYSTEM";
	}
}
