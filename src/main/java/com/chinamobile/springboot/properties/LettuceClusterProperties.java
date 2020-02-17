package com.chinamobile.springboot.properties;

import lombok.Data;

/**
 * @description:
 * @author: shenjian
 * @create: 2020/2/13 17:14
 */
@Data
public class LettuceClusterProperties {

    private String nodes;
    private String password;
}
