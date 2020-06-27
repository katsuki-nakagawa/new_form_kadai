package entity;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import constants.SystemConstants;


public class UserDAO extends User{

	/**
	 *  ユーザーテーブルを取得するSQL
	 * @return
	 */
	private String selectUserSql() {

		StringBuilder sb = new StringBuilder();
    	String sql = null;

    	sb.append("SELECT");
    	sb.append("  ID_USER");
    	sb.append(" ,ID_LOGIN_USER");
    	sb.append(" ,PASSWORD");
    	sb.append(" ,MEI_USER");
    	sb.append(" ,SEIBETU");
    	sb.append(" ,AGE");
    	sb.append(" ,SEIBETU_CUSTOM");
    	sb.append(" FROM");
    	sb.append(" m_user");

    	sql = sb.toString();
    	return sql;
	}

	 /**
     * ユーザーテーブル取得
     * @param id
     * @return
	 * @throws SQLException
     * @throws Exception
     */
    public List<User> selectUser() throws Exception  {

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	List<User> userList = new ArrayList<User>();

    	try {
    		// MySQLドライバをロード
        	Class.forName("com.mysql.jdbc.Driver");

        	// MySQLデータベースに接続
        	conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

        	// ステートメントを作成
        	stmt = conn.createStatement();
        	// SQL発行
        	rs = stmt.executeQuery(selectUserSql());

        	// 取得レコード数分、処理を繰り返しユーザー情報を設定する
        	while (rs.next()) {
        	    User user = new User();
        	    user.setIdUser(rs.getString("ID_USER"));
        	    user.setIdLoginUser(rs.getString("ID_LOGIN_USER"));
        	    user.setPassword(rs.getString("PASSWORD"));
        	    user.setMeiUser(rs.getString("MEI_USER"));
        	    user.setSeibetu(rs.getString("SEIBETU"));
        	    user.setAge(rs.getString("AGE"));
        	    user.setCustom(rs.getString("SEIBETU_CUSTOM"));

        	    userList.add(user);
        	}

    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally {

    	}

    	// ステートメントをクローズ
        if(!stmt.isClosed()) {
            stmt.close();
        }


        // 接続をクローズ
        if(!conn.isClosed()) {
            conn.close();
        }

        // 結果セットをクローズ
        if(!rs.isClosed()) {
            rs.close();
        }

        return userList;
    }



	public boolean DuplicationCheck(HttpServletRequest request)throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
//		int count = 1;
		try {
			// MySQLドライバをロード
			Class.forName("com.mysql.jdbc.Driver");

			// MySQLデータベースに接続
			conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

			// ステートメントを作成
			stmt = conn.createStatement();
			// SQL発行
			rs = stmt.executeQuery(selectUserSql());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ステートメントをクローズ
			if (!stmt.isClosed()) {
				stmt.close();
			}

			// 接続をクローズ
			if (!conn.isClosed()) {
				conn.close();
			}

			// 結果セットをクローズ
			if (!rs.isClosed()) {
				rs.close();
			}
		}
		return false;

	}
}
