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
		Animal a = new Animal();//错误，抽象类不可以被实例化，抽象类用于被继承
	}
	
}

interface abc{//定义一个借口
	public void abc();
	public void abc(int a,int b);
}




abstract class Animal{//抽象类
	public String name="animal";
	
	public Animal(){
		System.out.println("aaaaaaaaa");
	}
	public Animal(String name){
		System.out.println("aaaaaaaaa111");
	}
	
	abstract public void run();//抽象方法，没有主体
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
	public void run(){//子类必须实现父类所有的抽象方法，否则子类也必须定义为抽象类
		//System.out.println("cat");
		
		
	}
	
}
abstract class Dog extends Animal{
	
	public Dog(){
		super("name");
		System.out.println("bbbbbbbbbbb");
	}
}