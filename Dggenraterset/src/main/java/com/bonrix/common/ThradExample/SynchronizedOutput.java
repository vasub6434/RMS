package com.bonrix.common.ThradExample;

public class SynchronizedOutput {

	static class  abc{
		private static void sysout() {
			System.out.println("In static class");

		}
	}
	public static synchronized void displayList(String name, String list[])

	{
		abc.sysout();
		for (int i = 0; i < list.length; ++i) {

			MyThread t = (MyThread) Thread.currentThread();

			t.randomWait();

			System.out.println(name + list[i]);

		}

	}
}
