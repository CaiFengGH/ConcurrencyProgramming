package ConcurrencyLock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ethan
 * @desc 基于所降级实现缓存的数据更新
 */
public class Cache {
	
	//非并发容器进行数据存储
	private Map<String,String> map = new HashMap<String,String>();
	//可重入的读写锁
	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	//数据处理
	public String getData(String key){
		//获取读锁
		readWriteLock.readLock().lock();
		
		String value = null;
		
		try{
			value = map.get(key);
			
			if(value == null){
				
				//释放读锁，获取写锁
				readWriteLock.readLock().unlock();
				readWriteLock.writeLock().lock();
				
				try{
					value = map.get(key);
					
					//避免再次过程中，写锁更改数据
					if(value == null){
						value = "hello";
						map.put(key, value);
					}

					//根据锁降级的规则，在释放写锁时，优先过去读锁
					readWriteLock.readLock().lock();
				}finally{
					readWriteLock.writeLock().unlock();
				}
			}
		}finally{
			readWriteLock.readLock().unlock();
		}
		
		return value;
	}
}
