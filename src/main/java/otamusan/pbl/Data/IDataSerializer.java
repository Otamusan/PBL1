package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * 送受信したい値を{@link ByteBuffer}と相互に変換するクラス
 * @author otamusan
 *
 * @param <T> 送受信したい値の型
 */
public interface IDataSerializer<T> {
	/**
	 * 与えられた値を与えられた{@link ByteBuffer}に書き込む。
	 * {@link ByteBuffer}の位置を{@link IDataSerializer#getCapacity()}の分だけ移動させる
	 * @param t 書き込む値
	 * @param buffer 書き込まれるバイト列
	 */
	public void encode(T t, ByteBuffer buffer);

	/**
	 * {@link ByteBuffer}から値を読み込む
	 * {@link ByteBuffer}の位置を{@link IDataSerializer#getCapacity()}の分だけ移動させる
	 * @param buffer 読み込むバイト列
	 * @return 読み込んだ値
	 */
	public T decode(ByteBuffer buffer);

	//public String name();
	/**
	 * @return 書き込むのに必要なByte数
	 */
	public int getCapacity();

	/**
	 * {@link Object}の値を変換を行うデータ型にキャストするメソッド
	 * @param キャストする値
	 * @return キャストされた値。キャストに失敗した場合は{@link Optional#empty()}を返す
	 */
	public Optional<T> cast(Object o);

	/**
	 * キャスト可能かどうかを返すメソッド
	 * @param o
	 * @return
	 */
	public boolean isCastable(Object o);
}
