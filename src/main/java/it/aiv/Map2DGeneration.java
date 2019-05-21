package it.aiv;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Map2DGeneration {
	
	private int rows;
	private int cols;
	private int[][] initialMap;
	private int[][] biggestArea;
	private int[][] biggestAreaCropped;
	private boolean[][] areasAlreadyFlooding;

	public Map2DGeneration(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;			
	}
		
	public void build(int steps, int seed) {		
		initialMap = new int[rows][cols];
		initializeMap(seed);
		
		for (int i = 0; i < steps; i++) {
			generate();			
		}
	}
	
	public void performFlooding() {
		if(initialMap == null)
			throw new RuntimeException("Initial map has not yet been generated");
		
		areasAlreadyFlooding=new boolean[rows][cols];
		biggestArea = new int[rows][cols];
		int[][] temp = new int[rows][cols];
		int biggestDimension = -1;
		
		for (int x = 1; x < initialMap.length-1; x++) {
			for (int y = 1; y < initialMap.length-1; y++) {
				int current = initialMap[x][y];
				if (current == 0 && !areasAlreadyFlooding[x][y]) {	
					initializeMatrix(biggestArea, 1);
					int currShapeDimension = floodWalkableArea(x, y);
					if(currShapeDimension > biggestDimension) {
						biggestDimension=currShapeDimension;
						temp = Arrays.stream(biggestArea).map(r -> r.clone()).toArray(int[][]::new);	
					}	    
				}
			}
		}	
		
		biggestArea = temp;
		areasAlreadyFlooding=null; //for GC
	}
	
	public void crop(int borderAroundArea) {
		if(biggestArea == null)
			throw new RuntimeException("The biggest area has not yet been generated");
		
		int minX = Integer.MAX_VALUE;
		int maxX = -1;
		int minY = Integer.MAX_VALUE;
		int maxY = -1;
		
		for (int i = 0; i < biggestArea.length; i++) {
			for (int j = 0; j < biggestArea[i].length; j++) {
				if(biggestArea[i][j] == 0) {
					minX = Math.min(j, minX);
					maxX = Math.max(j, maxX);
					minY = Math.min(i, minY);
					maxY = Math.max(i, maxY);
				}
			}
		}
					
		Queue<Integer> area = new LinkedList<Integer>();	
		for (int i = minY; i <= maxY; i++) {
			for (int j = minX; j <= maxX; j++) {
				area.add(biggestArea[i][j]);
			}
		}
			
		int shapeHeight = maxX - minX + 1;		
		int shapeWidth = maxY - minY + 1;
		biggestAreaCropped = new int[shapeWidth + (borderAroundArea*2)][shapeHeight + (borderAroundArea*2)];
		
		for (int i = 0; i < biggestAreaCropped.length; i++) {
			for (int j = 0; j < biggestAreaCropped[i].length; j++) {
				if (i < borderAroundArea || j < borderAroundArea || i >= biggestAreaCropped.length - borderAroundArea
						|| j >= biggestAreaCropped[0].length - borderAroundArea) 
				{
					biggestAreaCropped[i][j] = 1;
				}
				else 
					biggestAreaCropped[i][j] = area.remove();
			}
		}
	}
	
	//Return a copy of initialMap, not the reference
	public int[][] getInitialMap() {
		return Arrays.stream(initialMap).map(r -> r.clone()).toArray(int[][]::new);
	}

	//Return a copy of biggestArea, not the reference
	public int[][] getBiggestArea() {
		return Arrays.stream(biggestArea).map(r -> r.clone()).toArray(int[][]::new);
	}

	//Return a copy of biggestAreaCropped, not the reference
	public int[][] getBiggestAreaCropped() {
		return Arrays.stream(biggestAreaCropped).map(r -> r.clone()).toArray(int[][]::new);
	}
	
	private void initializeMap(int seed) {	
		Random rand = new Random(seed);
		for (int i = 0; i < initialMap.length; i++) {
			for (int j = 0; j < initialMap[i].length; j++) {
				if (i == 0 || j == 0 || i == initialMap.length - 1 || j == initialMap[0].length -1) initialMap[i][j] = 1;
				else initialMap[i][j] = rand.nextInt(2);
			}
		}
	}
	
	private void initializeMatrix(int[][] matrix, int value) {
		for (int x = 0; x < matrix.length; x++) {
			for (int y = 0; y < matrix.length; y++) {
				 matrix[x][y] = value;				
			}
		}
	}

	private void generate() {
		int[][] result = Arrays.stream(initialMap).map(r -> r.clone()).toArray(int[][]::new);
		
		for (int i = 1; i < initialMap.length-1; i++) {
			for (int j = 1; j < initialMap.length-1; j++) {
				int wallAround = countWallAround(initialMap, i, j);
				int current = initialMap[i][j];
				if (current == 1) {
					if (wallAround >= 4) result[i][j] = 1;
					else if (wallAround < 2) result[i][j] = 0;
					else result[i][j] = 0;
				} else {
					if (wallAround >= 5) result[i][j] = 1;
					else result[i][j] = 0;
				}
			}
		}
	}
	
	private int countWallAround(int[][] matrix, int row, int col) {
		int count = 0;
		for (int i = row-1; i <= row+1; i++) {
			for (int j = col-1; j <= col+1; j++) {
				if (i == row && j == col) continue;
				count+= matrix[i][j];
			}
		}
		return count;
	}

	private int floodWalkableArea(int x, int y) 
    {   	
        if (initialMap[x][y] == 1 || areasAlreadyFlooding[x][y]) 
            return 0;
        
    	areasAlreadyFlooding[x][y] = true;  
    	biggestArea[x][y] = 0;
            
    	int areaDimension = 1;
        areaDimension+=floodWalkableArea(x+1, y); 
        areaDimension+=floodWalkableArea(x-1, y); 
        areaDimension+=floodWalkableArea(x, y+1); 
        areaDimension+=floodWalkableArea(x, y-1); 
        
        return areaDimension;
    } 
}
