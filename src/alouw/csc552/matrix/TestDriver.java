package alouw.csc552.matrix;

public class TestDriver {
	
	private static final MatMathImpl u = new MatMathImpl();

	public static void main(String[] args) {
		
		MatMathImpl.setMode(MatMathImpl.EXECUTION_MODE.THREADS);
		MatMathImpl.setMode(MatMathImpl.EXECUTION_MODE.STREAMS);
		MatMathImpl.setMode(MatMathImpl.EXECUTION_MODE.EXECUTORS);
		
		final long startTime = System.currentTimeMillis();
		
		int[][] A = new int[2][3];
		int[][] B = new int[3][2];
		int[][] C = new int[2][2];
		
		A[0][0] = 1; A[0][1] = 2; A[0][2] = 3;
		A[1][0] = 4; A[1][1] = 5; A[1][2] = 6;
		
		B[0][0] = 7;  B[0][1] = 8; 
		B[1][0] = 9;  B[1][1] = 10; 
		B[2][0] = 11; B[2][1] = 12;

		final int[][] D = new int[4][3];
		final int[][] E = new int[3][5];
		final int[][] F = new int[4][5];
		
		D[0][0] =  4; D[0][1] =  5;	D[0][2] = -5;
		D[1][0] = -1; D[1][1] = -4;	D[1][2] = -2;
		D[2][0] = -3; D[2][1] =  1;	D[2][2] =  5;
		D[3][0] =  2; D[3][1] =  1;	D[3][2] =  4;
		
		E[0][0] = -4; E[0][1] =  4;	E[0][2] = -1; E[0][3] =  3;	E[0][4] = -4;
		E[1][0] = -1; E[1][1] = -5;	E[1][2] = -5; E[1][3] =  4;	E[1][4] = -5;		
		E[2][0] = -1; E[2][1] = -1; E[2][2] =  0; E[2][3] = -1; E[2][4] =  1;
		
		//serialMultiply(A, B, C, 2, 3, 2);
		//print(C, 2, 2);
		
		//serialMultiply(D, E, F, 4, 3, 5);
		//print(F, 4, 5);
		
		u.multiply(A, B, C);
		u.print(C);
		
		u.multiply(D, E, F);
		u.print(F);
		
		final int[][] G = new int[2][2];
		final int[][] H = new int[2][2];
		final int[][] I = new int[2][2];
		
		G[0][0] = 3; G[0][1] = 8; G[1][0] = 4; G[1][1] = 6;
		H[0][0] = 4; H[0][1] = 0; H[1][0] = 1; H[1][1] = -9;
		
		u.add(G, H, I);
		u.print(I);
		
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
	
	public static void print(int[][] matrix, int rows, int cols) {
		System.out.println("------------------");
		for (int i = 0; i < rows; i ++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(matrix[i][j]);
				System.out.print("   ");
			}
			System.out.println();
		}
		System.out.println("------------------");
	}
}