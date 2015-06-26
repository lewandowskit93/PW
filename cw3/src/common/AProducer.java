package common;

public abstract class AProducer<T> implements IProducer<T>{
	private Buffer<T> _buffer;

	public AProducer(Buffer<T> buffer) {
		_buffer = buffer;
		registerToBuffer();
	}

	@Override
	public boolean produceIntoBuffer() {
		T elem = produce();
		if (elem != null) {
			if(_buffer.insertElement(elem))
			{
				return true;
			}
			else
			{
				unregisterFromBuffer();
				return false;
			}
		} else {
			unregisterFromBuffer();
			return false;
		}
	}

	@Override
	public void registerToBuffer() {
		_buffer.registerProducer(this);
	}

	@Override
	public void unregisterFromBuffer() {
		_buffer.unregisterProducer(this);
	}
}
