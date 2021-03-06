

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import constants.SystemConstants;
import entity.UserEntity;


/**
 * Servlet implementation class Member
 */
@WebServlet("/Member")
public class Member extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Member() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF8");

		String proc = request.getParameter("proc");
		String id = request.getParameter("id");

		if ("update".equals(proc) || "delete".equals(proc)) {
			UserEntity user = this.fetchUser(id);
			request.setAttribute("proc", proc);
			request.setAttribute("user", user);
		}
		RequestDispatcher dispatch = request.getRequestDispatcher("./member.jsp");
		dispatch.forward(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF8");

//		UserDAO dao = new UserDAO();

		String proc = request.getParameter("proc");
//		HttpSession session = request.getSession();

		//ユーザー情報
		UserEntity user =new UserEntity();
		user.setIdUser(request.getParameter("userid"));			//ID
		user.setIdLoginUser(request.getParameter("id"));		//ログインID
		user.setPassword(request.getParameter("pass"));			//パスワード
		user.setMeiUser(request.getParameter("name"));			//ユーザー名
		user.setAge(request.getParameter("age"));				//年齢
		user.setSeibetu(request.getParameter("seibetsu"));		//性別
		user.setCustom(request.getParameter("seibetsuText"));	//性別カスタム


		request.setAttribute("user", user);
		request.setAttribute("proc", proc);

		boolean hasError = false;
		RequestDispatcher dispatch = request.getRequestDispatcher("./member.jsp");

		//正規表現のステータス取得
		hasError = this.Validate(request, user);

		if ("delete".equals(proc)) {
			// 削除
			if (deleteUser(user)) {
				// 削除処理に問題有り
				dispatch = request.getRequestDispatcher("./member.jsp");
			} else {
				// 削除処理に問題無し
				request.setAttribute("result", "削除しました。");
				dispatch = request.getRequestDispatcher("./complete.jsp");
			}
		} else {

			if ("new".equals(proc)) {
				hasError = this.DuplicationCheck(request);
			} else if ("update".equals(proc)) {
				hasError = this.IDduplicationCheck(request, Integer.parseInt(user.getIdUser()));
			}

			if (hasError) {
				dispatch = request.getRequestDispatcher("./member.jsp");
			} else {
				//			session.setAttribute("user", user);
				dispatch = request.getRequestDispatcher("./confirm.jsp");
			}
		}

		dispatch.forward(request, response);

	}

	/**
	 * IDの重複check
	 * @param request
	 * @return
	 */
	public boolean DuplicationCheck(HttpServletRequest request) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int count = 0;
		String id = request.getParameter("id");
		if (!id.isEmpty()) {
			try {
				// MySQLドライバをロード
				Class.forName("com.mysql.jdbc.Driver");

				// データベースに接続
				conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

				// クエリ生成
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT COUNT(*) ");
				sb.append("FROM m_user ");
				sb.append("WHERE ");
				sb.append("id_login_user = ?;");

				String sql = sb.toString();

				pstmt = conn.prepareStatement(sql);
				// バインドパラメータをセット
				pstmt.setString(1, id);

				// SQL発行
				rset = pstmt.executeQuery();

				rset.next();

				count = rset.getInt(1);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// 結果セットをクローズ
					if (!rset.isClosed()) {
						rset.close();
					}

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

			if(count > 0) {
				request.setAttribute("ERROR_MSG_ID", SystemConstants.Error_msgID);
				return true;
			}
		}else {
			return true;
		}
		return false;
	}

	/**
	 * 入力チェック
	 * @param request
	 * @param user
	 * @return
	 */
	public boolean Validate(HttpServletRequest request,UserEntity user) {
		boolean hasError= false;



		//ID入力チェック
		if (StringUtils.isBlank(user.getIdLoginUser())) { //空白チェック
			request.setAttribute("ERROR_MSG_ID", SystemConstants.Error_msgEMPTY);
			hasError = true;
		} else if (user.getIdLoginUser().matches(SystemConstants.Regex001)) { //半角英数記号チェック
			request.setAttribute("ERROR_MSG_ID", SystemConstants.Error_msgHANKAKU_EISU);
			hasError = true;
		} else if (user.getIdLoginUser().getBytes().length > 20) { //文字数チェック
			request.setAttribute("ERROR_MSG_ID", SystemConstants.Error_msgMOJISU);
			hasError = true;
		}

		//PASSチェック
		if (StringUtils.isBlank(user.getPassword())) { //空白チェック
			request.setAttribute("ERROR_MSG_PASS", SystemConstants.Error_msgEMPTY);
			hasError = true;
		} else if (!user.getPassword().matches(SystemConstants.Regex001)) { //半角英数記号チェック
			request.setAttribute("ERROR_MSG_PASS", SystemConstants.Error_msgHANKAKU_EISU);
			hasError = true;
		} else if (user.getPassword().getBytes().length > 20) { //文字数チェック
			request.setAttribute("ERROR_MSG_PASS", SystemConstants.Error_msgMOJISU);
			hasError = true;
		}

		//氏名のチェック
		if (StringUtils.isBlank(user.getMeiUser())) {
			request.setAttribute("ERROR_MSG_NAME", SystemConstants.Error_msgEMPTY);
			hasError = true;
		} else if (!user.getMeiUser().matches(SystemConstants.Regex002)) {
			request.setAttribute("ERROR_MSG_NAME", SystemConstants.Error_msgZENKAKU);
			hasError = true;
		} else if (user.getMeiUser().getBytes().length > 20) {
			request.setAttribute("ERROR_MSG_NAME", SystemConstants.Error_msgMOJISU);
			hasError = true;
		}


		//年齢チェック
		Pattern p = Pattern.compile("^0+([0-9]+.*)");
		Matcher m = p.matcher(user.getAge());

		if (StringUtils.isBlank(user.getAge())) {
			request.setAttribute("ERROR_MSG_AGE", SystemConstants.Error_msgEMPTY);
			hasError = true;
		} else if (!user.getAge().matches(SystemConstants.Regex003)) {
			request.setAttribute("ERROR_MSG_AGE", SystemConstants.Error_msgSEISU);
			hasError = true;
		} else if (m.matches()) {
			request.setAttribute("ERROR_MSG_AGE", SystemConstants.Error_msgZERO_SUPP);
			hasError = true;
		} else if (user.getAge().getBytes().length > 3) {
			request.setAttribute("ERROR_MSG_AGE", SystemConstants.Error_msgMOJISU);
			hasError = true;
		}


		//性別（カスタム）チェック
		if (user.getSeibetu().isEmpty()) {
			if (StringUtils.isBlank(user.getCustom())) {
				request.setAttribute("ERROR_MSG_CUSTOM", SystemConstants.Error_msgEMPTY);
				hasError = true;
			} else if (user.getCustom().getBytes().length > 20) {
				request.setAttribute("ERROR_MSG_CUSTOM", SystemConstants.Error_msgMOJISU);
				hasError = true;
			}
		}
		return hasError;
	}



	// データベースからuserデータを取得し、userクラスに格納して返す。
	public UserEntity fetchUser(String id_user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		try {
			// MySQLドライバをロード
			Class.forName("com.mysql.jdbc.Driver");

			// データベースに接続
			conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

			// クエリ生成
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			sb.append("id_login_user");
			sb.append(", password");
			sb.append(", mei_user");
			sb.append(", seibetu");
			sb.append(", seibetu_custom");
			sb.append(", age ");
			sb.append("FROM m_user ");
			sb.append("WHERE ");
			sb.append("id_user = ?;");

			String sql = sb.toString();

			pstmt = conn.prepareStatement(sql);
			// バインドパラメータをセット
			pstmt.setInt(1, Integer.parseInt(id_user));

			// SQL発行
			rset = pstmt.executeQuery();

			rset.next();

			// userクラスに格納
			UserEntity user = new UserEntity();
			user.setIdLoginUser(rset.getString(1));
			user.setPassword(rset.getString(2));
			user.setMeiUser(rset.getString(3));
			user.setAge(rset.getString(6));
			if (rset.getString(4) == null) {
				user.setSeibetu("カスタム");
			} else {
				user.setSeibetu(rset.getString(4));
			}
			user.setCustom(rset.getString(5));
			user.setIdUser(id_user);

			return user;

		} catch (Exception e) {
			// エラーが出た場合nullを返す
			e.printStackTrace();
			return null;
		} finally {
			try {
				// 結果セットをクローズ
				if (!rset.isClosed()) {
					rset.close();
				}

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
	private boolean IDduplicationCheck(HttpServletRequest request, int id_user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int count = 1;
		String id = request.getParameter("id");
		if (!id.isEmpty()) {
			try {
				// MySQLドライバをロード
				Class.forName("com.mysql.jdbc.Driver");

				// データベースに接続
				conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

				// クエリ生成
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT COUNT(*) ");
				sb.append("FROM m_user ");
				sb.append("WHERE ");
				sb.append("id_user NOT IN (?)");
				sb.append("AND id_login_user = ?;");

				String sql = sb.toString();

				pstmt = conn.prepareStatement(sql);
				// バインドパラメータをセット
				pstmt.setInt(1, id_user);
				pstmt.setString(2, id);

				// SQL発行
				rset = pstmt.executeQuery();

				rset.next();

				count = rset.getInt(1);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// 結果セットをクローズ
					if (!rset.isClosed()) {
						rset.close();
					}

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
			if (count > 0) {
				request.setAttribute("ErrorID", "エラー");
				return true;
			}
		} else {
			return true;
		}
		return false;
	}



	//ユーザーを消去
	public boolean deleteUser(UserEntity user) {
		// 削除処理
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			// MySQLドライバをロード
			Class.forName("com.mysql.jdbc.Driver");

			// データベースに接続
			conn = DriverManager.getConnection(SystemConstants.DB_CON_STR);

			// クエリ生成
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM m_user ");
			sb.append("WHERE id_user = ?;");
			String sql = sb.toString();

			pstmt = conn.prepareStatement(sql);

			// バインドパラメータをセット
			pstmt.setInt(1, Integer.parseInt(user.getIdUser()));

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


