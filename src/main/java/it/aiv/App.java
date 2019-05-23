package it.aiv;

public class App {

	public static void main(String[] args) {
		int rows = args.length >= 2 ? Integer.parseInt(args[0]) : 48;
		int cols = args.length >= 2 ? Integer.parseInt(args[1]) : 48;
		int steps = args.length >=3 ? Integer.parseInt(args[2]) : 3;
		int seed = args.length >= 4 ? Integer.parseInt(args[3]) : 1;
		int n = args.length == 5 ? Integer.parseInt(args[4]) : 2;
				
		System.out.println("\nSTARTING MAP GENERATION:\n");
		System.out.println("ROWS: " + rows);
		System.out.println("COLS: " + cols);
		System.out.println("STEPS: " + steps);
		System.out.println("SEED: " + seed);
		System.out.println("BORDER: " + n);
		
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
		System.out.println();
	}

	private static void print(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				int value = matrix[i][j];

				if (value == 1) System.out.print("x ");
				else System.out.print("  ");
			}
			System.out.println();
		}
	}
}
