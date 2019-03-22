import java.util.Scanner;

public class Student{
	
	public static void main(String[] args){
		Welcome();
		

		Admin[] admins =new Admin[5];//�ֲ�������ֻ����main������ʹ��
		initData(admins);
		
		operation(admins);

		}
		
		public static void Welcome(){
		System.out.println("=====��ӭ��¼ѧ������ϵͳ======");
		System.out.println("1.��¼    2.�˳�");
		System.out.println("-------------------------------");
	}
		public static void operation(Admin[] admins){
			System.out.print("��ѡ��");
			Scanner s = new Scanner(System.in);
			int operation = s.nextInt();
			if(operation == 1){
				//��¼
				Login(admins);
				
			}else if(operation == 2){
				//�˳�
				System.exit(0);
			}else{
				System.out.println("�����ֵ�������������롣");
				operation(admins);//����������ѭ���÷������ݹ����
			}	
		}
		
		public static void Login(Admin[] admins){
			System.out.println("��ӭ��¼");
			
			//��ȡ�û���������
			Scanner s = new Scanner(System.in);
			System.out.print("�������û�����");
			String username = s.nextLine();
			System.out.print("���������룺");
			String password = s.nextLine();
			//int num = 0;
			
			
			
			for(int i =0;i<admins.length;i++){
				
				Admin admin = admins[i];
				
				if(admins[i] == null){
					continue;
				}
				if(username.equals(admins[i].username) && password.equals(admins[i].password)){
					System.out.println("��¼�ɹ�");
					System.out.println("��ӭ����"+admins[i].username);
					
				}else if(i==admins.length){
					System.out.println("�û��������û�и��û�");
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
	public String username;//�û���
	public String password;//����
	
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
	//��ȡ�ı�id��ֵ
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	//��ȡ�ı�name��ֵ
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	//��ȡ�ı�sex��ֵ
	public String getSex(){
		return sex;
	}
	public void setSex(String sex){
		this.sex = sex;
	}
	//��ȡ�ı�age��ֵ
	public int  getAge(){
		return age;
	}
	public void setAge(int age){
		this.age = age;
	}
	//��ȡ�ı�_class��ֵ
	public String get_class(){
		return _class;
	}
	public void set_class(String _class){
		this._class = _class;
	}
	//��ȡ�ı�address��ֵ
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address = address;
	}
	//��ȡ�ı�phone��ֵ
	public String getPhone(){
		return phone;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	//��ȡ�ı�email��ֵ
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
}