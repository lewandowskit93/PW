package common;

public interface IConsumer<T> {
	public boolean consumeNext();
	public void consume(T element);
	public void registerToBuffer();
	public void unregisterFromBuffer();
}
