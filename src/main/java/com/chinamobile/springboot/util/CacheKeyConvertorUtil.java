package com.chinamobile.springboot.util;

import com.chinamobile.springboot.exception.CacheException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/22 17:31
 */
public class CacheKeyConvertorUtil {

    /**
     * 将String、byte[]转换为byte[]
     * */
    public static byte[] convertObj2ByteArr(Object obj) throws IOException {

        if (obj == null) {
            throw new NullPointerException("obj is null!!!");
        }
        byte[] tmpByteArr = null;
        if (obj instanceof String) {
            tmpByteArr = obj.toString().getBytes("UTF-8");
        } else if (obj instanceof byte[]) {
            tmpByteArr = (byte[]) obj;
        } else if (obj instanceof Number) {
            tmpByteArr = (obj.getClass().getSimpleName() + obj).getBytes("UTF-8");
        } else if (obj instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss,SSS");
            tmpByteArr = (obj.getClass().getSimpleName() + sdf.format(obj)).getBytes();
        } else if (obj instanceof Boolean) {
            tmpByteArr = obj.toString().getBytes("UTF-8");
        } else if (obj instanceof Serializable) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(obj);
            os.close();
            bos.close();
            tmpByteArr = bos.toByteArray();
        } else {
            throw new CacheException("can't convert key of class: " + obj.getClass());
        }
        return tmpByteArr;
    }

}
