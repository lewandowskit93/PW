package common;

public abstract class AConsumer<T> implements IConsumer<T>{
	private Buffer<T> _buffer;

	public AConsumer(Buffer<T> buffer) {
		_buffer = buffer;
		registerToBuffer();
	}

	@Override
	public boolean consumeNext() {
		T elem = _buffer.getElement();
		if (elem != null) {
			consume(elem);
			return true;
		} else {
			unregisterFromBuffer();
			return false;
		}
	}

	@Override
	public void registerToBuffer() {
		_buffer.registerConsumer(this);
	}

	@Override
	public void unregisterFromBuffer() {
		_buffer.unregisterConsumer(this);
	}
}
