package otamusan.pblconnection;

import java.util.Optional;

/**
 * 非同期で更新された値を格納し、更新されたときに値を同期するクラス
 * @author otamusan
 *
 */
public class Container {
	private Object buffer;
	private Object current;
	private Object previous;
	private boolean isReceivedbuffer = false;
	private boolean isReceived = false;

	public void update() {
		this.previous = this.current;
		this.current = this.buffer;
		this.isReceived = this.isReceivedbuffer;
		this.isReceivedbuffer = false;
	}

	/**
	 * 値を非同期で変更する時に使うメソッド
	 * @param t
	 */
	public void put(Object t) {
		this.buffer = t;
		this.isReceivedbuffer = true;
	}

	/**
	 * 値の変化に関わらず、前回呼び出された後から現在までに値を受け取ったか確認するメソッド
	 * @return
	 */
	public boolean checkReceived() {
		boolean b = this.isReceived;
		this.isReceived = false;
		return b;
	}

	/**
	 * 値を変更する時に使うメソッド
	 * @param t
	 */
	public void set(Object t) {
		this.current = t;
	}

	/**
	 * 1フレーム前の状態と現在の状態が異なっているか調べるメソッド
	 * @return
	 */
	public boolean isChange() {
		return this.current != this.previous;
	}

	/**
	 * 同期された値を取得するメソッド
	 * @param defa 値が存在しなかった場合に返す値
	 * @return
	 */
	public Object get(Object defa) {
		if (this.current == null)
			return defa;
		return this.current;
	}

	/**
	 * 同期された値を取得するメソッド
	 * @return
	 */
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
