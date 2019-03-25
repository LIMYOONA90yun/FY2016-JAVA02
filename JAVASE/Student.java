import java.util.Scanner; //导入一个Scanner包

public class Student{
	
	public static void main(String[] args){
		Welcome();//调用登录界面
	
		Admin[] admins =new Admin[5];//局部变量，只能在main方法里使用
		initData(admins);//存储的用户名和密码信息，并返回值
		
		Students[] stud = new Students[10];
		study(stud);//存储学生信息的方法
		operation(admins,stud);//1登陆，2退出界面
		//addStudents(stud);
		}
		
		public static void Welcome(){
		System.out.println("=====欢迎登录学生管理系统======");
		System.out.println("1.登录    2.退出");
		System.out.println("-------------------------------");
	}
		public static void operation(Admin[] admins,Students[] stud){
			System.out.print("请选择：");
			Scanner s = new Scanner(System.in);
			int operation = s.nextInt();
			if(operation == 1){
				//登录
				Login(admins,stud);
				
			}else if(operation == 2){
				//退出
				System.exit(0);
			}else{
				System.out.println("输入的值有误，请重新输入。");
				operation(admins,stud);//如果输入错误循环该方法，递归调用
			}	
		}
		
		public static void Login(Admin[] admins,Students[] stud){
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
					System.out.println("用户名错误或没有该用户");
					System.out.println("");
					Login(admins,stud);
				}else if(username.equals(admins[i].username) && password.equals(admins[i].password)){
					System.out.println("登录成功");
					System.out.println("欢迎您："+admins[i].username);
					direction(admins,stud);

				}	
		}	
		}
		
		public static void addStudents(Admin[] admins,Students[] stud){
			System.out.println("****************************添加学生信息******************************");
			Scanner s = new Scanner(System.in);
			System.out.print("请输入学生ID：");
			int id = s.nextInt();
			for(int i = 0;i<stud.length;i++){
			if(stud[i]==null){
				break;
			}else if(stud[i].id ==id){
				System.out.println("此ID"+id+"存在，请重新输入");
				addStudents(admins,stud);
			}
			}
			Scanner s1 = new Scanner(System.in);
			System.out.print("请输入学生姓名：");
			String name = s1.nextLine();
			System.out.print("请输入学生性别：");
			String sex = s1.nextLine();
			
			System.out.print("请输入学生年龄(仅限于1-120之间)：");
			int age = s.nextInt();
			
			System.out.print("请输入学生地址：");
			String address = s1.nextLine();
			System.out.print("请输入学生的联系方式(11位手机号)：");
			String phone = s1.nextLine();
			
			System.out.print("请输入学生电子邮箱：");
			String email = s1.nextLine();
			System.out.println("数据保存成功，系统自动返回上层目录");
			direction(admins,stud);
			
		}
	
		public static void direction(Admin[] admins,Students[] stud){
			System.out.println("***********************请选择要操作的信息对应数字*************************");
			System.out.println("*1.查看学生信息  2.添加学生信息  3.删除学生信息  4.修改学生信息  5.退出  *");
			System.out.println("**************************************************************************");
			System.out.print("请选择：");
			Scanner s = new Scanner(System.in);
			int operation = s.nextInt();
			if(operation == 1){
				find(admins,stud);
			}else if(operation == 2){
				addStudents(admins,stud);
			}else if(operation == 3){
				
			}else if(operation == 4){
				modify(admins,stud);
			}else if(operation == 5){
				System.exit(0);
			}else{
				System.out.println("输入的值有误，请重新输入。");
				direction(admins,stud);//如果输入错误循环该方法，递归调用
			}
		}

		public static void modify(Admin[] admins,Students[] stud){
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("1.根据ID修改学生全部信息  2.根据ID修改学生部分信息  3.返回上级目录  4.系统退出");
			System.out.println("-------------------------------------------------------------------------------");
			System.out.print("请选择：");
			Scanner s = new Scanner(System.in);
			int operation = s.nextInt();
			if(operation == 1){
			
			}else if(operation == 2){
				part(admins,stud);
			}else if(operation == 3){
				direction(admins,stud);
			}else if(operation == 4){
				System.exit(0);
			}else{
				System.out.println("输入的值有误，请重新输入。");
				direction(admins,stud);//如果输入错误循环该方法，递归调用
			}
			
		}
		
		public static void part(Admin[] admins,Students[] stud){
			Scanner s = new Scanner(System.in);
			System.out.println("请输入要修改的ID");
			int id = s.nextInt();
			Scanner s1 = new Scanner(System.in);
			System.out.print("请输入要修改的属性");
			String attribute = s1.nextLine();
			System.out.print("请输入修改后的数据");
			String data = s1.nextLine();
			System.out.println("修改成功");
			System.out.println("系统自动返回上层目录......");
			modify(admins,stud);
		}
		
		
		
		public static void find(Admin[] admins,Students[] stud){
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("1.查看所有学生信息  2.根据ID查询学生信息  3.根据ID查询学生姓名  4.返回上一层"   );
			System.out.println("-------------------------------------------------------------------------------");
			
			System.out.print("请选择菜单：");
			Scanner s = new Scanner(System.in);
			int operation = s.nextInt();
			if(operation == 1){
			
			}else if(operation == 2){
				idInformation(admins,stud);
			}else if(operation == 3){
				
			}else if(operation == 4){
				direction(admins,stud);
			}
		}
		
		public static void idInformation(Admin[] admins,Students[] stud){
			Scanner s= new Scanner(System.in);
			System.out.print("请选择要查询的ID：");
			int id = s.nextInt();
			int id1 = id-1;
			if(stud[id1].id == 0){
				System.out.println("没有此ID"+id1+"的信息，请重新输入");
			}
			System.out.println(id1);
			System.out.println("----------------------------------所有学生信息------------------------------------");
			System.out.println("|学号  |姓名  |年龄  |性别  |年级  |电话        |EMAIL           |地址            ");
			System.out.println("----------------------------------------------------------------------------------");
			System.out.println("|"+stud[id1].id+"     "+"|"+stud[id1].name+"  "+"|"+stud[id1].age+"    "+"|"+stud[id1].sex+"    "+"|"+stud[id1]._class+"  "+"|"+stud[id1].phone+" "+"|"+stud[id1].email+"     "+"|"+stud[id1].address);
			System.out.println("查询完毕系统自动返回上层目录");
			find(admins,stud);
		}
	
		public static void initData(Admin[] admins){
			Admin admin = new Admin("admin","admin");
			admins[0] = admin;
			Admin admin1 = new Admin("dandan","dandan");
			admins[1] = admin1;
			Admin admin2 = new Admin("bing","bing");
			admins[2] = admin2;
		}
	
		public static void study(Students[] stud){
			Students stu0 = new Students(1,"张三","男",11,"初级","河北邯郸","12345612312","1231@qq.com");
			stud[0] = stu0;
			Students stu1 = new Students(2,"李四","男",21,"初级","河北邯郸","12345612312","1231@qq.com");
			stud[1] = stu1;
			Students stu2 = new Students(3,"老王","男",13,"初级","河北邯郸","12345612312","1231@qq.com");
			stud[2] = stu2;
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
	//定义学生类，
	public int id;
	public String name;
	public String sex;
	public int age;
	public String _class;
	public String address;
	public String phone;
	public String email;
	//学生的构造函数
	public Students(int id,String name,String sex,int age,String _class,String address,String phone,String email){
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this._class = _class;
		this.address = address;
		this.phone = phone;
		this.email = email;
		
	}
	
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