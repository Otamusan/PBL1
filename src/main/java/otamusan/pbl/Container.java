package otamusan.pbl;

import java.util.Optional;

public class Container {
	private Object buffer;
	private Object current;
	private Object previous;

	public void update() {
		this.previous = this.current;
		this.current = this.buffer;
	}

	public void put(Object t) {
		this.buffer = t;
	}

	public void set(Object t) {
		this.current = t;
	}

	public boolean isChange() {
		return this.current != this.previous;
	}

	public Object get(Object defa) {
		if (this.current == null)
			return defa;
		return this.current;
	}

	public Optional<Object> get() {
		if (this.current == null)
			return Optional.empty();
		return Optional.of(this.current);
	}

	@Override
	public String toString() {
		return "[current:" + this.current + ",container:" + this.buffer + "]";
	}
}
