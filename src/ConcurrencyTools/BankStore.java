package ConcurrencyTools;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Ethan
 * @desc 实现银行存储业务，15个用户，仅有3个柜台
 */
public class BankStore {
	
	public void store(){
		BankAccount account ;
		Semaphore semaphore = new Semaphore(3);
		
		//初始化15个用户
		ExecutorService executors = Executors.newFixedThreadPool(15);
		
		for(int i = 0; i < 15;i++){
			account = new BankAccount();
			executors.execute(new User(account,semaphore));
		}
		executors.shutdown();
	}
	
	public static void main(String[] args) {
		BankStore bs = new BankStore();
		bs.store();
	}
}

/**
 * @author Ethan
 * @desc 存钱的用户
 */
class User implements Runnable{
	
	private BankAccount account;
	private Semaphore semaphore;
	
	public User(BankAccount account,Semaphore semaphore){
		this.account = account;
		this.semaphore = semaphore;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(semaphore.availablePermits() > 0){
			System.out.println("获取许可，开始存钱");
		}else{
			System.out.println("等待空余许可，继续排队");
		}
		try{
			semaphore.acquire();
			account.save(new Random().nextInt(100));
			System.out.println(account.getCount());
			semaphore.release();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}

/**
 * @author Ethan
 * @desc 银行账户
 */
class BankAccount{
	private int count = 100;
	
	public int getCount(){
		return this.count;
	}
	
	public void save(int number){
		count += number;
	}
	
}