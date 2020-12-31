package otamusan.pbl.sample;

import otamusan.pbl.Connections.ContainerKey;
import otamusan.pbl.Connections.RegisterKey;
import otamusan.pbl.data.Types;


public class ContainerKeys {
	public static otamusan.pbl.Connections.ContainerKey<Character> cha;
	public static ContainerKey<String> message;
	//Client側の登録とServer側の登録が食い違うと厄介なことになるので、どちらからでも呼び出したり取得したContainerKeyを参照できるように、
	// 登録にはStaticメソッドとStaticフィールドを使用することを推奨する。
	public static void init(RegisterKey key) {
		message = key.register(Types.getTypeString(64));//Containerの登録にはやり取りするデータの型を扱うクラスが必要になる。
		// 一般的な型はTypesに実装してあるが、Tuple型のような型引数を持つような型は型引数に合わせて個別に生成する必要がある。
		// また、データを送信したり、受信したデータを取得する際に返り値のContainerKeyが必要になる。
	}
}
