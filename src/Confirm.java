
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constants.SystemConstants;
import entity.User;


/**
 * Servlet implementation class Confirm
 */
@WebServlet("/Confirm")
public class Confirm extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Confirm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF8");

		//ユーザー情報
		User user =new User();
		user.setIdUser(request.getParameter("userId"));			//ID
		user.setIdLoginUser(request.getParameter("id"));		//ログインID
		user.setPassword(request.getParameter("pass"));			//パスワード
		user.setMeiUser(request.getParameter("name"));			//ユーザー名
		user.setAge(request.getParameter("age"));				//年齢
		user.setSeibetu(request.getParameter("seibetsu"));		//性別
		user.setCustom(request.getParameter("seibetsuText"));	//性別カスタム

		String proc = request.getParameter("proc");
		String registration = request.getParameter("registration");
		String back = request.getParameter("back");



		request.setAttribute("user", user);
		request.setAttribute("proc", proc);

		RequestDispatcher dispatch = request.getRequestDispatcher("./complete.jsp");

		if ("new".equals(proc)) {

			if (insertUser(user)) {
				dispatch = request.getRequestDispatcher("./member.jsp");
			} else {
				dispatch = request.getRequestDispatcher("./complete.jsp");
				request.setAttribute("result", "登録しました。");
			}
		} else if ("登録".equals(registration)) {
			if (updateUser(user)) {
				dispatch = request.getRequestDispatcher("./member.jsp");
			} else {
				dispatch = request.getRequestDispatcher("./complete.jsp");
				request.setAttribute("result", "更新しました。");
			}

		} else if ("戻る".equals(back)) {

			dispatch = request.getRequestDispatcher("./member.jsp");
		}
		dispatch.forward(request, response);

	}

	//inset
	public boolean insertUser(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			// MySQLドライバをロード
			Class.forName("com.mysql.jdbc.Driver");

			// データベースに接続
			conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

			// クエリ生成
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO m_user (");
			sb.append("id_user");
			sb.append(", id_login_user");
			sb.append(", password");
			sb.append(", mei_user");
			sb.append(", seibetu");
			sb.append(", seibetu_custom");
			sb.append(", age");
			sb.append(") ");
			sb.append("VALUES (NULL, ?, ?, ?, ?, ?, ?);");
			String sql = sb.toString();

			pstmt = conn.prepareStatement(sql);

			// バインドパラメータをセット
			pstmt.setString(1, user.getIdLoginUser());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getMeiUser());

			switch (user.getSeibetu()) {
			case "0":
				pstmt.setString(4, "0");
				break;
			case "1":
				pstmt.setString(4, "1");
				break;
			default:
				pstmt.setNull(4, java.sql.Types.NULL);
			}

			pstmt.setString(5, user.getCustom());
			pstmt.setString(6, String.valueOf(Integer.parseInt(user.getAge())));

			// SQL発行
			pstmt.executeUpdate();

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		} finally {
			try {

				// ステートメントをクローズ
				if (!pstmt.isClosed()) {
					pstmt.close();
				}

				// 接続をクローズ
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}



	//update
	public boolean updateUser(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			// MySQLドライバをロード
			Class.forName("com.mysql.jdbc.Driver");

			// データベースに接続
			conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

			// クエリ生成
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE m_user ");
			sb.append("SET ");
			sb.append("id_login_user = ?");
			sb.append(", password = ?");
			sb.append(", mei_user = ?");
			sb.append(", seibetu = ?");
			sb.append(", seibetu_custom = ?");
			sb.append(", age = ? ");
			sb.append("WHERE ");
			sb.append("id_user = ?;");
			String sql = sb.toString();

			pstmt = conn.prepareStatement(sql);

			// パラメータをセット
			pstmt.setString(1, user.getIdLoginUser());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getMeiUser());

			switch (user.getSeibetu()) {
			case "0":
				pstmt.setString(4, "0");
				break;
			case "1":
				pstmt.setString(4, "1");
				break;
			default:
				pstmt.setNull(4, java.sql.Types.NULL);
			}

			pstmt.setString(5, user.getCustom());
			pstmt.setString(6, String.valueOf(Integer.parseInt(user.getAge())));
			pstmt.setInt(7, Integer.parseInt(user.getIdUser()));
			// SQL発行
			pstmt.executeUpdate();

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		} finally {
			try {

				// ステートメントをクローズ
				if (!pstmt.isClosed()) {
					pstmt.close();
				}

				// 接続をクローズ
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
