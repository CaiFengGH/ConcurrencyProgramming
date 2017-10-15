package ConcurrencyTools;

import java.util.Map.Entry;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ethan
 * @desc 银行流水服务
 */
public class BankWaterService implements Runnable{

	//并发容器存储待处理银行流水数据
	private ConcurrentHashMap<String,Integer> data = 
							new ConcurrentHashMap<String,Integer>();
	
	//循环屏障执行拆分任务
	private CyclicBarrier cb = new CyclicBarrier(4,this);
	
	//基于线程池进行任务拆分
	private ExecutorService executor = Executors.newFixedThreadPool(4);
	
	
	//流水任务的执行
	public void count(){
		
		for(int i = 0;i < 4;i++){
			
			executor.execute(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					data.put(Thread.currentThread().getName(), 1);
					try{
						System.out.println(Thread.currentThread().getName()+"完成任务");
						cb.await();
					}catch(InterruptedException e){
						e.printStackTrace();
					}catch(BrokenBarrierException e){
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	//子任务执行完后的汇总
	@Override
	public void run() {
		System.out.println("子任务均已完成，汇总工作在继续");
		try{
			int result = 0;
			for(Entry<String, Integer> es:data.entrySet()){
				result += es.getValue();
			}
			//将结果进行记录
			data.put("result", result);
			System.out.println(result);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		BankWaterService bws = new BankWaterService();
		bws.count();
	}
}
