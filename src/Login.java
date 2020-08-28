import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.UserEntity;

/**
 * ログインサーブレット
 */
@WebServlet("/Login")
public class Login extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * メイン処理
	 */
	protected void executeLogic(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 既にログイン済の場合はメニュー画面に遷移させる
		if (request.getSession().getAttribute("userinfo") != null) {
			request.getRequestDispatcher("menu.jsp").forward(request, response);
			return;
		}

		// 処理を分岐
		switch (Objects.toString(request.getParameter("exec"),"")) {
		case "login":
			if (login(request, response)) {
				request.getRequestDispatcher("menu.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
			break;
		default:
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}

	}

	/**
	 * 認証チェック
	 * ※ログイン画面は認証チェックをスルーさせる為、本メソッドをOverrideし処理をスルーさせる
	 */
	@Override
	protected boolean chkAuth(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return true;
	}

	/**
	 * ログイン処理
	 */
	private boolean login(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		// リクエスト情報取得
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");

		if (id == null || "".equals(id) || pass == null || "".equals(pass)) {
			request.setAttribute("errmsg", "ID又はパスワードが入力されていません。");
			return false;
		}

		/** ユーザーマスタ検索 */
		// DBアクセス用パラメータ設定
		List<String> paramList = new ArrayList<String>();
		paramList.add(id);

		// 単一レコード取得部品を使用しユーザー情報を取得
		UserEntity user = dba.selectOne("select * from m_user where id_login_user = ?", UserEntity.class, paramList);

		/** ログインチェック */
		// ユーザーマスタ存在チェック
		if (user == null) {
			request.setAttribute("errmsg", "ID又はパスワードが違います。");
			return false;
		}

		// ログインチェックを行う
		if (id.equals(user.getIdLoginUser()) && pass.equals(user.getPassword())) {
			// セッションの作成
			HttpSession session = request.getSession();
			session.setAttribute("userinfo", user);
			return true;
		}

		request.setAttribute("errmsg", "ID又はパスワードが違います。");
		return false;
	}

}
