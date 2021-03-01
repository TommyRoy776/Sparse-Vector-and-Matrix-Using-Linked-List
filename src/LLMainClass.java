import java.util.Scanner;
import java.io.File;

public class LLMainClass {

	public static SparseM ParseMatrix(String file_name){
	    Scanner sc = null;
	    String tmps;
	    SparseM M = null;
	    try {
	        sc = new Scanner(new File(file_name));
		    while (sc.hasNext()) {
		    	tmps = sc.next();
		    	if(tmps.equals("MATRIX")){
		    		// initialize the matrix
		    		int nr = sc.nextInt();
		    		int nc = sc.nextInt();
		    		M = new LLSparseM(nr,nc);	
		    	}else if(tmps.equals("END")){
		    		// finished, return the matrix
		    		sc.close();
		    		return M;
		    	}else if(tmps.equals("SET")){
		    		// set an element
		    		int ridx = sc.nextInt(); // row index
		    		int cidx = sc.nextInt(); // col index
		    		int val = sc.nextInt();  // value
		    		M.setElement(ridx, cidx, val);
		    	}else if(tmps.equals("CLEAR")){
		    		// clear an element
		    		int ridx = sc.nextInt(); // row index
		    		int cidx = sc.nextInt(); // col index
		    		M.clearElement(ridx, cidx);		    		
		    	}		    	
		    }
		    sc.close();
		    return M;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	public static SparseVec ParseVector(String file_name){
	    Scanner sc = null;
	    String tmps;
	    SparseVec V = null;
	    try {
	        sc = new Scanner(new File(file_name));
		    while (sc.hasNext()) {
		    	tmps = sc.next();
		    	if(tmps.equals("VECTOR")){
		    		// initialize the matrix
		    		int len = sc.nextInt(); //get length from the file
		    		V = new LLSparseVec(len); //new object	
		    	}else if(tmps.equals("END")){
		    		// finished, return the matrix
		    		sc.close();
		    		return V;
		    	}else if(tmps.equals("SET")){
		    		// set an element
		    		int idx = sc.nextInt(); // index
		    		int val = sc.nextInt(); // value
		    		V.setElement(idx, val); //set element in the index
		    	}else if(tmps.equals("CLEAR")){
		    		// clear an element
		    		int idx = sc.nextInt(); // index
		    		V.clearElement(idx);		    		
		    	}		    	
		    }
		    sc.close();
		    return V;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	
	public static void main(String[] args) {
		try{
			int n = args.length;
			if(n < 2){
				System.out.println("java LLMainClass VEC/MAT File1 A/M File2 ...");
				return;
			}
			String fname;
			String operator;
			String dtype = args[0];	// matrix or vector
			if(dtype.equals("VEC")){
				fname = args[1];
				SparseVec V = ParseVector(fname);	// read V from the first file
				
				if(V == null){	
					// if invalid input file, print NULL and exit
					System.out.println("NULL: Illegal Input File "+fname);
					return;
				}
				
				SparseVec tmpV;
				for(int i = 2; i < n; i+=2){
					operator = args[i];
					fname = args[i+1];
					tmpV = ParseVector(fname);	// read tmpM from the next file
					if(tmpV == null)			// if the file is invalid, skip
					{	System.out.println("NULL: Illegal Input File "+fname);
						return;
					}
					if(operator.equals("A"))
						V = V.addition(tmpV); 			// add tmpV to V
					else if(operator.equals("S"))
						V = V.subtraction(tmpV); 	// multiply tmpV to V
					else if(operator.equals("M"))
						V = V.multiplication(tmpV); 	// multiply tmpV to V
					else{
						System.out.println("NULL: Illegal operator: " + operator );
						return;
					}
					if(V == null)			// operation failed
					{	System.out.println("NULL: Operator "+operator+" Failed, Fname "+fname);
						return;
					}
				}
				
				// output the final matrix
				System.out.println("Final_Vector");
				int[] idx = V.getAllIndices();
				int[] val = V.getAllValues();
				int len, ne;
				len = V.getLength();	// length
				ne = V.numElements();	// number of elements
				System.out.println("LENGTH " + len + " NELEMS " + ne);
				for(int i = 0; i < ne; i++)
					System.out.println("IDX " + idx[i] + " VAL " + val[i]);
				System.out.println("END");			
			}else if(dtype.equals("MAT")){
				fname = args[1];
				SparseM M = ParseMatrix(fname);	// read M from the first file
				
				if(M == null){	
					// if invalid input file, print NULL and exit
					System.out.println("NULL: Illegal Matrix Input, Fname " + fname);
					return;
				}
				
				SparseM tmpM;
				for(int i = 2; i < n; i+=2){
					operator = args[i];
					fname = args[i+1];
					tmpM = ParseMatrix(fname);	// read tmpM from the next file
					if(tmpM == null)			// if the file is invalid, skip
					{	System.out.println("NULL: Illegal Input, Fname " + fname);
						return;
					}
					if(operator.equals("A"))
						M = M.addition(tmpM); 			// add tmpM to M
					else if(operator.equals("S"))
						M = M.subtraction(tmpM); 	// multiply tmpV to V
					else if(operator.equals("M"))
						M = M.multiplication(tmpM); 	// multiply tmpM to M
					else{
						System.out.println("NULL: Illegal operator: " + operator );
						return;
					}
					if(M == null)			// if the file is invalid, skip
					{	System.out.println("NULL: Operation "+operator + " Failed, Fname "+ fname);
						return;
					}				
				}
				
				// output the final matrix
				System.out.println("FINAL_MATRIX");
				int nr, nc, ne;
				nr = M.nrows();			// number of rows
				nc = M.ncols();			// number of columns
				ne = M.numElements();	// number of elements
				System.out.println("NROWS " + nr + " NCOLS " + nc + " NELEMS " + ne);
				// print the matrix in rows
				int[] ridx_list = M.getRowIndices();
				System.out.println(ridx_list[0]); //test
				System.out.println("NUM_ROWS "+ridx_list.length);
				for(int ridx : ridx_list){
					System.out.println("ROW "+ridx);
					int[] one_row_cidx_list = M.getOneRowColIndices(ridx);
					int[] one_row_vals_list = M.getOneRowValues(ridx);
					for(int i = 0; i < one_row_cidx_list.length; ++i)
						System.out.println("RIDX "+ridx+" CIDX " + one_row_cidx_list[i] + " VAL " + one_row_vals_list[i]);
				}
	
				System.out.println("END");
	
			}else{
				System.out.println("The first argument has to be either VEC or MAT, meaning the data is Vector or Matrix");
				return;
			}
	    } catch (Exception e) {
	    	System.out.println("NULL: Something is wrong");
	        return;
	    }
	}
}
