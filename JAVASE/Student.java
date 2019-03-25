import java.util.Scanner; //����һ��Scanner��

public class Student{
	
	public static void main(String[] args){
		Welcome();//���õ�¼����
	
		Admin[] admins =new Admin[5];//�ֲ�������ֻ����main������ʹ��
		initData(admins);//�洢���û�����������Ϣ��������ֵ
		
		Students[] stud = new Students[10];
		study(stud);//�洢ѧ����Ϣ�ķ���
		operation(admins,stud);//1��½��2�˳�����
		//addStudents(stud);
		}
		
		public static void Welcome(){
		System.out.println("=====��ӭ��¼ѧ������ϵͳ======");
		System.out.println("1.��¼    2.�˳�");
		System.out.println("-------------------------------");
	}
		public static void operation(Admin[] admins,Students[] stud){
			System.out.print("��ѡ��");
			Scanner s = new Scanner(System.in);
			int operation = s.nextInt();
			if(operation == 1){
				//��¼
				Login(admins,stud);
				
			}else if(operation == 2){
				//�˳�
				System.exit(0);
			}else{
				System.out.println("�����ֵ�������������롣");
				operation(admins,stud);//����������ѭ���÷������ݹ����
			}	
		}
		
		public static void Login(Admin[] admins,Students[] stud){
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
					System.out.println("�û��������û�и��û�");
					System.out.println("");
					Login(admins,stud);
				}else if(username.equals(admins[i].username) && password.equals(admins[i].password)){
					System.out.println("��¼�ɹ�");
					System.out.println("��ӭ����"+admins[i].username);
					direction(admins,stud);

				}	
		}	
		}
		
		public static void addStudents(Admin[] admins,Students[] stud){
			System.out.println("****************************���ѧ����Ϣ******************************");
			Scanner s = new Scanner(System.in);
			System.out.print("������ѧ��ID��");
			int id = s.nextInt();
			for(int i = 0;i<stud.length;i++){
			if(stud[i]==null){
				break;
			}else if(stud[i].id ==id){
				System.out.println("��ID"+id+"���ڣ�����������");
				addStudents(admins,stud);
			}
			}
			Scanner s1 = new Scanner(System.in);
			System.out.print("������ѧ��������");
			String name = s1.nextLine();
			System.out.print("������ѧ���Ա�");
			String sex = s1.nextLine();
			
			System.out.print("������ѧ������(������1-120֮��)��");
			int age = s.nextInt();
			
			System.out.print("������ѧ����ַ��");
			String address = s1.nextLine();
			System.out.print("������ѧ������ϵ��ʽ(11λ�ֻ���)��");
			String phone = s1.nextLine();
			
			System.out.print("������ѧ���������䣺");
			String email = s1.nextLine();
			System.out.println("���ݱ���ɹ���ϵͳ�Զ������ϲ�Ŀ¼");
			direction(admins,stud);
			
		}
	
		public static void direction(Admin[] admins,Students[] stud){
			System.out.println("***********************��ѡ��Ҫ��������Ϣ��Ӧ����*************************");
			System.out.println("*1.�鿴ѧ����Ϣ  2.���ѧ����Ϣ  3.ɾ��ѧ����Ϣ  4.�޸�ѧ����Ϣ  5.�˳�  *");
			System.out.println("**************************************************************************");
			System.out.print("��ѡ��");
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
				System.out.println("�����ֵ�������������롣");
				direction(admins,stud);//����������ѭ���÷������ݹ����
			}
		}

		public static void modify(Admin[] admins,Students[] stud){
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("1.����ID�޸�ѧ��ȫ����Ϣ  2.����ID�޸�ѧ��������Ϣ  3.�����ϼ�Ŀ¼  4.ϵͳ�˳�");
			System.out.println("-------------------------------------------------------------------------------");
			System.out.print("��ѡ��");
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
				System.out.println("�����ֵ�������������롣");
				direction(admins,stud);//����������ѭ���÷������ݹ����
			}
			
		}
		
		public static void part(Admin[] admins,Students[] stud){
			Scanner s = new Scanner(System.in);
			System.out.println("������Ҫ�޸ĵ�ID");
			int id = s.nextInt();
			Scanner s1 = new Scanner(System.in);
			System.out.print("������Ҫ�޸ĵ�����");
			String attribute = s1.nextLine();
			System.out.print("�������޸ĺ������");
			String data = s1.nextLine();
			System.out.println("�޸ĳɹ�");
			System.out.println("ϵͳ�Զ������ϲ�Ŀ¼......");
			modify(admins,stud);
		}
		
		
		
		public static void find(Admin[] admins,Students[] stud){
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("1.�鿴����ѧ����Ϣ  2.����ID��ѯѧ����Ϣ  3.����ID��ѯѧ������  4.������һ��"   );
			System.out.println("-------------------------------------------------------------------------------");
			
			System.out.print("��ѡ��˵���");
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
			System.out.print("��ѡ��Ҫ��ѯ��ID��");
			int id = s.nextInt();
			int id1 = id-1;
			if(stud[id1].id == 0){
				System.out.println("û�д�ID"+id1+"����Ϣ������������");
			}
			System.out.println(id1);
			System.out.println("----------------------------------����ѧ����Ϣ------------------------------------");
			System.out.println("|ѧ��  |����  |����  |�Ա�  |�꼶  |�绰        |EMAIL           |��ַ            ");
			System.out.println("----------------------------------------------------------------------------------");
			System.out.println("|"+stud[id1].id+"     "+"|"+stud[id1].name+"  "+"|"+stud[id1].age+"    "+"|"+stud[id1].sex+"    "+"|"+stud[id1]._class+"  "+"|"+stud[id1].phone+" "+"|"+stud[id1].email+"     "+"|"+stud[id1].address);
			System.out.println("��ѯ���ϵͳ�Զ������ϲ�Ŀ¼");
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
			Students stu0 = new Students(1,"����","��",11,"����","�ӱ�����","12345612312","1231@qq.com");
			stud[0] = stu0;
			Students stu1 = new Students(2,"����","��",21,"����","�ӱ�����","12345612312","1231@qq.com");
			stud[1] = stu1;
			Students stu2 = new Students(3,"����","��",13,"����","�ӱ�����","12345612312","1231@qq.com");
			stud[2] = stu2;
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
	//����ѧ���࣬
	public int id;
	public String name;
	public String sex;
	public int age;
	public String _class;
	public String address;
	public String phone;
	public String email;
	//ѧ���Ĺ��캯��
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