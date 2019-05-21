package it.aiv;

public class App {

	public static void main(String[] args) {
		int rows = 40;
		int cols = 40;
		int steps = 3;
		int seed = 1;
		int n=2;
		
		if(args.length == 2) {
			rows = Integer.parseInt(args[0]);
			cols = Integer.parseInt(args[1]);
		}
		
		if(args.length == 3)
			steps = Integer.parseInt(args[2]);
		
		if(args.length == 4) 
			seed = Integer.parseInt(args[3]);
		
		if(args.length == 5) 
			seed = Integer.parseInt(args[4]);
		
		Map2DGeneration map = new Map2DGeneration(rows, cols);
		
		map.build(steps, seed);
		System.out.println("\nMAP GENERATED\n");
		print(map.getInitialMap());
		
		map.performFlooding();
		System.out.println("\nBIGGEST AREA\n");
		print(map.getBiggestArea());
		
		map.crop(n);
		System.out.println("\nBIGGEST AREA CROPPED\n");
		print(map.getBiggestAreaCropped());
	}

	private static void print(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				int value = matrix[i][j];

				if (value == 1) System.out.print(value + " ");
				else System.out.print("  ");
			}
			System.out.println();
		}
	}
}
