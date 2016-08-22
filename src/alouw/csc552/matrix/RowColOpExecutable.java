package alouw.csc552.matrix;

import java.util.concurrent.CountDownLatch;

public interface RowColOpExecutable extends Runnable {
	
	public void run();
}

class RowColOpExecutableRunnableImpl implements RowColOpExecutable {
	
	private final int [][] rowSource; 
	private final int rowSourceRowIndex;
	private final int rowSourceColIndex;
	private final int [][] colSource;
	private final int colSourceRowIndex;
	private final int colSourceColIndex;
	private final int targetRowIndex;
	private final int targetColIndex;
	private final ThreadSafeMatrix targetMatrix;
	private final Operation op;
	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;
	
	RowColOpExecutableRunnableImpl(
			final int [][] rowSource, 
			final int rowSourceRowIndex,
			final int rowSourceColIndex,
			final int [][] colSource, 
			final int colSourceRowIndex,
			final int colSourceColIndex,
			final int targetRowIndex,
			final int targetColIndex,
			final ThreadSafeMatrix targetMatrix,
			final Operation operation,
			final CountDownLatch startSignal,
			final CountDownLatch doneSignal) {
		
		this.rowSource = rowSource;
		this.rowSourceRowIndex = rowSourceRowIndex;
		this.rowSourceColIndex = rowSourceColIndex;
		
		this.colSource = colSource;
		this.colSourceRowIndex = colSourceRowIndex;
		this.colSourceColIndex = colSourceColIndex;
		
		this.targetRowIndex = targetRowIndex;
		this.targetColIndex = targetColIndex;
		this.targetMatrix = targetMatrix;
		
		this.op = operation;
		
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

	/*
	 * Reads one value each from two source arrays
	 * Applies an operation to the two values
	 * Writes the result to the target array 
	 * 
	 * @precondition: source arrays are immutable objects (or effectively immutable)
	 */
	@Override
	public void run() {
		try {
	        startSignal.await();
	        
	        final int operandA = this.rowSource[this.rowSourceRowIndex][this.rowSourceColIndex];
			final int operandB = this.colSource[this.colSourceRowIndex][this.colSourceColIndex];
			final int resultC = this.op.apply(operandA, operandB);
		
			this.targetMatrix.increment(this.targetRowIndex, this.targetColIndex, resultC);
			
	        doneSignal.countDown();
	        
	    } catch (InterruptedException ex) {return;}
	}
}

class RowColOpExecutableStreamableImpl implements RowColOpExecutable {
	
	private final int [][] rowSource; 
	private final int rowSourceRowIndex;
	private final int rowSourceColIndex;
	private final int [][] colSource;
	private final int colSourceRowIndex;
	private final int colSourceColIndex;
	private final int targetRowIndex;
	private final int targetColIndex;
	private final ThreadSafeMatrix targetMatrix;
	private final Operation op;

	RowColOpExecutableStreamableImpl(
			final int [][] rowSource, 
			final int rowSourceRowIndex,
			final int rowSourceColIndex,
			final int [][] colSource, 
			final int colSourceRowIndex,
			final int colSourceColIndex,
			final int targetRowIndex,
			final int targetColIndex,
			final ThreadSafeMatrix targetMatrix,
			final Operation operation) {
		
		this.rowSource = rowSource;
		this.rowSourceRowIndex = rowSourceRowIndex;
		this.rowSourceColIndex = rowSourceColIndex;
		
		this.colSource = colSource;
		this.colSourceRowIndex = colSourceRowIndex;
		this.colSourceColIndex = colSourceColIndex;
		
		this.targetRowIndex = targetRowIndex;
		this.targetColIndex = targetColIndex;
		this.targetMatrix = targetMatrix;
		
		this.op = operation;
	}

	/*
	 * Reads one value each from two source arrays
	 * Applies an operation to the two values
	 * Writes the result to the target array 
	 * 
	 * @precondition: source arrays are immutable objects (or effectively immutable)
	 */
	@Override
	public void run() {
		final int operandA = this.rowSource[this.rowSourceRowIndex][this.rowSourceColIndex];
		final int operandB = this.colSource[this.colSourceRowIndex][this.colSourceColIndex];
		final int resultC = this.op.apply(operandA, operandB);
		
		this.targetMatrix.increment(this.targetRowIndex, this.targetColIndex, resultC);
	}
}