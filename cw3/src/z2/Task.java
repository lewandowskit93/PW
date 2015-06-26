package z2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import common.Buffer;
import common.PasswordConsumer;
import common.PasswordProducerStreamed;
import common.PasswordReproducer;

public class Task {
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("First buffer size:");
		int buf1_size = scanner.nextInt();
		System.out.println("Second buffer size:");
		int buf2_size = scanner.nextInt();
		System.out.println("Number of producers:");
		int nr_of_producers = scanner.nextInt();
		System.out.println("Number of reproducers:");
		int nr_of_reproducers = scanner.nextInt();
		System.out.println("Number of consumers:");
		int nr_of_consumers = scanner.nextInt();
		scanner.nextLine();
		
		Buffer<String> buffer1 = new Buffer<String>(buf1_size);
		Buffer<String> buffer2 = new Buffer<String>(buf2_size);
		
		LinkedList<PasswordProducerStreamed> producers = new LinkedList<PasswordProducerStreamed>();
		LinkedList<Thread> producers_threads = new LinkedList<Thread>();
		for(int i=0;i<nr_of_producers;++i)
		{
			System.out.println("Producer name:");
			String name = scanner.nextLine();
			System.out.println("File with passwords:");
			String filename = scanner.nextLine();
			PasswordProducerStreamed producer = new PasswordProducerStreamed(name, buffer1,new FileInputStream(filename));
			producers.add(producer);
			Thread producer_t = new Thread(producer);
			producers_threads.add(producer_t);
		}
		
		LinkedList<PasswordReproducer> reproducers = new LinkedList<PasswordReproducer>();
		LinkedList<Thread> reproducers_threads = new LinkedList<Thread>();
		for(int i=0;i<nr_of_reproducers;++i)
		{
			System.out.println("Reproducer name:");
			String name = scanner.nextLine();
			PasswordReproducer reproducer = new PasswordReproducer(name, buffer1,buffer2);
			reproducers.add(reproducer);
			Thread reproducer_t = new Thread(reproducer);
			reproducers_threads.add(reproducer_t);
		}
		
		LinkedList<PasswordConsumer> consumers = new LinkedList<PasswordConsumer>();
		LinkedList<Thread> consumers_threads = new LinkedList<Thread>();
		for(int i=0;i<nr_of_consumers;++i)
		{
			System.out.println("Consumer name:");
			String name = scanner.nextLine();
			System.out.println("Correct password:");
			String password = scanner.nextLine();
			PasswordConsumer consumer = new PasswordConsumer(name, buffer2, password);
			consumers.add(consumer);
			Thread consumer_t = new Thread(consumer);
			consumers_threads.add(consumer_t);
		}
		
		for(int i=0;i<nr_of_producers;++i)
		{
			producers_threads.get(i).start();
		}
		for(int i=0;i<nr_of_reproducers;++i)
		{
			reproducers_threads.get(i).start();
		}
		for(int i=0;i<nr_of_consumers;++i)
		{
			consumers_threads.get(i).start();
		}
		
		for(int i=0;i<nr_of_producers;++i)
		{
			producers_threads.get(i).join();
		}
		for(int i=0;i<nr_of_reproducers;++i)
		{
			reproducers_threads.get(i).join();
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
