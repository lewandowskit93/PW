package common;

public abstract class AReproducer<T> implements IReproducer<T>{
	Buffer<T> _input_buffer;
	Buffer<T> _output_buffer;
	
	public AReproducer(Buffer<T> input_buffer, Buffer<T> output_buffer) {
		_input_buffer = input_buffer;
		_output_buffer = output_buffer;
		registerToBuffer();
	}
	
	@Override
	public boolean consumeNext() {
		T elem = _input_buffer.getElement();
		if (elem != null) {
			consume(elem);
			return true;
		} else {
			unregisterFromBuffer();
			return false;
		}
	}
	
	@Override
	public boolean produceIntoBuffer() {
		T elem = produce();
		if (elem != null) {
			if(_output_buffer.insertElement(elem))
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
		_input_buffer.registerConsumer(this);
		_output_buffer.registerProducer(this);
	}
	@Override
	public void unregisterFromBuffer() {
		_input_buffer.unregisterConsumer(this);
		_output_buffer.unregisterProducer(this);	
	}
}
