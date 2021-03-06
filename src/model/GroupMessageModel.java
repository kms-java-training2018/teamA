package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import bean.GroupMessageBean;

public class GroupMessageModel {
    /**
     * 次の数字の確認
     * @param bean
     * @return
     */
    public GroupMessageBean nextNumCheck(GroupMessageBean bean) {
        // 初期化
        StringBuilder sb = new StringBuilder();

        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        }
        // 接続作成
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // SQL作成
            sb.append("SELECT ");
            sb.append("MAX(message_no) ");
            sb.append("FROM ");
            sb.append("t_message_info");

            // SQL実行
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());

            if (!rs.next()) {
                bean.setErrFlag(true);
            } else {
                bean.setMessageNo(String.valueOf(rs.getInt(1) + 1));
                conn.close();

            }

        } catch (SQLException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    /**
     * グループチェック
     * @param bean
     * @return
     */
    public GroupMessageBean groupCheck(GroupMessageBean bean) {
        // 初期化
        StringBuilder sb = new StringBuilder();
        String groupNo = bean.getGroupNo();
        String userNo = bean.getUserNo();

        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        }
        // 接続作成
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // SQL作成
            sb.append("SELECT ");
            sb.append(" user_no ");
            sb.append("FROM ");
            sb.append(" t_group_info ");
            sb.append("WHERE ");
            sb.append(" group_no = '" + groupNo + "'");
            sb.append(" AND user_no = '" + userNo + "'");
            sb.append(" AND out_flag = 0");

            // SQL実行
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());

            // グループに入ってない
            if (!rs.next()) {
                bean.setErrFlag(true);

            }

            conn.close();

        } catch (SQLException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    /**
     *	メッセージ送信処理
     * @param bean
     * @return
     */
    public GroupMessageBean sendMessage(GroupMessageBean bean) {
        // 初期化
        StringBuilder sb = new StringBuilder();
        String groupNo = bean.getGroupNo();
        String userNo = bean.getUserNo();
        String nextNo = bean.getMessageNo();
        String message = bean.getMessage();

        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        }
        // 接続作成
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // SQL作成
            sb.append("INSERT ");
            sb.append("INTO ");
            sb.append(" t_message_info (");
            sb.append(" message_no ");
            sb.append(" ,send_user_no  ");
            sb.append(" ,message");
            sb.append(" ,to_send_group_no");
            sb.append(" ,delete_flag");
            sb.append(" ,regist_date");
            sb.append(") values (");
            sb.append("'" + nextNo + "'");
            sb.append(" ,'" + userNo + "'");
            sb.append(" ,'" + message + "'");
            sb.append(" ,'" + groupNo + "'");
            sb.append(" ,0");
            sb.append(" ,sysdate)");

            // SQL実行
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());

            conn.close();

        } catch (SQLException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    /**
     * メッセージ削除処理
     * @param errFlag
     * @param delMessageNo
     * @return
     */
    public boolean DeleteMessage(boolean errFlag, String delMessageNo) {
        // 初期化
        StringBuilder sb = new StringBuilder();

        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            errFlag= true;
            e.printStackTrace();
        }
        // 接続作成
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // SQL作成
            sb.append("UPDATE ");
            sb.append(" t_message_info ");
            sb.append("SET ");
            sb.append(" delete_flag = 1 ");
            sb.append(" ,update_date = sysdate ");
            sb.append("WHERE ");
            sb.append(" message_no = '" + delMessageNo + "'");

            // SQL実行
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());

            conn.close();

        } catch (SQLException e) {
            errFlag = true;
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return errFlag;
    }

    /**	グループ退会処理
     * @param bean
     * @return
     */
    public GroupMessageBean escapeGroup(GroupMessageBean bean) {

        // 初期化
        StringBuilder sb = new StringBuilder();
        String groupNo = bean.getGroupNo();
        String userNo = bean.getUserNo();

        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        }
        // 接続作成
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // SQL作成
            sb.append("UPDATE ");
            sb.append(" t_group_info ");
            sb.append("SET ");
            sb.append(" out_flag = 1 ");
            sb.append("WHERE ");
            sb.append(" group_no = '" + groupNo + "'");
            sb.append(" AND user_no = '" + userNo + "'");
            sb.append(" AND out_flag = 0");

            // SQL実行
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());

            conn.close();

        } catch (SQLException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    /**
     *	グループ作成者チェック
     * @param bean
     * @param myUserNo
     * @return
     */
    public GroupMessageBean registCheck(GroupMessageBean bean,String myUserNo) {

        // 初期化
        StringBuilder sb = new StringBuilder();
        String groupNo = bean.getGroupNo();

        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        }
        // 接続作成
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // SQL作成
            sb.append("SELECT ");
            sb.append(" regist_user_no ");
            sb.append("FROM ");
            sb.append(" m_group ");
            sb.append("WHERE ");
            sb.append(" group_no = '" + groupNo + "'");

            // SQL実行
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());

            rs.next();
            if(rs.getString("regist_user_no").equals(myUserNo)) {
                bean.setErrFlag(true);
            }

            conn.close();

        } catch (SQLException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    /**
     * メッセージ取得処理
     * @param bean
     * @param myUserNo
     * @return
     */
    public ArrayList<GroupMessageBean> messageCheck(GroupMessageBean bean, String myUserNo) {

        // 初期化
        StringBuilder sb = new StringBuilder();
        String groupNo = bean.getGroupNo();
        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 接続作成
        ArrayList<GroupMessageBean> list = new ArrayList<GroupMessageBean>();
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // メッセージ一覧取得処理SQL作成
            sb.append("SELECT ");
            sb.append(" m_user.user_no ");
            sb.append(" ,m_user.user_name ");
            sb.append(" ,message_no ");
            sb.append(" ,message ");
            sb.append(" ,t_group_info.out_flag ");
            sb.append("FROM ");
            sb.append(" t_message_info ");
            sb.append("INNER JOIN ");
            sb.append(" m_user ");
            sb.append("ON ");
            sb.append(" t_message_info.send_user_no = m_user.user_no ");
            sb.append("INNER JOIN ");
            sb.append(" t_group_info ");
            sb.append("ON");
            sb.append(" t_message_info.to_send_group_no = t_group_info.group_no ");
            sb.append(" AND m_user.user_no = t_group_info.user_no ");
            sb.append("WHERE ");
            sb.append(" to_send_group_no ='" + groupNo + "'");
            sb.append(" AND delete_flag = 0");
            sb.append(" order by message_no");

            // SQL実行
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());

            while (rs.next()) {
                GroupMessageBean myMessage = new GroupMessageBean();
                // Listに追加
                myMessage.setMessageNo(rs.getString("message_no"));
                myMessage.setMessage(rs.getString("message"));
                myMessage.setSendUserName(rs.getString("user_name"));
                myMessage.setUserNo(rs.getString("user_no"));

                if (rs.getString("user_no").equals(myUserNo)) {
                    myMessage.setMyMessageFlag(true);
                } else {
                    myMessage.setMyMessageFlag(false);
                }

                if(rs.getString("out_flag").equals("1")) {
                    myMessage.setSendUserName("送信者不明");
                    //myMessage.setMyMessageFlag(true);
                }

                list.add(myMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        return list;
    }

    /**
     * グループ名取得処理
     * @param bean
     * @return
     */
    public GroupMessageBean getGroupName(GroupMessageBean bean) {

        // 初期化
        StringBuilder sb = new StringBuilder();
        String groupNo = bean.getGroupNo();

        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        }
        // 接続作成
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // SQL作成
            sb.append("SELECT ");
            sb.append(" group_name ");
            sb.append("FROM ");
            sb.append(" m_group ");
            sb.append("WHERE ");
            sb.append(" group_no = '" + groupNo + "'");

            // SQL実行
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());

            rs.next();

            bean.setGroupName(rs.getString("group_name"));

            conn.close();

        } catch (SQLException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    public ArrayList<String> getGroupMember(String groupno) {

        // 初期化
        StringBuilder sb = new StringBuilder();
        String groupNo = groupno;
        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 接続作成
        ArrayList<String> list = new ArrayList<String>();
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // メッセージ一覧取得処理SQL作成
            sb.append("SELECT ");
            sb.append(" user_name ");
            sb.append("FROM ");
            sb.append(" t_group_info ");
            sb.append("INNER JOIN ");
            sb.append(" m_user ");
            sb.append("ON ");
            sb.append(" t_group_info.user_no = m_user.user_no ");
            sb.append("WHERE ");
            sb.append(" t_group_info.group_no ='" + groupNo + "'");
            sb.append(" AND out_flag = 0");

            // SQL実行
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());

            while (rs.next()) {
                String groupMember = "";
                // Listに追加
                groupMember = rs.getString("user_name");

                list.add(groupMember);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        return list;
    }


    public GroupMessageBean deleteGroup(GroupMessageBean bean) {

        // 初期化
        StringBuilder sb = new StringBuilder();
        String groupNo = bean.getGroupNo();
        String userNo = bean.getUserNo();

        Connection conn = null;
        String url = "jdbc:oracle:thin:@192.168.51.67:1521:XE";
        String user = "DEV_TEAM_A";
        String dbPassword = "A_DEV_TEAM";
        // JDBCドライバーのロード
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        }
        // 接続作成
        try {
            conn = DriverManager.getConnection(url, user, dbPassword);

            // SQL作成
            sb.append("UPDATE ");
            sb.append(" m_group ");
            sb.append("SET ");
            sb.append(" delete_flag = 1 ");
            sb.append("WHERE ");
            sb.append(" group_no = '" + groupNo + "'");
            sb.append(" AND regist_user_no = '" + userNo + "'");
            sb.append(" AND delete_flag = 0");

            // SQL実行
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());

            conn.close();

        } catch (SQLException e) {
            bean.setErrFlag(true);
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }
}
