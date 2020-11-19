package otamusan.pbl;

public class Wrap<T> {
	public T t;

	public Wrap(T t) {
		this.setT(t);
	}

	public void setT(T t) {
		this.t = t;
	}

	public T getT() {
		return this.t;
	}
}
