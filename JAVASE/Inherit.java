public class Inherit{

	public static void main(String[] args){
		//Cat cat = new Cat();
		//System.out.println(cat.name);
		//Dog a = new Dog();
		/*System.out.println(a.name);
		a.run();*/
		Cat cat = new Cat();
		cat.abc();
		cat.abc(3,2);
		//Dog dog = new Dog();
		Animal a = new Animal();//���󣬳����಻���Ա�ʵ���������������ڱ��̳�
	}
	
}

interface abc{//����һ�����
	public void abc();
	public void abc(int a,int b);
}




abstract class Animal{//������
	public String name="animal";
	
	public Animal(){
		System.out.println("aaaaaaaaa");
	}
	public Animal(String name){
		System.out.println("aaaaaaaaa111");
	}
	
	abstract public void run();//���󷽷���û������
		//System.out.println("animal");
	
}
class Cat extends Animal implements abc{
	public void abc(){
		System.out.println("enheng");
	}
	public void abc(int a, int b){
		System.out.println(a+b);
	}
	
	public String name="cat";
	public void run(){//�������ʵ�ָ������еĳ��󷽷�����������Ҳ���붨��Ϊ������
		//System.out.println("cat");
		
		
	}
	
}
abstract class Dog extends Animal{
	
	public Dog(){
		super("name");
		System.out.println("bbbbbbbbbbb");
	}
}