package common;
import java.io.InputStream;
import java.util.Scanner;


public class PasswordProducerStreamed extends PasswordProducer {
	private Scanner _scanner;
	private String _name;
	
	public PasswordProducerStreamed(String name, Buffer<String> buffer, InputStream stream) {
		super(buffer);
		_name = name;
		_scanner = new Scanner(stream);
	}

	@Override
	public String produce() {
		if(_scanner.hasNextLine())
		{
			String s = _scanner.nextLine();
			System.out.println(_name+": Produced: "+s);
			return s;
		}
		else
		{
			unregisterFromBuffer();
			return null;
		}
	}

	public String getName() {
		return _name;
	}

}
