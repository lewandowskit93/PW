package common;

public interface IProducer<T> {
	public T produce();
	public boolean produceIntoBuffer();
	public void registerToBuffer();
	public void unregisterFromBuffer();
	
}
