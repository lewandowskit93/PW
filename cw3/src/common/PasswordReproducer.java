package common;

public class PasswordReproducer extends AReproducer<String> implements Runnable {
	private String _password;
	private String _name;
	
	public PasswordReproducer(String name, Buffer<String> input_buffer,
			Buffer<String> output_buffer) {
		super(input_buffer, output_buffer);
		_name=name;
		_password=null;
	}

	@Override
	public String produce() {
		if(_password!=null)
		{
			System.out.println(_name+": Reproducing: "+_password);
			String password = _password;
			password = password.toUpperCase();
			_password=null;
			return password;
		}
		else
		{
			unregisterFromBuffer();
			return null;
		}
	}

	@Override
	public void consume(String element) {
		System.out.println(_name+": Consuming: "+element);
		_password = element;
	}

	@Override
	public void run() {
		while (consumeNext() && produceIntoBuffer())
			;

	}

	public String getName() {
		return _name;
	}

}
