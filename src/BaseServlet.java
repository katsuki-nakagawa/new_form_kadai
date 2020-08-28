import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DataBaseAccess;
/**
 * サーブレット抽象クラス
 * すべてのサーブレットはこのサーブレットを継承して実装する
 */
public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 処理ステータス
	 */
	private boolean status;

	/**
	 * データベースアクセスライブラリ
	 */
	protected DataBaseAccess dba = null;

	/**
	 * GET処理
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		execute(request, response);
	}

	/**
	 * POST処理
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		execute(request, response);
	}

	/**
	 * サーブレット共通処理
	 * @param request
	 * @param response
	 * @param dba
	 * @throws Exception
	 */
	private void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 文字コード設定
		request.setCharacterEncoding("utf-8");

		// 処理ステータスを正常に設定
		this.setStatus(true);

		// DBコネクション生成
		dba = new DataBaseAccess();

		// コネクションの取得が出来ない場合
		if(dba.isConnClosed()) {
			// 共通エラーページに遷移させる
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

		// 認証エラーの場合
		if (!chkAuth(request, response)) {
			// ログインページに遷移させる
			request.getRequestDispatcher("login.jsp").forward(request, response);

			// DBのコネクションを切断
			this.dba.disConnection();
			return;
		}

		try {
			// メイン処理を呼び出し
			this.executeLogic(request, response);
		} catch (Exception ex) {
			// 例外発生時は処理ステータスに異常を設定
			this.setStatus(false);

			// 共通エラーページに遷移させる
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}

		// 処理ステータスによりコミットまたはロールバックを行う
		if (this.isStatus()) {
			this.dba.commit();
		} else {
			this.dba.rollback();
		}

		// DBのコネクションを切断
		this.dba.disConnection();

	}

	/**
	 * サーブレットにおけるメイン処理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected abstract void executeLogic(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 認証チェック
	 */
	protected boolean chkAuth(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		if(request.getSession().getAttribute("userinfo") == null) {
			return false;
		}
		return true;
	}

	/**
	 * ステータスを取得する
	 * @return status
	 */
	protected boolean isStatus() {
		return status;
	}

	/**
	 * ステータスをセットする
	 * @param status ステータス
	 */
	protected void setStatus(boolean status) {
		this.status = status;
	}

}
