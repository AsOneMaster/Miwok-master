package com.example.android.learnmiwok.ycontact.vo;


public class ContatUser {
	
    private Integer user_id;

    private Integer corps_id;

    private String user_name;

    private String gender;

    private String phone_num;

    private String password;

    private String qq_num;

    private String email;

    private Byte status;

    private String detail;
    
	public Integer getUser_id()
	{
		return user_id;
	}

	public void setUser_id(Integer user_id)
	{
		this.user_id = user_id;
	}

	public Integer getCorps_id()
	{
		return corps_id;
	}

	public void setCorps_id(Integer corps_id)
	{
		this.corps_id = corps_id;
	}

	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getPhone_num()
	{
		return phone_num;
	}

	public void setPhone_num(String phone_num)
	{
		this.phone_num = phone_num;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getQq_num()
	{
		return qq_num;
	}

	public void setQq_num(String qq_num)
	{
		this.qq_num = qq_num;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Byte getStatus()
	{
		return status;
	}

	public void setStatus(Byte status)
	{
		this.status = status;
	}

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	public ContatUser()
	{
	}
	
	public ContatUser(Integer user_id, Integer corps_id, String user_name,
			String gender, String phone_num, String password, String qq_num,
			String email, Byte status, String detail)
	{
		super();
		this.user_id = user_id;
		this.corps_id = corps_id;
		this.user_name = user_name;
		this.gender = gender;
		this.phone_num = phone_num;
		this.password = password;
		this.qq_num = qq_num;
		this.email = email;
		this.status = status;
		this.detail = detail;
	}

	
}