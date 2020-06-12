package entity;

public class User {
    /**
     * ユーザーID
     */
    private String idUser;

    /**
     * ログインユーザーID
     */
    private String idLoginUser;

    /**
     * パスワード
     */
    private String password;

    /**
     * ユーザー名
     */
    private String meiUser;

    /**
     * 性別（0：男／1：女／2：カスタム）
     */
    private String seibetu;

    /**
     * 年齢
     */
    private String age;

    /**
     * カスタム
     */
    private String custom;

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIdLoginUser() {
		return idLoginUser;
	}

	public void setIdLoginUser(String idLoginUser) {
		this.idLoginUser = idLoginUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMeiUser() {
		return meiUser;
	}

	public void setMeiUser(String meiUser) {
		this.meiUser = meiUser;
	}

	public String getSeibetu() {
		return seibetu;
	}

	public void setSeibetu(String seibetu) {
		this.seibetu = seibetu;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}


}