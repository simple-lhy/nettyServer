/**
 * @(#)TestMySocketPool.java, 16/11/10.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package MySocketPool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 */
public class TestMySocketPool {
    public static void main(String args[]) {

        new TestSocketPool().start();

        //        ConnectionProvider provider = MyConnectionProvider.newInstance();
        //        SocketAdapter socketAdapter = provider.getConnection();
        //        SocketAdapter socketAdapter1 = provider.getConnection();
        //        System.out.println(socketAdapter.isFree());
        //        try {
        //            PrintWriter output = new PrintWriter(
        //                socketAdapter.getOutputStream(), true);
        //            output.write("test");
        //            output.flush();
        //            BufferedReader input = new BufferedReader(
        //                new InputStreamReader(socketAdapter.getInputStream()));
        //            String line = null;
        //
        //            while ((line = input.readLine()) != null) {
        //                System.out.print("Socket alone:");
        //
        //                System.out.println(line);
        //            }
        //            input.close();
        //            output.close();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //
        //        } finally {
        //            if (socketAdapter != null) {
        //                try {
        //
        //                    socketAdapter.close();
        //                    //                    System.out.println(socketAdapter.isFree());
        //                } catch (IOException e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        }
    }

    static class TestSocketPool extends Thread {
        @Override
        public void run() {
            System.out.println("run thread to test socketpool");
            for (int i = 0; i < 5; i++) {
                ConnectionProvider provider = MyConnectionProvider
                    .newInstance();
                SocketAdapter socketAdapter = provider.getConnection();
                //                System.out.println(socketAdapter.isFree());
                try {
                    //                    PrintWriter output = new PrintWriter(
                    //                        socketAdapter.getOutputStream(), true);
                    BufferedWriter output = new BufferedWriter(
                        new OutputStreamWriter(
                            socketAdapter.getOutputStream()));
                    output.write("test1");
                    output.flush();
                    BufferedReader input = new BufferedReader(
                        new InputStreamReader(socketAdapter.getInputStream()));
                    String line = null;

                    while ((line = input.readLine()) != null) {
                        System.out.print("Socket" + i + ":");
                        System.out.println(line);
                        break;
                    }
                    //                    output.close();
                    //                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                } finally {
                    if (socketAdapter != null) {
                        try {
                            socketAdapter.close();
                                                        System.out.println("has closed");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
