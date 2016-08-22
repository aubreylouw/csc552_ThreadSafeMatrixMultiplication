package alouw.csc552.matrix;

import java.util.Random;
public class TestHugeArrayDriver {

	private static final MatMathImpl u = new MatMathImpl();
	private static final int M = 100;
	private static final int N = 200;
	private static final int O = 500;
	
	private static final int[][] A = new int[M][N];
	private static final int[][] B = new int[N][O];
	private static final int[][] C = new int[M][O];
	
	private static final Random random = new Random();
	
	public static void main(String[] args) {
		
		final long startTime = System.currentTimeMillis();
		
		//MatMathImpl.setMode(MatMathImpl.EXECUTION_MODE.THREADS);
		//MatMathImpl.setMode(MatMathImpl.EXECUTION_MODE.STREAMS);
		MatMathImpl.setMode(MatMathImpl.EXECUTION_MODE.EXECUTORS);
		
		for (int i = 0; i < M; i ++) {
			for (int j = 0; j < N; j++) {
				A[i][j] = random.nextInt();
			}
		}
		
		for (int i = 0; i < N; i ++) {
			for (int j = 0; j < O; j++) {
				B[i][j] = random.nextInt();
			}
		}
		
		//serialMultiply(A, B, C, M, N, O);
		u.multiply(A, B, C);
		
		final long endTime = System.currentTimeMillis();
		
		System.out.println("");
		System.out.println("Duration: " + (endTime - startTime));
	}
	
	public static void serialMultiply(int [][] A, int[][] B, int [][] C, int M, int N, int O) {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				int a = A[i][j];
				for (int k = 0; k < O; k++) {
					int b = B[j][k];
					C[i][k] = C[i][k] + (a * b);
				}
			}
		}
	}
}
