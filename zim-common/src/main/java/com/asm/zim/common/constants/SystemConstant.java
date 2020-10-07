package com.asm.zim.common.constants;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description 系统
 * @Author azhao
 */
public class SystemConstant {
    /**
     * 登录
     */
    public static final int Login = 1;
    /**
     * 登录成功
     */
    public static final int LoginSuccess = 2;
    /**
     * 注册
     */
    public static final int register = 3;

    private static Set<Integer> codeSet = new HashSet<>();

    static {
        Field[] fields = SystemConstant.class.getFields();
        for (Field field : fields) {
            try {
                codeSet.add((Integer) field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }


    public static Set<Integer> getCodeSet() {
        return codeSet;
    }
}
