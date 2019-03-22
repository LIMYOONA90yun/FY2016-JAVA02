import java.util.Scanner;

public class Student{
	
	public static void main(String[] args){
		Welcome();
		

		Admin[] admins =new Admin[5];//局部变量，只能在main方法里使用
		initData(admins);
		
		operation(admins);

		}
		
		public static void Welcome(){
		System.out.println("=====欢迎登录学生管理系统======");
		System.out.println("1.登录    2.退出");
		System.out.println("-------------------------------");
	}
		public static void operation(Admin[] admins){
			System.out.print("请选择：");
			Scanner s = new Scanner(System.in);
			int operation = s.nextInt();
			if(operation == 1){
				//登录
				Login(admins);
				
			}else if(operation == 2){
				//退出
				System.exit(0);
			}else{
				System.out.println("输入的值有误，请重新输入。");
				operation(admins);//如果输入错误循环该方法，递归调用
			}	
		}
		
		public static void Login(Admin[] admins){
			System.out.println("欢迎登录");
			
			//获取用户名和密码
			Scanner s = new Scanner(System.in);
			System.out.print("请输入用户名：");
			String username = s.nextLine();
			System.out.print("请输入密码：");
			String password = s.nextLine();
			//int num = 0;
			
			
			
			for(int i =0;i<admins.length;i++){
				
				Admin admin = admins[i];
				
				if(admins[i] == null){
					continue;
				}
				if(username.equals(admins[i].username) && password.equals(admins[i].password)){
					System.out.println("登录成功");
					System.out.println("欢迎您："+admins[i].username);
					
				}else if(i==admins.length){
					System.out.println("用户名错误或没有该用户");
					Login(admins);
				}	
		}
						
			
			
		}
	
		public static void initData(Admin[] admins){
			Admin admin = new Admin("admin","admin");
			admins[0] = admin;
			Admin admin1 = new Admin("dandan","dandan");
			admins[1] = admin1;
			Admin admin2 = new Admin("bing","bing");
			admins[2] = admin2;
		}
	
}
class Admin{
	public String username;//用户名
	public String password;//密码
	
	public Admin(String username,String password){
		this.username = username;
		this.password = password;
	}
	
	
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	
}
class Students{
	
	public int id;
	public String name;
	public String sex;
	public int age;
	public String _class;
	public String address;
	public String phone;
	public String email;
	//获取改变id的值
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	//获取改变name的值
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	//获取改变sex的值
	public String getSex(){
		return sex;
	}
	public void setSex(String sex){
		this.sex = sex;
	}
	//获取改变age的值
	public int  getAge(){
		return age;
	}
	public void setAge(int age){
		this.age = age;
	}
	//获取改变_class的值
	public String get_class(){
		return _class;
	}
	public void set_class(String _class){
		this._class = _class;
	}
	//获取改变address的值
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address = address;
	}
	//获取改变phone的值
	public String getPhone(){
		return phone;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	//获取改变email的值
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
}