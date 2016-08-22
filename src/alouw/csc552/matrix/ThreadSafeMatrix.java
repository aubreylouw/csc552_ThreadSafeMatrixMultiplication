package alouw.csc552.matrix;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeMatrix {
	
	/*
	 * Synchronization policy:
	 * 	(1) the core object (array) is published safely in the constructor 
	 *  (2) reads and writes to the core object are guarded by AtomicIntegers 
	 *  	resident at specific row/index coordinates (lock striping)
	 *  (3) reads of the whole core object are guarded by this
	 *  (4) all state is privat & state references are immutable
	 */
	
	private final AtomicInteger[][] array;
	private final int rows;
	private final int columns;
	
	ThreadSafeMatrix(final int rows, final int cols) {
		this.array = new AtomicInteger[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.array[i][j] = new AtomicInteger(0);
			}
		}
		
		this.rows = rows;
		this.columns = cols;
	}
	
	public int get(int row, int col) {
		if (row < 0 || row > this.rows) throw new IllegalArgumentException ("Bad row index");
		if (col < 0 || col > this.columns) throw new IllegalArgumentException ("Bad column index");
		
		return this.array[row][col].intValue();
	}
	
	public int increment(int row, int col, int addend) {
		if (row < 0 || row > this.rows) throw new IllegalArgumentException ("Bad row index");
		if (col < 0 || col > this.columns) throw new IllegalArgumentException ("Bad column index");
		
		return this.array[row][col].addAndGet(addend);
	}
	
	public synchronized int[][] returnCopy() {
		final int[][] result = new int[this.rows][this.columns]; 
		
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				result[i][j] = this.array[i][j].get();;
			}
		}
		
		return result;
	}
}