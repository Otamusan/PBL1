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
}
