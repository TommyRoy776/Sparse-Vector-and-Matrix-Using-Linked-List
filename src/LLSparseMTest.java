
public class LLSparseMTest {

	public static void main(String[] args) {
		LLSparseM M = new LLSparseM (30,30) ;
		M.setElement(11, 20, 8);
		M.setElement(11, 2, 8);
		M.setElement(2, 23, 8);
		M.setElement(1, 12, 8);
		M.clearElement(2, 23);
		//M.clearElement(11, 2);

		
       for(int i = 0; i<2; i++){
    	   int[] A = M.getRowIndices();
    	   System.out.println(A[i]);
       }
       System.out.println("N:"+ M.numElements());
	}

}
