package z1;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import common.Buffer;
import common.PasswordConsumer;
import common.PasswordProducerStreamed;


public class Variant2 {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Buffer size:");
		int buf_size = scanner.nextInt();
		scanner.nextLine();
		Buffer<String> buffer = new Buffer<String>(buf_size);
		
		System.out.println("File with passwords:");
		String filename = scanner.nextLine();
		PasswordProducerStreamed producer = new PasswordProducerStreamed("Producer", buffer,new FileInputStream(filename));
		Thread producer_t = new Thread(producer);
		
		System.out.println("Correct password:");
		String password = scanner.nextLine();
		PasswordConsumer consumer = new PasswordConsumer("Consumer", buffer, password);
		Thread consumer_t = new Thread(consumer);
		
		producer_t.start();
		consumer_t.start();
		
		producer_t.join();
		consumer_t.join();
		
		System.out.println(consumer.getName()+": Access granted: "+consumer.isAccessGranted());
		
		scanner.close();
	}

}