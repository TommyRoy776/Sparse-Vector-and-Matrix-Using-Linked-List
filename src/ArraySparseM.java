// implementing SparseM using array
public class ArraySparseM implements SparseM {
	private	int nrows, ncols; 	// number of rows and columns of the matrix
	private int nelements = 0; 	// number of nonzero elements, initialized to be zero
	private int [][] elements;	// array of array for the matrix
	
	// constructor initialize a sparse matrix with nr rows and nc columns
	public ArraySparseM(int nr, int nc){
		if(nr <= 0) nr = 1;	// if zero or negative nr, set nr = 1;
		if(nc <= 0) nc = 1;	// if zero or negative nc, set nc = 1;	
		nrows = nr;	
		ncols = nc;
		elements = new int[nr][nc];	// allocate the matrix
	}

	// check if the given (ridx, cidx) is out of bounds
	private boolean outOfBounds(int ridx, int cidx){
		return((ridx < 0) || (ridx >= nrows) || (cidx < 0) || (cidx >= ncols));
	}
	
	// get number of rows
	public int nrows() {
		return nrows;
	}

	// get number of columns
	public int ncols() {
		return ncols;
	}

	// get number of nonzero entries
	public int numElements() {
		return nelements;
	}

	// get element at (ridx,cidx)
	// if out of bounds, return Integer.MIN_VALUE
	public int getElement(int ridx, int cidx) {
		if(outOfBounds(ridx,cidx))
			return Integer.MIN_VALUE;
		return elements[ridx][cidx];
	}

	// clear element at (ridx,cidx)
	public void clearElement(int ridx, int cidx) {
		if(outOfBounds(ridx,cidx))
			return;
		int tmp = elements[ridx][cidx];
		elements[ridx][cidx] = 0;	//set the element to zero
		if(tmp != 0)				//if the original element is nonzero, decrement nelements by 1
			nelements--;
		return;
	}
	
	// set the element at (ridx,cidx) to val
	public void setElement(int ridx, int cidx, int val) {
		if(outOfBounds(ridx,cidx))
			return;
		if(val == 0)
			clearElement(ridx,cidx);
		else{
			int tmp = elements[ridx][cidx];
			elements[ridx][cidx] = val;	//set the element to val			
			if(tmp == 0)	//if the original element is zero, increment nelements by 1
				nelements++;
		}
		return;
	}

	// get indices of non-empty rows, sorted
	public int[] getRowIndices(){			
		boolean[] isnonzero = new boolean[nrows];
		int counter = 0;
		for(int r = 0; r < nrows; ++r){
			isnonzero[r] = false;
			for(int c = 0; c < ncols; ++c){
				if(elements[r][c] != 0){
					isnonzero[r] = true;
					counter ++;
					break;
				}
			}			
		}
		int[] ridx = new int[counter];
		int counter2=0;
		for(int r = 0; r < nrows; ++r){
			if(isnonzero[r])
				ridx[counter2++] = r;
		}
		return ridx;
	}

	// get indices of non-empty cols, sorted
	public int[] getColIndices(){			
		boolean[] isnonzero = new boolean[ncols];
		int counter = 0;
		for(int c = 0; c < ncols; ++c){
			isnonzero[c] = false;
			for(int r = 0; r < nrows; ++r){
				if(elements[r][c] != 0){
					isnonzero[c] = true;
					counter ++;
					break;
				}
			}			
		}		
		int[] cidx = new int[counter];
		int counter2=0;
		for(int c = 0; c < ncols; ++c){
			if(isnonzero[c])
				cidx[counter2++] = c;
		}
		return cidx;
	}
	
	// get col indices of a given row
	public int[] getOneRowColIndices(int ridx){
		boolean[] isnonzero = new boolean[ncols];
		int counter = 0;
		for(int c = 0; c < ncols; ++c){
			if(elements[ridx][c] != 0){
				isnonzero[c] = true;
				counter ++;
			}else{
				isnonzero[c] = false;
			}
		}		
		
		int[] cidx = new int[counter];
		int counter2=0;
		for(int c = 0; c < ncols; ++c){
			if(isnonzero[c])
				cidx[counter2++] = c;
		}
		return cidx;		
	}
	
	// get values of a given row
	public int[] getOneRowValues(int ridx){
		boolean[] isnonzero = new boolean[ncols];
		int counter = 0;
		for(int c = 0; c < ncols; ++c){
			if(elements[ridx][c] != 0){
				isnonzero[c] = true;
				counter ++;
			}else{
				isnonzero[c] = false;
			}
		}		
		
		int[] vals = new int[counter];
		int counter2=0;
		for(int c = 0; c < ncols; ++c){
			if(isnonzero[c])
				vals[counter2++] = elements[ridx][c];
		}
		return vals;		
	}

	// get col indices of a given col
	public int[] getOneColRowIndices(int cidx){
		boolean[] isnonzero = new boolean[nrows];
		int counter = 0;
		for(int r = 0; r < nrows; ++r){
			if(elements[r][cidx] != 0){
				isnonzero[r] = true;
				counter ++;
			}else{
				isnonzero[r] = false;
			}
		}		
		
		int[] ridx = new int[counter];
		int counter2=0;
		for(int r = 0; r < nrows; ++r){
			if(isnonzero[r])
				ridx[counter2++] = r;
		}
		return ridx;		
	}
	
	// get values of a given Col
	public int[] getOneColValues(int cidx){
		boolean[] isnonzero = new boolean[nrows];
		int counter = 0;
		for(int r = 0; r < nrows; ++r){
			if(elements[r][cidx] != 0){
				isnonzero[r] = true;
				counter ++;
			}else{
				isnonzero[r] = false;
			}
		}		
		
		int[] vals = new int[counter];
		int counter2=0;
		for(int r = 0; r < nrows; ++r){
			if(isnonzero[r])
				vals[counter2++] = elements[r][cidx];
		}
		return vals;		
	}
	
	// adding otherM into the current matrix, return the result as a new sparseM
	public SparseM addition(SparseM otherM) {
		
		// if the number of rows and cols are inconsistent, do nothing and return
		if((otherM.nrows() != nrows) || (otherM.ncols() != ncols))
			return null;

		SparseM newM = new ArraySparseM(nrows, ncols);
		
		int tmp;
		for(int r = 0; r < nrows; r++)
			for(int c = 0; c < ncols; c++){
				tmp = getElement(r,c) + otherM.getElement(r,c);
				newM.setElement(r,c,tmp);	// setElement take care of updating nelements
			}
		return newM;
	}

	// thisM - otherM, return the result as a new sparseM
	public SparseM subtraction(SparseM otherM) {
		
		// if the number of rows and cols are inconsistent, do nothing and return
		if((otherM.nrows() != nrows) || (otherM.ncols() != ncols))
			return null;

		SparseM newM = new ArraySparseM(nrows, ncols);
		
		int tmp;
		for(int r = 0; r < nrows; r++)
			for(int c = 0; c < ncols; c++){
				tmp = getElement(r,c) - otherM.getElement(r,c);
				newM.setElement(r,c,tmp);	// setElement take care of updating nelements
			}
		return newM;
	}

	// thisM .* otherM, return the result as a new sparseM
	public SparseM multiplication(SparseM otherM) {
		
		// if the number of rows and cols are inconsistent, do nothing and return
		if((otherM.nrows() != nrows) || (otherM.ncols() != ncols))
			return null;

		SparseM newM = new ArraySparseM(nrows, ncols);
		
		int tmp;
		for(int r = 0; r < nrows; r++)
			for(int c = 0; c < ncols; c++){
				tmp = getElement(r,c) * otherM.getElement(r,c);
				newM.setElement(r,c,tmp);	// setElement take care of updating nelements
			}
		return newM;
	}

}
