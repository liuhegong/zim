package com.asm.zim.common.constants;

/**
 * @Description 终端类型
 * @Author azhao
 */
public enum TerminalType {
    Android(1),
    WebSocket(2),
    Ios(3);
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    TerminalType(int value) {
        this.value = value;
    }
}
