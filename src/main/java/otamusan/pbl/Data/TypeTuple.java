package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class ByteTuple<L, R> implements IDataType<Tuple<L, R>> {

	private IDataType<L> typel;
	private IDataType<R> typer;

	public ByteTuple(IDataType<L> typel, IDataType<R> typer) {
		this.typel = typel;
		this.typer = typer;
	}

	@Override
	public void encode(Tuple<L, R> t, ByteBuffer buffer) {
		this.typel.encode(t.getLeft(), buffer);
		this.typer.encode(t.getRight(), buffer);
	}

	@Override
	public Tuple<L, R> decode(ByteBuffer buffer) {
		L l = this.typel.decode(buffer);
		R r = this.typer.decode(buffer);
		return new Tuple<L, R>(l, r);
	}

	@Override
	public int getCapacity() {
		return this.typel.getCapacity() + this.typer.getCapacity();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Tuple<L, R>> cast(Object o) {

		if (this.isCastable(o)) {
			return Optional.of((Tuple<L, R>) o);
		}
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		if (o instanceof Tuple) {
			Tuple<?, ?> tuple = (Tuple<?, ?>) o;
			return this.typel.isCastable(tuple.getLeft()) && this.typer.isCastable(tuple.getRight());
		}
		return false;
	}

}
