import java.io.*;

public class GridSort
{
	public static void main(String[] args)
	{
		int row = Integer.parseInt(args[0]);
		int col = Integer.parseInt(args[1]);
		int k = Integer.parseInt(args[2]);
		
		System.out.println("");
		
		
		int[] values = {20, 3, 5, 7, 31, 22, 3, 4, 10, 8, 4, 19, 1, 7, 18, 9, 2, 6, 23, 11};
		
		int[][] grid = createGrid(values, row, col);
		printGrid(grid, row, col);
		
		System.out.println("\n== SEARCHING GRID FOR K: " + k + " ==\n");
							
		boolean found = search_key(grid, row, col, k);
		
		if(found)
		{
			System.out.println("SUCCESFULLY FOUND K: " + k);
		}
		else
		{
			System.out.println("K: " + k + " HAS NOT BEEN FOUND");
		}
		
		System.out.println("\n== ENDING SEARCH FOR K: " + k + " ==\n");
	}
	
	public static void printGrid(int grid[][], int row, int col)
	{
		for(int ii = 0; ii < row; ii++)
		{
			for(int jj = 0; jj < col; jj++)
			{
				System.out.print(grid[ii][jj] + " ");
			}
			System.out.println("");
		}
	}
	
	private static void printList(int[] list)
	{
		for(int ii = 0; ii < 20; ii++)
		{
			System.out.print(list[ii] + " ");
		}
		System.out.println("\n");
	}
	
	public static int[][] createGrid(int[] values, int row, int col)
	{
		int r = 0, c = 0, lastRow = 0, lastCol = 0, count = 0;
		int grid[][] = new int[row][col];
		
		printList(values);
		System.out.println("");
		mergeSort(values);
		printList(values);
		
		while(r <= row && c < col)
		{
			if(lastRow <= row)
			{
				lastRow = r+1;
			}
			if(lastCol != col)
			{
				lastCol = c;
			}
			
			while(r >= 0 && c < col)
			{
				grid[r][c] = values[count];
				count++;
				r--;
				c++;
			}
			r = lastRow;
			if(r == row)
			{
				r--;
				c = lastCol;
				c++;
			}
			else
			{
				c = 0;
			}
		}
		return grid;
	}
	
    public static void mergeSort(int[] A)
    {
		mergeSortRecurse(A, 0, A.length-1);
	}
	
    private static void mergeSortRecurse(int[] A, int leftIdx, int rightIdx)
    {
		int midIdx;
		if( leftIdx < rightIdx)
		{
			midIdx = (leftIdx + rightIdx) / 2;
			mergeSortRecurse(A, leftIdx, midIdx);
			mergeSortRecurse(A, midIdx + 1, rightIdx);
			merge(A, leftIdx, midIdx, rightIdx);
		}
    }
	
    private static void merge(int[] A, int leftIdx, int midIdx, int rightIdx)
    {
		int[] tempArr;
		int ii, jj, kk;
		tempArr = new int[rightIdx - leftIdx + 1];
		ii = leftIdx;
		jj = midIdx + 1;
		kk = 0;
		
		while( (ii <= midIdx) && (jj <= rightIdx))
		{
			if( A[ii] <= A[jj])
			{
				tempArr[kk] = A[ii];
				ii++;
			}
			else
			{
				tempArr[kk] = A[jj];
				jj++;
			}
			kk++;
		}
		
		for( ii = ii; ii < midIdx + 1; ii++)
		{
			tempArr[kk] = A[ii];
			kk++;
		}
		for( jj = jj; jj < rightIdx + 1; jj++)
		{
			tempArr[kk] = A[jj];
			kk++;
		}
		for( kk = leftIdx; kk < rightIdx + 1; kk++)
		{
			A[kk] = tempArr[kk - leftIdx];
		}
    }
	
	public static boolean search_key(int grid[][], int row, int col, int k)
	{
		int r = 0, c = col - 1;
		while(r < row && c >= 0)
		{
			if(grid[r][c] == k)
			{
				System.out.println("CURRENT VALUE AT ROW: " + r + ", COL: " + c + " is: " + grid[r][c]);
				System.out.println("FOUND K: " + grid[r][c] + "\n");
				return true;
			}
			else if (grid[r][c] > k)
			{
				System.out.println("CURRENT VALUE AT ROW: " + r + ", COL: " + c + " is: " + grid[r][c]);
				System.out.println(grid[r][c] + " IS GREATER THAN K: " + k + " | MOVING LEFT\n");
				c--;
			}
			else
			{
				System.out.println("CURRENT VALUE AT ROW: " + r + ", COL: " + c + " is: " + grid[r][c]);
				System.out.println(grid[r][c] + " IS LESS THAN K: " + k + " | MOVING DOWN\n");
				r++;
			}
		}
		return false;
	}
}