package z1;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import common.Buffer;
import common.PasswordConsumer;
import common.PasswordProducerStreamed;


public class Variant5 {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Buffer size:");
		int buf_size = scanner.nextInt();
		System.out.println("Number of producers:");
		int nr_of_producers = scanner.nextInt();
		System.out.println("Number of consumers:");
		int nr_of_consumers = scanner.nextInt();
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
		
		for(int i=0;i<nr_of_producers;++i)
		{
			producers_threads.get(i).start();
		}
		for(int i=0;i<nr_of_consumers;++i)
		{
			consumers_threads.get(i).start();
		}
		
		for(int i=0;i<nr_of_producers;++i)
		{
			producers_threads.get(i).join();
		}		
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