package otamusan.pbl.Util;

/**
 * 任意の値の二つ組
 * @author otamusan
 *
 * @param <L> 左側の値の型
 * @param <R> 右側の値の型
 */
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
