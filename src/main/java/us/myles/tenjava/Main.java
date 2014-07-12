package us.myles.tenjava;

public class Main {

	public static void main(String[] args) {
		int x = 8;
		int z = 8;
		int height = (int) (((Math.abs(Math.sin(x - 8)) + Math.abs(Math.sin(z - 8))) * 100D) - 90) / 10 + 10;
		
		System.out.println(height);
		// peak in direct center
	}

}
