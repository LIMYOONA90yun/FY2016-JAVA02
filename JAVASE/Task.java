public class Task {
	
	public static void main(String[] args){
		/*int a= sum(100);
		System.out.println(a);*/
		/*Task print = Task print();
		System.out.println(print(5));*/
		//Task.print(5);
		Task x = new Task();
		
		x.print(5);
		System.out.println(sum(5));
	}
	/**杨辉三角的算法
		第i 行有i 个元素
		每一行的第一个元素和最后一个元素都为1
		除了1 之外，每个元素的值，都等于上一行同位置的元素以及前一个元素的和。
		要求：读入一个整数n，输出杨辉三角的前n 行
	*/
	public void print(int n){
		int[][] a = new int [n][];
		for(int i=0;i<n;i++){
			a[i] = new int[i+1];
			a[i][0] = 1;
			a[i][i]= 1;
			for(int m =1;m<a[i].length-1;m++){
				a[i][m] = a[i-1][m]+a[i-1][m-1];
			}
		}
		for(int i=0;i<a.length;i++){
			System.out.println("");
			for(int m = 0;m<a[i].length;m++){
				System.out.print(" "+a[i][m]);
			}
		}
	}
	
	
	//public static print (int n){
		
	//}
	/*//递归算法求和
	public static int sum(int p){
		if(p==1){
			return 1;
		}
		return p+sum(p-1);
	}*/
	
	
	public static int sum (int p){
		if(p == 1){
			return 1;
		}
		return p+sum(p-1);
	}
}