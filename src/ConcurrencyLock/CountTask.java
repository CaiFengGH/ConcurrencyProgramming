package ConcurrencyLock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author Ethan
 * @desc 基于fork/join框架并行的计算任务，计算1+2+3+4
 */
public class CountTask extends RecursiveTask<Integer> {

	//进行任务分割的阈值
	private static final int THRESHOLD = 2;
	
	private int begin = 0;
	private int end = 0;
	
	public CountTask(int begin,int end){
		this.begin = begin;
		this.end = end;
	}
	
	@Override
	protected Integer compute() {
		//初始化返回变量
		int sum = 0;
		
		//判断任务是否足够小
		boolean isSmall = (end - begin) <= THRESHOLD;
		
		if(isSmall){
			//直接开始任务的执行
			for(int i = begin; i <= end; i++){
				sum += i;
			}
		}else{
			//将大任务进行拆分
			int middle = (begin + end) / 2;
			CountTask leftTask = new CountTask(begin,middle);
			CountTask rightTask = new CountTask(middle+1,end);
			//任务的执行,fork直接调用compute
			leftTask.fork();
			rightTask.fork();
			//任务的合并
			int leftRes = leftTask.join();
			int rightRes = rightTask.join();
			sum = leftRes + rightRes;
		}
		return sum;
	}
	
	public static void main(String[] args) {
		//任务的提交和执行
		ForkJoinPool pool = new ForkJoinPool();
		
		CountTask task = new CountTask(1,4);
		Future<Integer> future = pool.submit(task);
		
		try{
			System.out.println(future.get());
		}catch(InterruptedException e){
		}catch(ExecutionException e){
		}
	}
}
