package com.chinamobile.springboot.support;

import org.springframework.core.ConfigurableObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/20 23:18
 */
public class SpringJavaValueDecoder extends JavaValueDecoder {

    public static final SpringJavaValueDecoder INSTANCE = new SpringJavaValueDecoder(true);

    public SpringJavaValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @Override
    protected ObjectInputStream buildObjectInputStream(ByteArrayInputStream in) throws IOException {
        return new ConfigurableObjectInputStream(in, Thread.currentThread().getContextClassLoader());
    }
}
