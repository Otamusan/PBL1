package otamusan.pbl.Util;

public class Tuple<L, R> {
	private L left;
	private R right;

	public Tuple(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return this.left;
	}

	public R getRight() {
		return this.right;
	}

	@Override
	public String toString() {
		return "[" + this.getLeft().toString() + "," + this.getRight().toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Tuple))
			return false;
		Tuple<?, ?> tuple = (Tuple<?, ?>) obj;
		return tuple.getLeft().equals(this.left) && tuple.getRight().equals(this.right);
	}
}
