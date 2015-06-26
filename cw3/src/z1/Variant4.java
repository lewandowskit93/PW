package z1;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import common.Buffer;
import common.PasswordConsumer;
import common.PasswordProducerStreamed;


public class Variant4 {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Buffer size:");
		int buf_size = scanner.nextInt();
		System.out.println("Number of consumers:");
		int nr_of_consumers = scanner.nextInt();
		scanner.nextLine();
		
		Buffer<String> buffer = new Buffer<String>(buf_size);
		
		System.out.println("File with passwords:");
		String filename = scanner.nextLine();
		PasswordProducerStreamed producer = new PasswordProducerStreamed("Producer", buffer,new FileInputStream(filename));
		Thread producer_t = new Thread(producer);
		
		LinkedList<PasswordConsumer> consumers = new LinkedList<PasswordConsumer>();
		LinkedList<Thread> consumers_threads = new LinkedList<Thread>();
		for(int i=0;i<nr_of_consumers;++i)
		{
			System.out.println("Consumer name:");
			String name = scanner.nextLine();
			System.out.println("Correct password:");
			String password = scanner.nextLine();
			PasswordConsumer consumer = new PasswordConsumer(name, buffer, password);
			consumers.add(consumer);
			Thread consumer_t = new Thread(consumer);
			consumers_threads.add(consumer_t);
		}
		
		producer_t.start();
		for(int i=0;i<nr_of_consumers;++i)
		{
			consumers_threads.get(i).start();
		}
		
		producer_t.join();		
		for(int i=0;i<nr_of_consumers;++i)
		{
			consumers_threads.get(i).join();
		}
		
		for(int i=0;i<nr_of_consumers;++i)
		{
			System.out.println(consumers.get(i).getName()+": Access granted: "+consumers.get(i).isAccessGranted());
		}
		
		scanner.close();
	}

}