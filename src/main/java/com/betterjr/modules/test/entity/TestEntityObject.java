package com.betterjr.modules.test.entity;

import java.io.Serializable;

import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "TEST_ENTITY")
public class TestEntityObject implements BetterjrEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "name", columnDefinition = "VARCHAR")
	private String name="userObject";
    
    @Column(name = "home", columnDefinition = "VARCHAR")
	private String home="for-web-test-input";
    
    @Column(name = "address", columnDefinition = "VARCHAR")
	private String address;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	
}
