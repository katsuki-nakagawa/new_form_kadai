package util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DBアクセスクラス
 * @author shimada
 *
 */
public class DataBaseAccess {

	/** DBコネクション */
	private Connection conn = null;

	/** DBカラム名 スネークケース有無 */
	private final boolean dbColSnake = true;

	/** SQLログ出力有無 */
	private final boolean sqlLogOut = true;

	/**
	 * コンストラクタ
	 */
	public DataBaseAccess() {
		getConnection();
	}

	/**
	 * DBコネクションを取得する
	 */
	public void getConnection() {
		try {
			if (conn != null) {
				return;
			}

			// MySQLドライバをロード
			Class.forName("com.mysql.jdbc.Driver");

			// MySQLデータベースに接続
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sql_study?user=root&password=&characterEncoding=utf8");

			// オートコミットを無効にする
			conn.setAutoCommit(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * コネクションクローズ可否取得
	 * @return コネクションクローズ可否（true:閉じている/false:開いている）
	 */
	public boolean isConnClosed() {
		try {
			return conn.isClosed();
		} catch (Exception ex) {
			return true;
		}
	}

	/**
	 * コミットを行う
	 */
	public void commit() {
		try {
			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ロールバックを行う
	 */
	public void rollback() {
		try {
			conn.rollback();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * DBコネクションを切断する
	 */
	public void disConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * PreparedStatementへパラメーターをセットする
	 * @param statement プリぺアードステートメント
	 * @param params パラメータ（可変）
	 * @throws SQLException
	 */
	private void setParams(PreparedStatement statement, Object... params) throws SQLException {
		int paramNo = 1;
		for (Object param : params) {
			if (param instanceof ArrayList) {
				List<?> paramList = (ArrayList<?>) param;
				for (Object para : paramList) {
					if (para.equals("NULL")) {
						statement.setNull(paramNo++, java.sql.Types.NULL);
					} else {
						statement.setObject(paramNo++, para);
					}
				}
			} else {
				if (param.equals("NULL")) {
					statement.setNull(paramNo++, java.sql.Types.NULL);
				} else {
					statement.setObject(paramNo++, param);
				}
			}
		}
	}

	/**
	 * DBデータから指定されたクラスのオブジェクトのリストへ変換する
	 * @param rs リザルトセット
	 * @param clazz ビーンクラス
	 * @return ビーンリスト
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private <E> List<E> toObjectList(ResultSet rs, Class<E> clazz)
			throws SQLException, InstantiationException, IllegalAccessException {

		List<E> rsList = new ArrayList<>();

		while (rs.next()) {
			Field[] fields = clazz.getDeclaredFields();
			E bean = clazz.newInstance();

			for (Field f : fields) {
				String colName = f.getName();

				// DBのカラム名がスネークケースでの定義であれば、キャメルに置き換える
				if (this.dbColSnake) {
					colName = this.camelToSnake(colName);
				}

				f.setAccessible(true);
				try {
					if (rs.getString(colName) != null) {
						f.set(bean, rs.getString(colName));
					}
				} catch (Exception ex) {
					System.out.println("Beanクラスに値セット不可：" + clazz.getName() + "／フィールド：" + colName);
				}
			}
			rsList.add(bean);
		}
		return rsList;
	}

	/**
	 * insertやupdate等を実行する
	 * @param sql SQL
	 * @param params パラメータ（可変）
	 * @return
	 * @throws SQLException
	 */
	public int update(String sql, Object... params) throws SQLException {
		try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
			this.setParams(statement, params);

			sqlLog(sql, params);
			commit();
			return statement.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException();
		}
	}

	/**
	 * 1レコードのみ取得する
	 * @param sql SQL
	 * @param clazz ビーンクラス
	 * @param params パラメータ（可変）
	 * @return
	 * @throws SQLException
	 */
	public <E> E selectOne(String sql, Class<E> clazz, Object... params) throws SQLException {
		List<E> list = selectList(sql, clazz, params);
		if (list != null) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 複数レコードを取得します
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> selectList(String sql, Class<E> clazz, Object... params) throws SQLException {
		ResultSet rs = null;
		try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
			this.setParams(statement, params);

			sqlLog(sql, params);
			rs = statement.executeQuery();
			List<E> rsList = this.toObjectList(rs, clazz);

			if (rsList.size() > 0) {
				return rsList;
			}

			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * キャメルケースをスネークケースに変換(idUser→id_user)
	 * @param camel
	 * @return
	 */
	private String camelToSnake(final String camel) {
		if (camel != null && "".equals(camel)) {
			return camel;
		}

		final StringBuilder sb = new StringBuilder(camel.length() + camel.length());

		for (int i = 0; i < camel.length(); i++) {
			final char c = camel.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(sb.length() != 0 ? '_' : "").append(Character.toLowerCase(c));
			} else {
				sb.append(Character.toLowerCase(c));
			}
		}
		return sb.toString();
	}

	/**
	 * SQLログを出力
	 * @param sql SQL
	 * @param params パラメータ（可変）
	 */
	private void sqlLog(String sql, Object... params) {
		if (!sqlLogOut) {
			return;
		}

		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("SQL:" + sql);

		System.out.print("SQLパラメータ:");
		boolean firstFlg = true;

		for (Object param : params) {
			if (!firstFlg) {
				System.out.print(", ");
			}
			System.out.print(param);
		}
		System.out.println("\n-----------------------------------------------------------------------------");
	}
}
