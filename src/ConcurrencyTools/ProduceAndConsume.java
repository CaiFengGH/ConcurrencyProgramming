package ConcurrencyTools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * @author Ethan
 * @desc 生产者和消费者模型
 */
public class ProduceAndConsume {
	
	static class Producer implements Runnable{
		
		private List<String> buffer = null;
		
		private Exchanger<List<String>> exchanger = null;
		
		public Producer(List<String> buffer,Exchanger<List<String>> exchanger){
			this.buffer = buffer;
			this.exchanger = exchanger;
		}
		
		@Override
		public void run() {
			//生产者工作
			for(int i = 1; i < 5;i++){
				System.out.println("生产者第"+i+"生产");
				
				for(int j = 1; j <= 3; j++){
					System.out.println("生产者生产"+i+"---"+j);
					buffer.add(i+"---"+j);
				}
				
				try{
					exchanger.exchange(buffer);
					
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}

	static class Consumer implements Runnable{
		
		private List<String> buffer = null;
		
		private Exchanger<List<String>> exchanger = null;
		
		public Consumer(List<String> buffer,Exchanger<List<String>> exchanger){
			this.buffer = buffer;
			this.exchanger = exchanger;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(int i = 1; i < 5;i++){
				
				try{
					buffer = exchanger.exchange(buffer);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				
				System.out.println("消费者第"+i+"次消费数据了");
				
				for(int j = 1; j <= 3; j++){
					System.out.println("消费者消费了"+buffer.get(0));
					buffer.remove(0);
				}
				
			}
		}
	}
	
	public static void main(String[] args) {
		//生产者的工具
		List<String> bufferP = new ArrayList<String>();
		//消费者的工具
		List<String> bufferC = new ArrayList<String>();
		//交换
		Exchanger<List<String>> exchanger = new Exchanger<List<String>>();
		
		Thread producer = new Thread(new Producer(bufferP,exchanger));
		Thread consumer = new Thread(new Consumer(bufferC,exchanger));
		
		producer.start();
		consumer.start();
	}
}
