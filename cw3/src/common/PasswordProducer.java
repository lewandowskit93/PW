package common;

public abstract class PasswordProducer extends AProducer<String> implements Runnable {

	public PasswordProducer(Buffer<String> buffer)
	{
		super(buffer);
	}
	
	@Override
	public void run() {
		while(produceIntoBuffer())
		{
			
		}
	}

}
