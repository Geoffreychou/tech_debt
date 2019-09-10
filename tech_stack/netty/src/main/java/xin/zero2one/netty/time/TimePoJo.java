package xin.zero2one.netty.time;


import java.util.Date;

/**
 * @author zhoujundong
 * @data 9/3/2019
 * @description TODO
 */
public class TimePoJo {

    private long value;

    public TimePoJo() {
        this.value = System.currentTimeMillis();
    }

    public TimePoJo(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new Date(value).toString();
    }
}
