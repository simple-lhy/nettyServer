//
//package SocketPoolUtils;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.UnknownHostException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import com.wondersgroup.framework.core.web.action.xwork.AbstractAjaxAction;
//import com.wondersgroup.module.common.ExecuteSql;
//import com.wondersgroup.stjt.mail.util.*;
//import com.wondersgroup.stjt.util.StringUtil;
//
//public class ShmetromailAction extends AbstractAjaxAction {
//    //private String emailDomain = "@shmetro.com";
//    //private String host = "222.66.3.199";
//    //private int port = 8888;
//    private String errMsg = "-";
//
//    private String okMsg = "+";
//
//    public String getMailDetail() throws UnknownHostException {
//
//        //获得用户名
//
//        String curLoginName = (String) this.getRequest().getSession()
//            .getAttribute("login_name");
//        String mailLoginName = "";
//        String mailPwd = "";
//
//        //取得用户email地址和email密码！
//
//        ExecuteSql dealsql = new ExecuteSql();
//        String sql = "select t.id,t.email from cs_user t where t.login_name='"
//            + curLoginName + "'";
//        //System.out.println("sql");
//        //dealsql.ExecuteSql(sql);
//        //String mailLoginName="";
//        int iuserid = 0;
//        //String mailPwd="";
//        try {
//            ResultSet rs = dealsql.ExecuteDemandSql(sql);
//
//            if (rs.next()) {
//                iuserid = rs.getInt("ID");
//                mailLoginName = rs.getString("EMAIL");
//                //System.out.println(iuserid+"----"+mailLoginName);
//            }
//            sql = "select t.email_passwd from t_cs_user t where t.id='"
//                + iuserid + "'";
//            rs = dealsql.ExecuteDemandSql(sql);
//            if (rs.next()) {
//
//                mailPwd = rs.getString("email_passwd");
//                //System.out.println(iuserid+"--++--"+mailPwd);
//            }
//            rs.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            createJSonData(
//                "{\"success\":false, \"results\": \"connectionerr\"}");
//            return AJAX;
//        } finally {
//
//            try {
//                dealsql.close();
//            } catch (SQLException e) {
//
//                e.printStackTrace();
//                createJSonData(
//                    "{\"success\":false, \"results\": \"connectionerr\"}");
//                return AJAX;
//            }
//        }
//
//        //判断地址，密码和发性
//
//        //Socket connection=null;
//        if (StringUtil.isNull(mailLoginName)
//            || !mailLoginName.matches("\\S+@shmetro\\.com")
//            || StringUtil.isNull(mailPwd)) {
//            createJSonData(
//                "{\"success\":false, \"results\": \"errgetemailifo\"}");
//            return AJAX;
//        }
//        SocketAdapter connection = null;
//        int allMailCount = 0;
//        int unReadCount = 0;
//        // connect to server
//        try {
//            //获取新的connection
//            ConnectionProvider conpool = MyConnectionProvider.newInstance();
//
//            connection = conpool.getConnection();
//
//            if (connection == null) {
//                createJSonData(
//                    "{\"success\":false, \"results\": \"errgetconnection\"}");
//                return AJAX;
//            }
//            //so1.setKeepAlive(true);
//            //connection=so1;
//
//            BufferedReader input = new BufferedReader(
//                new InputStreamReader(connection.getInputStream()));// 接受
//            PrintWriter out = new PrintWriter(connection.getOutputStream(),
//                true/* autoFlush */);// 传输
//
//            String info = null;// 接受信息
//            // read information from server
//            info = input.readLine();
//
//            //输入邮件地址，密码
//
//            BufferedReader in = new BufferedReader(
//                new InputStreamReader(System.in));
//            String sInput = null;
//            String line = null;
//            out.println("USER " + mailLoginName);
//            System.out.println("USER " + mailLoginName);
//            out.flush();
//            line = input.readLine();
//            System.out.println("line " + line);
//            if (StringUtil.isNull(line) || line.startsWith(errMsg)) {
//                createJSonData(
//                    "{\"success\":false, \"results\": \"errloginname\"}");
//                return AJAX;
//            }
//            //System.out.println("1"+line);
//            out.println("PASS " + mailPwd);
//            out.flush();
//            line = input.readLine();
//            //System.out.println("line2 " + line);
//            if (StringUtil.isNull(line) || line.startsWith(errMsg)) {
//                createJSonData("{\"success\":false, \"results\": \"errpwd\"}");
//                return AJAX;
//            }
//            //System.out.println("2"+line);
//            //进入目录查找邮件信息。
//
//            out.println("CHDIR inbox");
//            line = input.readLine();
//            if (StringUtil.isNull(line) || line.startsWith(errMsg)) {
//                createJSonData(
//                    "{\"success\":false, \"results\": \"errchdir\"}");
//                return AJAX;
//            }
//            //System.out.println("3"+line);
//            out.println("LIST");
//            line = input.readLine();
//            if (StringUtil.isNull(line) || line.startsWith(errMsg)) {
//                createJSonData(
//                    "{\"success\":false, \"results\": \"errchdir\"}");
//                return AJAX;
//            }
//            int i = 0;
//            //获取邮件信息并统计
//
//            while ((line = input.readLine()) != null) {
//                //System.out.println(i++);
//                int temp = getMailInfo(line);
//                if (temp != -1) {
//                    if (temp != -2) {
//                        allMailCount++;
//                        if (temp == 0) {
//                            unReadCount++;
//                        }
//                    } else {
//
//                        break;
//                    }
//                } else {
//                    createJSonData(
//                        "{\"success\":false, \"results\": \"errmailinfo\"}");
//                    return AJAX;
//                }
//                //System.out.println(line);
//            }
//            //System.out.println(allMailCount+"----"+unReadCount);
//
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//
//        } finally {
//            if (connection != null)
//                try {
//                    connection.close();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//        }
//        //返回。
//
//        createJSonData("{\"success\":true, \"results\":{\"allMailCount\": \""
//            + allMailCount + "\", \"unReadCount\": \"" + unReadCount + "\"}}");
//        return AJAX;
//    }
//
//    private static int getMailInfo(String line) {
//        if (line.matches("\\S+\\s+\\S+\\s+\\d+")) {
//            String re = line.split("\\s+")[2];
//            return Integer.parseInt(re);
//        }
//        //System.out.println("==="+line+"===");
//        if (line.matches(".")) {
//            //System.out.println("yes");
//            return -2;
//        }
//        return -1;
//    }
//
//    public static void main(String[] args) throws IOException {
//        String errMsg = "-";
//        String okMsg = "+";
//        String mailLoginName = "limingmin@shmetro.com";
//        String mailPwd = "1111";
//
//        SocketAdapter connection = null;
//        int allMailCount = 0;
//        int unReadCount = 0;
//        // connect to server
//        try {
//            //获取新的connection
//            ConnectionProvider conpool = MyConnectionProvider.newInstance();
//
//            connection = conpool.getConnection();
//
//            if (connection == null) {
//
//            }
//            //so1.setKeepAlive(true);
//            //connection=so1;
//
//            BufferedReader input = new BufferedReader(
//                new InputStreamReader(connection.getInputStream()));// 接受
//            PrintWriter out = new PrintWriter(connection.getOutputStream(),
//                true/* autoFlush */);// 传输
//
//            String info = null;// 接受信息
//            // read information from server
//            //info = input.readLine();
//
//            //输入邮件地址，密码
//
//            //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            String sInput = null;
//            String line = null;
//            out.println("USER " + mailLoginName);
//            System.out.println("USER " + mailLoginName);
//            out.flush();
//            line = input.readLine();
//            System.out.println("line " + line);
//            if (StringUtil.isNull(line) || line.startsWith(errMsg)) {
//
//            }
//            //System.out.println("1"+line);
//            out.println("PASS " + mailPwd);
//            out.flush();
//            line = input.readLine();
//            //System.out.println("line2 " + line);
//            if (StringUtil.isNull(line) || line.startsWith(errMsg)) {
//
//            }
//            //System.out.println("2"+line);
//            //进入目录查找邮件信息。
//
//            out.println("CHDIR inbox");
//            line = input.readLine();
//            if (StringUtil.isNull(line) || line.startsWith(errMsg)) {
//
//            }
//            //System.out.println("3"+line);
//            out.println("LIST");
//            line = input.readLine();
//            if (StringUtil.isNull(line) || line.startsWith(errMsg)) {
//
//            }
//            int i = 0;
//            //获取邮件信息并统计
//
//            while ((line = input.readLine()) != null) {
//                //System.out.println(i++);
//                int temp = getMailInfo(line);
//                if (temp != -1) {
//                    if (temp != -2) {
//                        allMailCount++;
//                        if (temp == 0) {
//                            unReadCount++;
//                        }
//                    } else {
//
//                        break;
//                    }
//                } else {
//
//                }
//                //System.out.println(line);
//            }
//            //System.out.println(allMailCount+"----"+unReadCount);
//
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            System.out.println("111111111111111111111111111111111111111");
//        } finally {
//            if (connection != null)
//                try {
//                    connection.close();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//        }
//
//    }
//
//}
