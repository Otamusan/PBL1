package otamusan.pbl.data;

import java.util.List;

import otamusan.pbl.util.Tuple;

public class Types {
	public static final IDataSerializer<Integer> TYPE_INT = new TypeInt();
	public static final IDataSerializer<Short> TYPE_SHORT = new TypeShort();
	public static final IDataSerializer<Long> TYPE_LONG = new TypeLong();
	public static final IDataSerializer<Float> TYPE_FLOAT = new TypeFloat();
	public static final IDataSerializer<Double> TYPE_DOUBLE = new TypeDouble();
	public static final IDataSerializer<Character> TYPE_CHAR = new TypeChar();
	public static final IDataSerializer<Boolean> TYPE_BOOL = new TypeBoolean();

	/**
	 * 二つの値の組{@link Tuple}を扱う{@link IDataSerializer}を返すメソッド。
	 * どの値の型とどの値の型のTupleを扱うかによってそれぞれ生成する必要がある
	 * @param <L> 左側の値の型
	 * @param <R> 右側の値の型
	 * @param typel 左側の値の型を扱う{@link IDataSerializer}
	 * @param typer 右側の値の型を扱う{@link IDataSerializer}
	 * @return 与えられた条件の{@link Tuple}を扱う{@link IDataSerializer}
	 */
	public static <L, R> IDataSerializer<Tuple<L, R>> getTypeTuple(IDataSerializer<L> typel, IDataSerializer<R> typer) {
		return new TypeTuple<L, R>(typel, typer);
	}

	/**
	 * ある値の{@link List}を扱う{@link IDataSerializer}を返すメソッド。
	 * どの型のListかとListの長さに対してそれぞれ生成する必要がある
	 * @param <T> 扱いたいListの型引数
	 * @param length Listの長さ
	 * @param type Listに格納する値を扱う{@link IDataSerializer}
	 * @return 与えられた条件の{@link List}を扱う{@link IDataSerializer}
	 */
	public static <T> IDataSerializer<List<T>> getTypeList(int length, IDataSerializer<T> type) {
		return new TypeList<T>(length, type);
	}

	/**
	 * 文字列を扱う{@link IDataSerializer}を返すメソッド。
	 * 文字列の最大の長さをあらかじめ指定しておく必要がある。
	 * @param length 扱う文字列の最大の長さ
	 * @return 与えられた条件の{@link String}を扱う{@link IDataSerializer}
	 */
	public static IDataSerializer<String> getTypeString(int length) {
		return new TypeString(length);
	}

}
