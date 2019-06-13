package com.neuedu;
import com.neuedu.*;
import java.util.Scanner;
public class smallProject {

	public static void main(String[] args) throws RegisterException{
		// TODO Auto-generated method stub
		
		RealUserBiz cc = new RealUserBiz();
		RealUserBiz.signin(cc.user);
		RealUserView re = new RealUserView();
		re.login();
		re.register();
		RealUserBiz us = new RealUserBiz();
		RealUserBiz.signin(cc.user);
	
	}

}

interface UserBiz{
	 void register(String username, String password, String password2,
	 String name, String email) throws RegisterException; //用户注册
	 void login(String username, String password) throws LoginException; //用户登录
}

interface UserView{
	 void login();
	 void register();
}
class RealUserView implements UserView{

	@SuppressWarnings("unused")
	//@Override
	RealUserBiz r = new RealUserBiz();
	public void login() {
		// TODO Auto-generated method stub
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("请输入用户名");
	    String username =s.nextLine() ;
	    System.out.println("请输入密码");
	    String password =s.nextLine() ;
	    System.out.println("再次输入密码");
	    String password2 =s.nextLine() ;
		try {
	    r.login(username,password);
    }catch(LoginException a) {}
	}

	@Override
	public void register() throws RegisterException{
	// TODO Auto-generated method stub
		Scanner s = new Scanner(System.in);
		
		System.out.println("请输入用户名");
	    String username =s.nextLine() ;
	    System.out.println("请输入密码");
	    String password =s.nextLine() ;
	    System.out.println("请确认密码");
	    String password2 =s.nextLine() ;
	    System.out.println("请输入真实姓名");
	    String name =s.nextLine() ;
	    System.out.println("请输入电子邮件");
	    String email =s.nextLine() ;
	    try {
	    r.register(username,password,password,name,email);
	    }catch(RegisterException a) {}
	}
	
}

class RealUserBiz implements UserBiz{

	//private Object user[];
	User [] user = new User[5];
	@Override
	public void register(String username, String password, String password2, String name, String email)
			throws RegisterException {
		// TODO Auto-generated method stub
		
		for(int i=0;i<user.length;i++) {
			User use = (User) user[i];
			if(use.username == username & password != password2) {
				throw new RegisterException();
			}
		}	
		
		
	}
	@Override
	public void login(String username, String password) throws LoginException {
		// TODO Auto-generated method stub
		
		try {
			
			for(int i=0;i<user.length;i++) {
			//User use = (User) user[i];
				if(user[i].username == null&user[i].username != username & user[i].password != password) {
				throw new LoginException();
			}else if (user[i].username == username) {
				System.out.print("aaaa");
			}
			}
			
		}catch(NullPointerException n) {}
			
	}
	
	public static void signin(User[] user) {
		
		User user1 = new User("admin","admin"," Administrator","admin@123.com");
		user[0] = user1;
		User user2 = new User("tom","cat"," tomcat","tomcat@cat.com");
		user[1] = user2;
		
	
	}
	
}

class User{
	public String username;
	public String password;
	public String name;
	public String email;

	public User(String username, String password, String name, String email) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
