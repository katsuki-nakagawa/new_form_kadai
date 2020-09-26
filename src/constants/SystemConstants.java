package constants;

public class SystemConstants {
    // ------------------------ DB定義 ------------------------
    /** MySQL接続文字列 */
    public static final String DB_CON_STR = "jdbc:mysql://localhost:3306/sql_study?user=root&password=&useUnicode=true&characterEncoding=utf8";

    public static final String Error_msgID_PASS 	 = "IDまたはパスワードが違います";
    public static final String Error_msgEMPTY 		 = "入力必須項目です。";
    public static final String Error_msgHANKAKU_EISU = "半角英数記号で入力してください";
    public static final String Error_msgZENKAKU 	 = "全角入力してください。";
    public static final String Error_msgSEISU 		 = "整数で入力してください。";
    public static final String Error_msgMOJISU		 = "文字数制限を超えています。";
    public static final String Error_msgID		 	 = "このIDはすでに使われています";
    public static final String Error_msgZERO_SUPP    = "先頭に0を含めることはできません。";


    /**
     * 正規表現
     */
    public static final String Regex001 ="^[\\x20-\\x7F]*$";	//半角英数記号
//    public static final String Regex001 = "^[a-zA-Z0-9!-/:-@¥[-`{-~]*$"; 	//半角英数記号
    public static final String Regex002 = "^{1,20}[^ -~｡-ﾟ]+$";			//全角文字
    public static final String Regex003 = "^[0-9]*$";						//整数
    public static final String Regex004 = "^0+([0-9]+.*)";

}
