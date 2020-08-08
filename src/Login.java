import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import constants.SystemConstants;
import entity.User;

/**
 * Servlet implementation class LoginSample
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF8");



		// 呼び出し元Jspからデータ受け取り
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");


		//インスタンス
		HttpSession session = request.getSession();
	    User user = new User();

		boolean isLogin = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			// MySQLドライバをロード
			Class.forName("com.mysql.jdbc.Driver");

			//インスタンス
			List<User> userList = new ArrayList<User>();


			// MySQLデータベースに接続
			conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

			// ステートメントを作成
			stmt = conn.createStatement();

			// クエリ生成
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT id_login_user, password ");
			sb.append("FROM m_user ");
			sb.append("WHERE id_login_user = '" + id + "'");
			String sql = sb.toString();
			System.out.println("SQL:" + sql);

			// SQL発行
			rset = stmt.executeQuery(sql);

			// 取得レコード数分、処理を繰り返しユーザー情報を設定する

			while (rset.next()) {
			    user.setIdLoginUser(rset.getString(1));
			    user.setPassword(rset.getString(2));
			    userList.add(user);
			}
			session.setAttribute("userInfo", user);


			// 取得レコード数分、処理を繰り返しログインチェックを行う
			for (User uid : userList) {
				if (id != null && pass != null
						&& id.equals(uid.getIdLoginUser()) && pass.equals(uid.getPassword())) {
					isLogin = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 結果セットをクローズ
				if (!rset.isClosed()) {
					rset.close();
				}

				// ステートメントをクローズ
				if (!stmt.isClosed()) {
					stmt.close();
				}

				// 接続をクローズ
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// ログインチェック
		if (isLogin) {
			// Jspに渡すデータセット
			System.out.println("ログインに成功しました。");

			//セッション
			user.setIdLoginUser(id);
			session.setAttribute("loginUser", user);

			// result.jsp にページ遷移
			RequestDispatcher dispatch = request.getRequestDispatcher("menu.jsp");
			dispatch.forward(request, response);
		} else {
			// Jspに渡すデータセット
			request.setAttribute("error_msg", SystemConstants.Error_msgID_PASS);
			System.out.println("ログインに失敗しました。");

			RequestDispatcher dispatch = request.getRequestDispatcher("login.jsp");
			dispatch.forward(request, response);
		}


	}
}