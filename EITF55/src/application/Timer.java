package application;


public final class Timer {
	
	private static long startTime;
	
	public static void start() {
		startTime = System.currentTimeMillis();
	}
	
	public static void stop() {
		System.out.println("Operation took: " + (System.currentTimeMillis()-startTime) + " ms");
	}
	
	public static void stop(int iterations) {
		System.out.println("Mean time for operation was: " + (System.currentTimeMillis()-startTime)/iterations + " ms/op");
	}
}
