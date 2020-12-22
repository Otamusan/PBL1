package otamusan.pbl;

import java.util.Optional;

public class Container<T> {
	private T container;
	private T current;
	private T previous;

	public void update() {
		this.previous = this.current;
		this.current = this.container;
	}

	public void put(T t) {
		this.container = t;
	}

	public boolean isChange() {
		return this.current != this.previous;
	}

	public T get(T defa) {
		if (this.current == null)
			return defa;
		return this.current;
	}

	public Optional<T> get() {
		if (this.current == null)
			return Optional.empty();
		return Optional.of(this.current);
	}

	@Override
	public String toString() {
		return "[current:" + this.current + ",container:" + this.container + "]";
	}
}
