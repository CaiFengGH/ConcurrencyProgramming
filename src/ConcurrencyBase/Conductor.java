package ConcurrencyBase;

/**
 * @author Ethan
 * @desc 两种方式开启多线程
 */
public class Conductor extends Thread{
	
	private int ticket = 0;
	
	private boolean keepSale = true;
	
	public void run(){
		
		while(keepSale){
			System.out.println(Thread.currentThread().getName()+"-->"
						+"售票"+(++ticket)+"张");
			
			if(ticket == 100){
				keepSale = false;
			}
		}
		
		System.out.println(Thread.currentThread().getName()+"售票结束");
		
	}

	public static void main(String[] args) {
		//方法一
		Thread conductor1 = new Conductor();
		conductor1.setName("售票员1");
		conductor1.start();
		
		//方法二
		Thread conductor2 = new Thread(new AnotherConductor(),"售票员2");
		conductor2.start();
	}
}

class AnotherConductor implements Runnable{

	private int ticket;
	
	private boolean keepSale = true;
	
	@Override
	public void run() {
		while(keepSale){

			System.out.println(Thread.currentThread().getName()+"-->"
						+"售票:"+(++ticket)+"张");
			
			if(ticket == 100){
				keepSale = false;
			}
		}
		
		System.out.println(Thread.currentThread().getName()+"售票结束");
	}
}