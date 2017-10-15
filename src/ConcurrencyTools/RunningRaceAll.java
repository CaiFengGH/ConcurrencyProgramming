package ConcurrencyTools;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ethan
 * @desc 短跑竞赛，包括开始和结束
 */
public class RunningRaceAll {
	
	//开始beginSignal
	private static CountDownLatch beginSignal = new CountDownLatch(1); 
	//结束endSignal
	private static CountDownLatch endSignal = new CountDownLatch(10); 
	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("预备...");

		//运动员准备
		ExecutorService executors = Executors.newFixedThreadPool(10);
		
		for(int i = 0; i < 10;i++){
			int index = i + 1;
			executors.execute(new Runnable(){
				
				@Override
				public void run(){
					try{
						System.out.println("NO."+index+"准备就绪");
						beginSignal.await();
						Thread.sleep(1000 * (new Random().nextInt(10)));
						System.out.println("NO."+index+"冲过终点");
						endSignal.countDown();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			});
		}
		executors.shutdown();
		//预备命令开始
		beginSignal.countDown();
		//比赛焦灼的进行
//		System.out.println("比赛正在焦灼的进行");
		//等待
		endSignal.await();
		//最后一位冲过终点
		System.out.println("比赛结束");
	}
}
