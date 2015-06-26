package z1;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import common.Buffer;
import common.PasswordConsumer;
import common.PasswordProducerStreamed;


public class Variant3 {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Buffer size:");
		int buf_size = scanner.nextInt();
		System.out.println("Number of producers:");
		int nr_of_producers = scanner.nextInt();
		scanner.nextLine();
		
		Buffer<String> buffer = new Buffer<String>(buf_size);
		
		LinkedList<PasswordProducerStreamed> producers = new LinkedList<PasswordProducerStreamed>();
		LinkedList<Thread> producers_threads = new LinkedList<Thread>();
		for(int i=0;i<nr_of_producers;++i)
		{
			System.out.println("Producer name:");
			String name = scanner.nextLine();
			System.out.println("File with passwords:");
			String filename = scanner.nextLine();
			PasswordProducerStreamed producer = new PasswordProducerStreamed(name, buffer,new FileInputStream(filename));
			producers.add(producer);
			Thread producer_t = new Thread(producer);
			producers_threads.add(producer_t);
		}
		
		System.out.println("Correct password:");
		String password = scanner.nextLine();
		PasswordConsumer consumer = new PasswordConsumer("Consumer", buffer, password);
		Thread consumer_t = new Thread(consumer);
		
		for(int i=0;i<nr_of_producers;++i)
		{
			producers_threads.get(i).start();
		}
		consumer_t.start();
		
		for(int i=0;i<nr_of_producers;++i)
		{
			producers_threads.get(i).join();
		}
		consumer_t.join();
		
		System.out.println(consumer.getName()+": Access granted: "+consumer.isAccessGranted());
		
		scanner.close();
	}

}