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
		System.out.println("¶¯Îï");
	}
}
class Cat extends Animal{
	public String name="cat";
	public void run(){
		System.out.println("Ã¨");
	}
	
}
class Dog extends Animal{
	
}