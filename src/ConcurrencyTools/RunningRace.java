package ConcurrencyTools;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ethan
 * @desc 短跑竞赛预备
 */
public class RunningRace {
	//有6人参加比赛
	private final static int numberOfAthelete = 6;

	public static void main(String[] args) {
		
		CyclicBarrier cb = new CyclicBarrier(numberOfAthelete,new Runnable(){
			public void run(){
				System.out.println("开跑...");
			}
		});
		
		ExecutorService executors = Executors.newFixedThreadPool(numberOfAthelete);
		for(int i = 0; i < numberOfAthelete; i++){
			//任务提交
			executors.submit(new Thread(new Athelete("选手"+i,cb)));
		}
		executors.shutdown();
	}
}

class Athelete implements Runnable{
	
	private String atheleteName = null;
	
	private CyclicBarrier cb;
	
	public Athelete(String atheleteName,CyclicBarrier cb){
		this.atheleteName = atheleteName;
		this.cb = cb;
	}
	
	@Override
	public void run() {
		try{
			Thread.sleep(1000 * (new Random()).nextInt(4));
			System.out.println(atheleteName+" 准备就绪");
			cb.await();
			System.out.println(atheleteName+" 开跑");
		}catch(InterruptedException e){
			e.printStackTrace();
		}catch(BrokenBarrierException e){
			e.printStackTrace();
		}
	}
}

