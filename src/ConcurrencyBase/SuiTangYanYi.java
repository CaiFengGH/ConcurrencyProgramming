package ConcurrencyBase;

/**
 * @author Ethan
 * @desc 隋唐演义的历史简化版本
 */
public class SuiTangYanYi extends Thread {
	
	@Override
	public void run() {
		//大幕徐徐拉开
		System.out.println("隋唐演义正式开始，大幕徐徐拉开");
		
		try{
			Thread.sleep(2000);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//历史背景介绍
		System.out.println("苦难的人民在隋朝的暴政下，欲掀起农民起义");
		
		Army sui = new Army();
		Army farmer = new Army();
		
		//隋唐军队战争进行的如火如荼，胜负难辨
		Thread suiThread = new Thread(sui,"隋朝军队"); 
		Thread farmerThread = new Thread(farmer,"农民起义军队"); 
		
		suiThread.start();
		farmerThread.start();
		
		try{
			Thread.sleep(50);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("英雄人物出场，半路杀出哥程咬金------农民军迎来了自己的大英雄");
				
		//半路杀出个程咬金
		Thread chengYaoJin = new Thread(new KeyPerson(),"程咬金");
		chengYaoJin.start();
		
		//历史人物统帅全局
		try{
			chengYaoJin.join();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		sui.keepFighting = false;
		farmer.keepFighting = false;
		
		try{
			suiThread.join();
			farmerThread.join();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		//历史是人民的胜利
		System.out.println("英雄的中国人民在一次次的压迫中自强");
		//大幕收官
		System.out.println("隋唐演义是中国历史上一次农民起义反对暴政的典型代表");
	}
	
	public static void main(String[] args) {
		new SuiTangYanYi().start();
	}
}

/**
 * @author Ethan
 * @desc 军队
 */
class Army implements Runnable{

	volatile boolean keepFighting = true;
	
	@Override
	public void run() {
		//军队相互间厮杀
		while(keepFighting){
			for(int i = 0; i < 5; i++){
				System.out.println(Thread.currentThread().getName()+"第"+i+"次攻击");
				//交出cpu时间片，下次进攻方未知
				Thread.yield();
			}
		}
		System.out.println(Thread.currentThread().getName()+"结束了战斗！");
	}
}

/**
 * @author Ethan
 * @desc 历史关键人物
 */
class KeyPerson implements Runnable{

	@Override
	public void run() {
		for(int i = 0; i < 5; i++){
			System.out.println(Thread.currentThread().getName()+"第"+i+"次歼灭敌军");
		}
		System.out.println(Thread.currentThread().getName()+"带领唐军迎来了胜利的曙光-------");
	}
}