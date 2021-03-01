
public interface SparseM {
	// problem 2
	int nrows();							//return number of rows 
	int ncols();							//return number of columns
	int numElements();						//return total number of nonzero elements in the matrix
	int getElement(int ridx, int cidx);	   	//return the element at a given entry (ridx, cidx), 
	void clearElement(int ridx, int cidx); 	//set the element at (ridx,cidx) to zero
	void setElement(int ridx, int cidx, int val); 			//set the element at (ridx,cidx) to val
	// get indices of non-empty rows, sorted
	int[] getRowIndices();
	// get indices of non-empty cols, sorted
	int[] getOneRowColIndices(int ridx);
	// get values of a given row
	int[] getOneRowValues(int ridx);

	// methods for problem 3
	SparseM addition(SparseM otherM);			// this matrix + otherM
												// return a new matrix storing the result
	SparseM subtraction(SparseM otherM);		// this matrix - otherM
												// return a new matrix storing the result
	SparseM multiplication(SparseM otherM);		// this matrix .* with otherM
												// return a new matrix storing the result
}
