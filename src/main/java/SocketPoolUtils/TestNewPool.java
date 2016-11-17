/**
 * @(#)TestNewPool.java, 16/11/8.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package SocketPoolUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *  * @author hzlvhaiyan@corp.netease.com on 16/11/8.  
 */
public class TestNewPool {
    public static void main(String args[]) {
        SocketAdapter connection = null;
        try {
            ConnectionProvider connectionProvider = MyConnectionProvider
                .newInstance();
            connection = connectionProvider.getConnection();
            System.out.println(connection.isFree());
            PrintWriter output = new PrintWriter(connection.getOutputStream(),
                true);
            output.write("test");
            output.flush();
            BufferedReader input = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
