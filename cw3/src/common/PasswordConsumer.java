package common;
public class PasswordConsumer extends AConsumer<String> implements Runnable {
	private String _correct_password;
	private boolean _access_granted;
	private String _name;

	public PasswordConsumer(String name, Buffer<String> buffer, String correct_password) {
		super(buffer);
		_name = name;
		_correct_password = correct_password;
		_access_granted = false;
	}

	@Override
	public void run() {
		while (consumeNext() && !_access_granted)
			;
	}

	@Override
	public void consume(String element) {
		System.out.println(_name+": Consuming: "+element);
		if (_correct_password.compareTo(element) == 0) {
			unregisterFromBuffer();
			_access_granted = true;
			System.out.println(_name+": Correct password.");
		}
	}

	public boolean isAccessGranted() {
		return _access_granted;
	}

	public String getName() {
		return _name;
	}
}
