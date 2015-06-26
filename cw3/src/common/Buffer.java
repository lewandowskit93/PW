package common;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public final class Buffer<T> {
	private int _max_size;
	private Set<IProducer<T>> _producers;
	private Set<IConsumer<T>> _consumers;
	private Queue<T> _buffer;

	public Buffer(int max_size) {
		_max_size = max_size;
		_producers = new HashSet<IProducer<T>>();
		_consumers = new HashSet<IConsumer<T>>();
		_buffer = new LinkedList<T>();
	}

	public synchronized void registerProducer(IProducer<T> p) {
		_producers.add(p);
		notifyAll();
	}

	public synchronized void unregisterProducer(IProducer<T> p) {
		_producers.remove(p);
		notifyAll();
	}

	public synchronized void registerConsumer(IConsumer<T> c) {
		_consumers.add(c);
		notifyAll();
	}

	public synchronized void unregisterConsumer(IConsumer<T> c) {
		_consumers.remove(c);
		notifyAll();
	}

	public synchronized int getNumberOfProducers() {
		return _producers.size();
	}
	
	public synchronized int getNumberOfConsumers() {
		return _consumers.size();
	}

	public synchronized boolean insertElement(T elem) {
		if (_max_size != 0) {
			while (_buffer.size() == _max_size && getNumberOfConsumers() != 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(_buffer.size()!=_max_size)
			{
				_buffer.add(elem);
				System.out.println("Inserted: "+elem);
				notifyAll();
				return getNumberOfConsumers()!=0;
			}
			else
			{
				notifyAll();
				return false;
			}
		} else {
			_buffer.add(elem);
			System.out.println("Inserted: "+elem);
			notifyAll();
			return true;
		}

	}

	public synchronized T getElement() {
		while (_buffer.size() == 0 && getNumberOfProducers() != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		T elem = _buffer.poll();
		System.out.println("Obtained: "+elem);
		notifyAll();
		return elem;
	}

}
