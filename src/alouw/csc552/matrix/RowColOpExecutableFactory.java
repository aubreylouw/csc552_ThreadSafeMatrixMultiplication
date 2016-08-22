package alouw.csc552.matrix;

import java.util.concurrent.CountDownLatch;

public interface RowColOpExecutableFactory {

	public static RowColOpExecutable getNewRunnableExecutable(
			final int [][] rowSource, 
			final int rowSourceRowIndex,
			final int rowSourceColIndex,
			final int [][] colSource, 
			final int colSourceRowIndex,
			final int colSourceColIndex,
			final int targetRowIndex,
			final int targetRowColumn,
			final ThreadSafeMatrix targetMatrix,
			final Operation operation,
			final CountDownLatch startSignal,
			final CountDownLatch doneSignal) {
		return RowColOpExecutableFactoryImpl.getNewRunnableExecutable(
				rowSource, 
				rowSourceRowIndex, 
				rowSourceColIndex, 
				colSource, 
				colSourceRowIndex, 
				colSourceColIndex, 
				targetRowIndex, 
				targetRowColumn, 
				targetMatrix, 
				operation,
				startSignal,
				doneSignal);
	}
	
	public static RowColOpExecutable getNewStreamableExecutable(
			final int [][] rowSource, 
			final int rowSourceRowIndex,
			final int rowSourceColIndex,
			final int [][] colSource, 
			final int colSourceRowIndex,
			final int colSourceColIndex,
			final int targetRowIndex,
			final int targetRowColumn,
			final ThreadSafeMatrix targetMatrix,
			final Operation operation) {
		return RowColOpExecutableFactoryImpl.getNewStreamableExecutable(
				rowSource, 
				rowSourceRowIndex, 
				rowSourceColIndex, 
				colSource, 
				colSourceRowIndex, 
				colSourceColIndex, 
				targetRowIndex, 
				targetRowColumn, 
				targetMatrix, 
				operation);
	}
}

class RowColOpExecutableFactoryImpl implements RowColOpExecutableFactory {

	public static RowColOpExecutable getNewRunnableExecutable(final int[][] rowSource,
			final int rowSourceRowIndex, final int rowSourceColIndex, final int[][] colSource,
			final int colSourceRowIndex, final int colSourceColIndex, final int targetRowIndex,
			final int targetRowColumn, final ThreadSafeMatrix targetMatrix, final Operation operation,
			final CountDownLatch startSignal, final CountDownLatch doneSignal) {
		return new RowColOpExecutableRunnableImpl(rowSource,
			rowSourceRowIndex, rowSourceColIndex, colSource,
			colSourceRowIndex, colSourceColIndex, targetRowIndex,
			targetRowColumn, targetMatrix, operation,
			startSignal, doneSignal);
	}
	
	public static RowColOpExecutable getNewStreamableExecutable(final int[][] rowSource,
			final int rowSourceRowIndex, final int rowSourceColIndex, final int[][] colSource,
			final int colSourceRowIndex, final int colSourceColIndex, final int targetRowIndex,
			final int targetRowColumn, final ThreadSafeMatrix targetMatrix, final Operation operation) {
		return new RowColOpExecutableStreamableImpl(rowSource,
			rowSourceRowIndex, rowSourceColIndex, colSource,
			colSourceRowIndex, colSourceColIndex, targetRowIndex,
			targetRowColumn, targetMatrix, operation);
	}	
}