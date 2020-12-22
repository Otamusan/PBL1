package otamusan.pbl;

import java.util.function.Function;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {
		Test test = new Test(5);
		Function<Test, Integer> function = Test::getF;
		System.out.println(function.apply(test));
	}

	public static class Test {
		int f;

		public Test(int f) {
			this.f = f;
		}

		public int getF() {
			return this.f;
		}
	}

}