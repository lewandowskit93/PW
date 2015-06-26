import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Coordinator implements Runnable{
	
	protected int nr_of_permutations;
	protected int nr_of_threads;
	protected int length;
	protected int permutations_per_thread;
	protected Vector<Generator> generators;
	protected Vector<Thread> threads;
	protected GUI gui;
	
	public Coordinator(int nr_of_threads, int length) throws InvocationTargetException, InterruptedException
	{
		this.nr_of_permutations = Generator.factorial(length);
		this.length=length;
		this.nr_of_threads=nr_of_threads;
		this.permutations_per_thread=nr_of_permutations/nr_of_threads;
		this.generators = new Vector<Generator>();
		this.threads = new Vector<Thread>();
		EventQueue.invokeAndWait(new Runnable()
		{
			@Override
			public void run()
			{
				gui=new GUI();
				gui.setVisible(true);
			}
		});
		for(int i=0;i<this.nr_of_threads;++i)
		{
			Generator generator = new Generator(i*permutations_per_thread, (i+1)*permutations_per_thread,length);
			generators.add(generator);
			Thread thread = new Thread(generator);
			threads.addElement(thread);
		}
	}
	
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException
	{
		//for(int i=0;i<24;++i)
		//{
		//	System.out.println(Arrays.toString(Generator.getKthPermutation(i, 4)));
		//}
		Coordinator coordinator = new Coordinator(1,12);
		coordinator.run();
	}
	
	@Override
	public void run()
	{
		Date start_time = Calendar.getInstance().getTime();
		for(int i=0;i<this.nr_of_threads;++i)
		{
			threads.get(i).start();
		}
		int generated=0;
		while(generated!=nr_of_permutations)
		{
			generated=0;
			for(int i=0;i<nr_of_threads;++i)
			{
				generated+=generators.get(i).getNrOfGenerated();
			}
			double finished = ((double)generated)/nr_of_permutations;
			Date current_time = Calendar.getInstance().getTime();
			long time = current_time.getTime() - start_time.getTime();
			long time_left = (long) ((time/finished) - time);
			//System.out.println(start_time.toString());
			//System.out.println(current_time.toString());
			final int gen = generated;
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					gui.updateInfo(finished*100,100-finished*100,time,time_left,gen,nr_of_permutations);				
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
