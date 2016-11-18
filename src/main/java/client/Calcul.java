/**
 * @(#)Calcul.java, 16/10/31.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package client;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Calcul {
    public static void main(String[] args) {
        List<Long> list = new ArrayList<Long>();
        list.add(220000L);
        list.add(100000L);
        list.add(100200L);
        list.add(100000L);
        list.add(130000L);
        list.add(100000L);
        list.add(100200L);
        list.add(100400L);
        list.add(102000L);
        list.add(150000L);
        list.add(301000L);
        list.add(100800L);
        list.add(160000L);
        list.add(202000L);
        long ret = 0;
        long sum = 0;
        for (Long str: list) {
            sum = sum + str;
            //            System.out.println(sum);
            //            System.out.println("&&&&&&&&&&&");
            ret = ret + Math.round(str * 0.08 * 2 / 365);
        }
        System.out.println("已售卖的总额:" + sum);
        System.out.println("用户收益(不包含红包,按照年收益率8%计算):" + ret);
        System.out.println("用户收益加本金(按照单笔计算,即正确的交易所应转入数值):" + (sum + ret));
        System.out.println("交易所应转入,取售卖总额计算所得为:"
            + (Math.round(1966600 * 0.08 * 2 / 365) + 1966600));
        System.out.println("用户收益加红包收益加本金,即应转入分发户总额:" + (sum + ret + 381));
    }
}
