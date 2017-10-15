package ConcurrencyBase;

/**
 * @author Ethan
 * @desc 演示能量守恒原则，能量从一个地方转移到另一个地方
 */
public class EnergyBalance {
	
	private static final int ENERGY_UNIT_NUM = 100;
	
	private static final double INITIAL_QUANTITY = 1000;
	
	public static void main(String[] args) {
		
		//搭建能量系统
		EnergySystem es = new EnergySystem(ENERGY_UNIT_NUM,INITIAL_QUANTITY);
		
		//每个能量单元均具有传递能量的能力
		for(int i = 0; i < ENERGY_UNIT_NUM; i++){
			EnergyTransferTask ett = new EnergyTransferTask(es,i,INITIAL_QUANTITY);
			Thread thread = new Thread(ett,"thread-"+i);
			thread.start();
		}
	}
}

/**
 * @author Ethan
 * @desc 能量系统
 */
class EnergySystem{
	
	//能量存储的位置
	private final double[] energy;
	
	//多线程的lock对象
	private final Object lockObj = new Object();
	
	public EnergySystem(int energyUnitNum,double initialQuantity){
		energy = new double[energyUnitNum];
		
		for(int i = 0; i < energyUnitNum; i++){
			energy[i] = initialQuantity;
		}
	}
	
	//能量系统的能量传输功能
	public void transfer(int from,int to,double energyQuantity){
		
		synchronized(lockObj){
			while(energy[from] < energyQuantity){
				try {
					lockObj.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			energy[from] = energy[from] - energyQuantity;
			energy[to] = energy[to] + energyQuantity;
			
			System.out.println(from+"-->"+to+":"+energyQuantity
					+"-->"+this.getEnergyQuantity());
			
			//释放掉lockObj对象上面的所有阻塞线程
			lockObj.notifyAll();
		}
	}
	
	//获取总能量
	public double getEnergyQuantity(){
		double sum = 0;
		for(double energyUnitQuantity:energy){
			sum += energyUnitQuantity;
		}
		return sum;
	}
	
	//获取能量存储位置的数量
	public int getEnergyAmount(){
		return energy.length;
	}
}

class EnergyTransferTask implements Runnable{

	//能量系统
	private EnergySystem es ; 
	//能量的来源单元
	private int from;
	//单次能量传输的最大值
	private double maxEnergyQuantity;
	
	public EnergyTransferTask(EnergySystem es,int from,double maxEnergyQuantity){
		this.es = es;
		this.from = from;
		this.maxEnergyQuantity = maxEnergyQuantity;
	}
	
	@Override
	public void run() {
		try {
			//能量系统的传输任务是不断执行的
			while(true){

				//确定能源的传递终点
				int to = (int) (es.getEnergyAmount() * Math.random());
				//确定能源的传递数量
				double energyQuantity = maxEnergyQuantity * Math.random();
				//调用能源系统的传递功能
				es.transfer(from, to, energyQuantity);
				
				Thread.sleep( (int)(10 * Math.random()));
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}