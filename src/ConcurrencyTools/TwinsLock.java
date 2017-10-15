package ConcurrencyTools;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Ethan
 * @desc 同一时刻只允许两个线程共同访问
 */
public class TwinsLock implements Lock {

	private final Syn syn = new Syn(2); 
	
	/**
	 * @author Ethan
	 * @desc 队列同步器的子类，需要重写其基本方法，同时其提供模板方法
	 */
	private static final class Syn extends AbstractQueuedSynchronizer{
		
		Syn(int count){
			//异常检测
			if(count <= 0){
				throw new IllegalArgumentException("参数必须大于0");
			}
			setState(count);
		}
		
		public int tryAcquireShared(int acquireCount){
			//自旋
			for(;;){
				int current = getState();
				int newCurrent = current - acquireCount;
				if(newCurrent < 0 || compareAndSetState(current,newCurrent)){
					return newCurrent;
				}
			}
		}
		
		public boolean tryReleaseShared(int releaseCount){
			for(;;){
				int current = getState();
				int newCurrent = current + releaseCount;
				if(compareAndSetState(current,newCurrent)){
					return true;
				}
			}
		}
		
		final ConditionObject newCondition(){
			return new ConditionObject();
		}
	}
	
	@Override
	public void lock() {
		syn.acquireShared(1);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		syn.acquireInterruptibly(1);
	}

	@Override
	public Condition newCondition() {
		return syn.newCondition();
	}

	@Override
	public boolean tryLock() {
		return syn.tryAcquireShared(1) >= 0;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return syn.tryAcquireSharedNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		syn.releaseShared(1);
	}

}
