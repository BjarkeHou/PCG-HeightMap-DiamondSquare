package dk.itu.DS;

import java.util.Random;

public class DiamondSquare {
	
	/**
	 * This is the method which should be used to construct the heightmap using the Diamond Square
	 * algorithm.
	 * @param iterations How many iterations to run the Diamond Square algorithm, influences the size of the height map.
	 * @param seed The height to seed the four corners of the first square with.
	 * @param variation The variation to used for the randomized heights.
	 * @param roughness The roughness factor to use to decrease variation over time.
	 * @return
	 */
	public int[][] buildMap(int iterations, int seed, int variation, double roughness)
	{
		int sizeOfMap = (int)Math.pow(2, iterations)+1;
		int[][] map = new int[sizeOfMap][sizeOfMap];
		
		// Set the 4 corners
		map[0][0] = seed;
		map[sizeOfMap-1][0] = seed;
		map[0][sizeOfMap-1] = seed;
		map[sizeOfMap-1][sizeOfMap-1] = seed;
		
		int curIterations = 0;
		int randRange = variation;
		int resolution = sizeOfMap-1;
		Random rand = new Random();
		
		
		while(curIterations < iterations) {
//			Pass through the array and perform the diamond step for each square present.
			for (int x = resolution/2; x < map.length; x+=resolution) {
				for (int y = resolution/2; y < map[x].length; y+=resolution) {
					int[] cornerValues = findCorners(x, y, resolution/2, map, true);
					int avgCornerValues = 0;
					for (int i : cornerValues) {
						avgCornerValues += i/cornerValues.length;
					}
					map[x][y] = avgCornerValues+rand.nextInt(randRange)-(randRange/2);
				}
			}
			
//			Pass through the array and perform the square step for each diamond present.
			int c = -1;
			for (int x = 0; x < map.length; x+=resolution/2) {
				for (int y = 0; y < map[x].length; y+=resolution/2) {
					c++;
					if(c%2==0) {
						continue;
					}
						
					
					int[] cornerValues = findCorners(x, y, resolution/2, map, false);
					int avgCornerValues = 0;
					int count = 0;
					for (int i : cornerValues) {
						if(i == -1)
							continue;
						
						avgCornerValues+=i;
						count++;
					}
					
					avgCornerValues = avgCornerValues/count;
					
					map[x][y] = avgCornerValues+rand.nextInt(randRange)-(randRange/2);
				}
			}
			
//			Reduce the random number range using the roughness factor.
			randRange *= roughness;
			if(randRange < 1) randRange = 1;
			
			resolution*=.5;
			
//			current iteration++;
			curIterations++;
			
		}
		
		return map;
	}
	
	private int[] findCorners(int x, int y, int resolution, int[][] map, boolean square) {
		int[] cornerValues = new int[4];
		
		if(square) {
			cornerValues[0] = map[x-resolution][y-resolution];
			cornerValues[1] = map[x-resolution][y+resolution];
			cornerValues[2] = map[x+resolution][y-resolution];
			cornerValues[3] = map[x+resolution][y+resolution];
		} else {
			if(y-resolution >= 0)
				cornerValues[0] = map[x][y-resolution];
			else
				cornerValues[0] = -1;
			
			if(x-resolution >= 0)
				cornerValues[1] = map[x-resolution][y];
			else
				cornerValues[1] = -1;
			
			if(y+resolution < map[0].length)
				cornerValues[2] = map[x][y+resolution];
			else 
				cornerValues[2] = -1;
			
			if(x+resolution < map.length)
				cornerValues[3] = map[x+resolution][y];
			else
				cornerValues[3] = -1;
		}
		
		return cornerValues;
	}
	
}
