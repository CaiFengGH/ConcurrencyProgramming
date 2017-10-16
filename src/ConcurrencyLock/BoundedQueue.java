package ConcurrencyLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ethan
 * @desc 有界队列
 */
public class BoundedQueue<T> {
	
	//存储对象元素的数组
	private Object[] items = null;
	//添加 删除元素索引及元素个数
	private int addIndex = 0;
	private int removeIndex = 0;
	private int count = 0;
	//读写锁获取Condition
	private Lock lock = new ReentrantLock();
	private Condition notFull = lock.newCondition();
	private Condition notEmpty = lock.newCondition();
	
	public BoundedQueue(int size){
		items = new Object[size];
	}
	
	//add方法
	public void add(T item) throws InterruptedException{
		//获取锁对象
		lock.lock();
		try{
			//count检验
			while(count == items.length){
				notFull.await();
			}
			items[addIndex] = item;
			if(++addIndex == items.length){
				addIndex = 0;
			}
			
			count++;
			notEmpty.signal();
			
		}finally{
			lock.unlock();
		}
	}
	
	//remove方法
	public T remove() throws InterruptedException{
		lock.lock();
		try{
			//是否为空
			while(count == 0){
				notEmpty.await();
			}
			
			Object t = items[removeIndex];
			if(++removeIndex == items.length){
				removeIndex = 0;
			}
			
			count--;
			notFull.signal();

			return (T) t;
		}finally{
			lock.unlock();
		}
	}
}
