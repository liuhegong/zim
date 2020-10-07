package com.asm.zim.common.utils;

import io.netty.buffer.ByteBuf;

import java.io.*;

public class ByteUtils {
	public static byte[] read(ByteBuf datas) {
		byte[] bytes = new byte[datas.readableBytes()];
		datas.readBytes(bytes);
		return bytes;
	}
	
	public static Object byteToObject(byte[] bytes) {
		Object obj = null;
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = null;
		try {
			oi = new ObjectInputStream(bi);
			obj = oi.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				oi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	public static byte[] objectToByte(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = null;
		try {
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bo.close();
				if (oo!=null){
					oo.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}
}
