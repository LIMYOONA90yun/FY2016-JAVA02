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
	/**������ǵ��㷨
		��i ����i ��Ԫ��
		ÿһ�еĵ�һ��Ԫ�غ����һ��Ԫ�ض�Ϊ1
		����1 ֮�⣬ÿ��Ԫ�ص�ֵ����������һ��ͬλ�õ�Ԫ���Լ�ǰһ��Ԫ�صĺ͡�
		Ҫ�󣺶���һ������n�����������ǵ�ǰn ��
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
	/*//�ݹ��㷨���
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