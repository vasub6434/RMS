package com.bonrix.common.ThradExample;

class MyThread extends Thread

{

	static String message[] =

			{ "Java", "is", "hot,", "aromatic,", "and", "invigorating." };

	public MyThread(String id)

	{

		super(id);

	}

	public void run()

	{
		SynchronizedOutput.displayList(getName(), message);

	}

	void randomWait()

	{

		try {

			sleep((long) (3000 * Math.random()));

		} catch (InterruptedException x) {

			System.out.println("Interrupted!");

		}

	}

}
