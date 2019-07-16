package xin.zero2one.url;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author zhoujundong
 * @data 5/15/2019
 * @description TODO
 */
public class UrlTest {

    @Test
    public void urlGet() throws MalformedURLException {
        System.setProperty("java.protocol.handler.pkgs","org.springframework.boot.loader");
        URL url = new URL("jar:/test");
    }
}
