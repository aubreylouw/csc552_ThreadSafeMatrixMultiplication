package alouw.csc552.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatMathImpl implements MatMath {
	
	final private static Operation multiplication = new Operation() {
		public int apply (final int operandA, final int operandB) {
			return operandA * operandB;
		}
	};

	
	final private static Operation addition = new Operation() {
		public int apply (final int operandA, final int operandB) {
			return operandA + operandB;
		}
	};
	
	public static enum EXECUTION_MODE {
		THREADS {
			@Override
			void applyOperation (MatMathImpl obj, final int[][] A, final int[][] B, int[][] C, Operation operation) {
				obj.applyThreadedOperation(A, B, C, operation);
			}
		}, 
	
		STREAMS {
			@Override
			void applyOperation (MatMathImpl obj, final int[][] A, final int[][] B, int[][] C, Operation operation) {
				obj.applyStreamedOperation(A, B, C, operation);
			}
		},
		
		EXECUTORS {
			@Override
			void applyOperation(MatMathImpl obj, int[][] A, int[][] B,
					int[][] C, Operation operation) {
				obj.applyExecutorOperation(A, B, C, operation);
			}
			
		}; 
		
		abstract void applyOperation (MatMathImpl obj, final int[][] A, final int[][] B, int[][] C, Operation operation);
	}
	
	private static EXECUTION_MODE mode = EXECUTION_MODE.STREAMS;
	
	public static void setMode (EXECUTION_MODE mode) {
		MatMathImpl.mode = mode;
	}
	
	/* 
	 * @invariant: every SubArray is of the same length
	 */
	private static boolean verifySubArraysOfEqualSize (final int[][] array) {
		final int rows = array.length;
		final int cols = array[0].length;
		
		for (int idx = 1; idx < rows; idx++) {
			if (array[idx].length != cols) return false;
		}
		
		return true;
	}

	/*
	 * @precondition: all SubArrays of A are of equal length
	 * @precondition: all SubArrays of B are of equal length
	 * @invariant: length of A[0] == length of B
	 */
	private static boolean verifyProductOperandsAreValid (final int[][] factorA, final int[][] factorB) {
		return (factorA[0].length == factorB.length) ? true : false;
	}
	
	/*
	 * @precondition: all SubArrays of A are of equal length
	 * @precondition: all SubArrays of B are of equal length
	 * @precondition: all SubArrays of C are of equal length
	 * @precondition: length of A[0] == length of B
	 * @invariant: length of A == length of product
	 * @invariant: length of B[0] == length of product[0]
	 */
	private static boolean verifyProductArrayIsValid (final int[][] factorA, final int[][] factorB, final int[][] product) {
		return (factorA.length == product.length &&
				factorB[0].length == product[0].length) ? true : false;
	}
	
	/*
	 * @precondition: all SubArrays of A are of equal length
	 * @precondition: all SubArrays of B are of equal length
	 * @invariant: length of A[0] == length of B[0]
	 * @invariant: length of A == length of B
	 */
	private static boolean verifySumOperandsAreValid (final int[][] factorA, final int[][] factorB) {
		return (factorA[0].length == factorB[0].length &&
				factorA.length == factorB.length) ? true : false;
	}
	
	/*
	 * @precondition: all SubArrays of A are of equal length
	 * @precondition: all SubArrays of B are of equal length
	 * @precondition: all SubArrays of C are of equal length
	 * @precondition: length of A[0] == length of B[0]
	 * @precondition: length of A == length of B
	 * @invariant: length of sum == length of A == length of B
	 * @invariant: length of sum[0] == length of A[0] == length of B[0]
	 */
	private static boolean verifySumArrayIsValid (final int[][] factorA, final int[][] factorB, final int[][] sum) {
		return (factorA[0].length == sum[0].length &&
				factorB.length == sum.length) ? true : false;
	}
		
	/*
	 * @precondition: all SubArrays of input are of equal length
	 */
	private static int [][] deepCopyOfArray (final int [][] input) {
		final int [][] result = new int [input.length][input[0].length];
		
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				result[i][j] = input[i][j];
			}
		}
		
		return result;
	}
	
	/*
	 * Multiplies two matrices together and stores the result in C.
	 * (1) Throws IllegalArgumentException for any invalid arguments
	 * (2)
	 * 
	 * @see alouw.csc552.matrix.MatMath#multiply(int[][], int[][], int[][])
	 */
	@Override
	public void multiply(final int[][] A, final int[][] B, int[][] C) throws IllegalArgumentException {
		// verify the arrays have the correct structure to permit multiplication 
		if (!verifySubArraysOfEqualSize(A)) throw new IllegalArgumentException("Operand A has SubArrays of unequal length.");
		if (!verifySubArraysOfEqualSize(B)) throw new IllegalArgumentException("Operand B has SubArrays of unequal length.");
		if (!verifySubArraysOfEqualSize(C)) throw new IllegalArgumentException("Result C has SubArrays of unequal length.");				
		if (!verifyProductOperandsAreValid(A, B)) 
			throw new IllegalArgumentException("The requested operation on operands A and B is not defined.");
		if (!verifyProductArrayIsValid(A, B, C)) 
			throw new IllegalArgumentException("C does not support the requested operation on operands A and B.");
		
		mode.applyOperation(this, A, B, C, MatMathImpl.multiplication);
	}

	@Override
	public void add(int[][] A, int[][] B, int[][] C) throws IllegalArgumentException {
		// verify the arrays have the correct structure to permit addition 
		if (!verifySubArraysOfEqualSize(A)) throw new IllegalArgumentException("Operand A has SubArrays of unequal length.");
		if (!verifySubArraysOfEqualSize(B)) throw new IllegalArgumentException("Operand B has SubArrays of unequal length.");
		if (!verifySubArraysOfEqualSize(C)) throw new IllegalArgumentException("Result C has SubArrays of unequal length.");
		if (!verifySumOperandsAreValid(A, B)) 
			throw new IllegalArgumentException("The requested operation on operands A and B is not defined.");
		if (!verifySumArrayIsValid(A, B, C)) 
			throw new IllegalArgumentException("C does not support the requested operation on operands A and B.");
		
		mode.applyOperation(this, A, B, C, MatMathImpl.addition);
	}
	
	private void applyThreadedOperation(final int[][] A, final int[][] B, int[][] C, Operation operation) {
		// make defensive copies of the inputs to maintain method invariants
		// and to permit parallel, non-blocking reads of the inputs (i.e. effectively immutable)
		final int [][] operandA;
		final int [][] operandB;
						
		synchronized (A) {
			synchronized (B) {
				operandA = deepCopyOfArray(A);
				operandB = deepCopyOfArray(B);  
			}
		}
				
		// create a thread-safe copy of C to permit parallel writes
		final ThreadSafeMatrix targetC = new ThreadSafeMatrix(C.length, C[0].length);
		
		// require one execution thread per operation
		// in practice this is TERRIBLE but it does have lots of concurrency and parallelization
		final int numExecutionThreads;
		
		// threads will communicate via latches
		final CountDownLatch startSignal = new CountDownLatch(1);
	    final CountDownLatch doneSignal; 
		
	    if (operation.equals(MatMathImpl.multiplication)) {
	    	
	    	numExecutionThreads = operandA.length * operandA[0].length * operandB[0].length;
	    	doneSignal= new CountDownLatch(numExecutionThreads);
	    	
	    	for (int i = 0; i < operandA.length; i ++) {
				for (int j = 0; j < operandA[0].length; j++) {
					for (int k = 0; k < operandB[0].length; k++) { 
						
						// spin up a thread with the coordinates and let it rip!
						((Thread) (new Thread (RowColOpExecutableFactory.getNewRunnableExecutable(
								operandA, i, j, operandB, j, k, 
								i, k, targetC, operation, startSignal, doneSignal)))).start();
					}
				}
			}
	    } else if (operation.equals(MatMathImpl.addition)){
	    	
	    	numExecutionThreads = operandA.length * operandA[0].length;
	    	doneSignal= new CountDownLatch(numExecutionThreads);
	    	
	    	for (int i = 0; i < operandA.length; i ++) {
				for (int j = 0; j < operandA[0].length; j++) {
					
					// spin up a thread with the coordinates and let it rip!
					((Thread) (new Thread (RowColOpExecutableFactory.getNewRunnableExecutable(
							operandA, i, j, operandB, i, j, 
							i, j, targetC, operation, startSignal, doneSignal)))).start();
				}
			}
		} else throw new IllegalArgumentException("Unsupported operation");
	   
		
		//Debug.out.breakPoint("Num threads running before countdown: " + Thread.activeCount()); 
		
		// kick off the parallel computation of operation
		startSignal.countDown();      
		
		//Debug.out.breakPoint("Num threads running after countdown: " + Thread.activeCount()); 
		
		// wait for the threads to complete
	    try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}   
	    
	    //Debug.out.breakPoint("Num threads running after donesignal: " + Thread.activeCount());
	    
		// replace C with a copy of the thread-safe matrix
		synchronized (C) {
			final int[][] result = targetC.returnCopy();
			for (int i = 0; i < C.length; i++) {
				for (int j = 0; j < C[0].length; j++) {
					C[i][j] = result[i][j];
				}
			}
		}
	}
	
	private void applyExecutorOperation(final int[][] A, final int[][] B, int[][] C, Operation operation) {
		// make defensive copies of the inputs to maintain method invariants
		// and to permit parallel, non-blocking reads of the inputs (i.e. effectively immutable)
		final int [][] operandA;
		final int [][] operandB;
						
		synchronized (A) {
			synchronized (B) {
				operandA = deepCopyOfArray(A);
				operandB = deepCopyOfArray(B);  
			}
		}
				
		// create a thread-safe copy of C to permit parallel writes
		final ThreadSafeMatrix targetC = new ThreadSafeMatrix(C.length, C[0].length);
		
		// require one execution thread per operation
		// in practice this is TERRIBLE but it does have lots of concurrency and parallelization
		final int numExecutionThreads;
		
		// executor service to which to submit runnables
		final List<Runnable> tasks = new ArrayList<>();   
		
	    if (operation.equals(MatMathImpl.multiplication)) {
	    	
	    	numExecutionThreads = operandA.length * operandA[0].length * operandB[0].length;

	    	for (int i = 0; i < operandA.length; i ++) {
				for (int j = 0; j < operandA[0].length; j++) {
					for (int k = 0; k < operandB[0].length; k++) { 
						
						tasks.add(RowColOpExecutableFactory.getNewStreamableExecutable(
								operandA, i, j, operandB, j, k, 
								i, k, targetC, operation));
					}
				}
			}
	    } else if (operation.equals(MatMathImpl.addition)){
	    	
	    	numExecutionThreads = operandA.length * operandA[0].length;
	    	
	    	for (int i = 0; i < operandA.length; i ++) {
				for (int j = 0; j < operandA[0].length; j++) {
					
					tasks.add(RowColOpExecutableFactory.getNewStreamableExecutable(
							operandA, i, j, operandB, i, j, 
							i, j, targetC, operation));
				}
			}
		} else throw new IllegalArgumentException("Unsupported operation");
	   
	    ExecutorService pool = Executors.newFixedThreadPool(numExecutionThreads);
	    List<Future<?>> futures = new ArrayList<>(numExecutionThreads);
	    //ConcurrentLinkedQueue<Future<?>> futures =  new ConcurrentLinkedQueue<>();
	    tasks.stream().forEach(t -> futures.add(pool.submit(t)));
	    
	    // iterate over the futures, and call get() to ensure that every thread is complete
	    for (Future<?> f: futures) {
	    	try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
	    }
	    
	    pool.shutdown();
	    
		// replace C with a copy of the thread-safe matrix
		synchronized (C) {
			final int[][] result = targetC.returnCopy();
			for (int i = 0; i < C.length; i++) {
				for (int j = 0; j < C[0].length; j++) {
					C[i][j] = result[i][j];
				}
			}
		}
	}
	
	private void applyStreamedOperation(final int[][] A, final int[][] B, int[][] C, Operation operation) {
		// make defensive copies of the inputs to maintain method invariants
		// and to permit parallel, non-blocking reads of the inputs (i.e. effectively immutable)
		final int [][] operandA;
		final int [][] operandB;
						
		synchronized (A) {
			synchronized (B) {
				operandA = deepCopyOfArray(A);
				operandB = deepCopyOfArray(B);  
			}
		}
				
		// create a thread-safe copy of C to permit parallel writes
		final ThreadSafeMatrix targetC = new ThreadSafeMatrix(C.length, C[0].length);
		
		// list backing the streaming operations
		final List<RowColOpExecutable> tasks = new ArrayList<>();
		
	    if (operation.equals(MatMathImpl.multiplication)) {
	    	
	    	for (int i = 0; i < operandA.length; i ++) {
				for (int j = 0; j < operandA[0].length; j++) {
					for (int k = 0; k < operandB[0].length; k++) { 
						
						tasks.add(
								RowColOpExecutableFactory.getNewStreamableExecutable(
										operandA, i, j, operandB, j, k, 
										i, k, targetC, operation)
									);
					}
				}
			}
	    } else if (operation.equals(MatMathImpl.addition)){
	    	
	    	for (int i = 0; i < operandA.length; i ++) {
				for (int j = 0; j < operandA[0].length; j++) {
					
					tasks.add(
							RowColOpExecutableFactory.getNewStreamableExecutable(
									operandA, i, j, operandB, i, j, 
									i, j, targetC, operation)
								);
				}
			}
		} else throw new IllegalArgumentException("Unsupported operation");

	    tasks.parallelStream().forEach(t -> t.run());
	   	    
		// replace C with a copy of the thread-safe matrix
		synchronized (C) {
			final int[][] result = targetC.returnCopy();
			for (int i = 0; i < C.length; i++) {
				for (int j = 0; j < C[0].length; j++) {
					C[i][j] = result[i][j];
				}
			}
		}
	}

	@Override
	public void print(int[][] A) {
		
		synchronized (A) {
			final int rows = A.length;
			final int cols = A[0].length;
			
			System.out.println("------------------" + this.getClass().getSimpleName());
			for (int i = 0; i < rows; i ++) {
				for (int j = 0; j < cols; j++) {
					System.out.print(A[i][j]);
					System.out.print("   ");
				}
				System.out.println();
			}
			System.out.println("------------------"+ this.getClass().getSimpleName());	
		}
	}
}