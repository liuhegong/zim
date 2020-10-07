package com.asm.zim.client.entry;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Data
public class Person implements Serializable {
	private String id;
	private String name;
	private String intro;
	public String header;
}
