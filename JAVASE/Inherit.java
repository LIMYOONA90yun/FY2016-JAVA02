public class Inherit{

	public static void main(String[] args){
		//Cat cat = new Cat();
		//System.out.println(cat.name);
		Animal a = new Cat();
		System.out.println(a.name);
		a.run();
	}
	
}
class Animal{
	public String name="animal";
	public void run(){
		System.out.println("����");
	}
}
class Cat extends Animal{
	public String name="cat";
	public void run(){
		System.out.println("è");
	}
	
}
class Dog extends Animal{
	
}