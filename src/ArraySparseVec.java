// implementing SparseVec using array
public class ArraySparseVec implements SparseVec {
	private	int len; 		// length of the vector
	private int nelements = 0; 	// number of nonzero elements, initialized to be zero
	private int [] elements;	// array storing the vector

	// constructor initialize a sparse vector with a given length l
	public ArraySparseVec(int l){
		if(l <= 0) l = 1;			// if zero or negative l, set l = 1;
		len = l;
		elements = new int[len];	// allocate the vector
	}

	// check if the given idx is out of bounds
	private boolean outOfBounds(int idx){
		return((idx < 0) || (idx >= len));
	}

	public int getLength(){
		return len;
	}
	
	public int numElements() {
		return nelements;
	}

	public int getElement(int idx) {
		if(outOfBounds(idx))
			return Integer.MIN_VALUE;
		return elements[idx];
	}

	public void clearElement(int idx) {
		if(outOfBounds(idx))
			return;
		int tmp = elements[idx];
		elements[idx] = 0;	//set the element to zero
		if(tmp != 0)		//if the original element is nonzero, decrement nelements by 1
			nelements--;
		return;
	}
	
	// set the element at idx to val
	public void setElement(int idx, int val) {
		if(outOfBounds(idx))
			return;
		if(val == 0)
			clearElement(idx);
		else{
			int tmp = elements[idx];
			elements[idx] = val;	//set the element to val			
			if(tmp == 0)			//if the original element is zero, increment nelements by 1
				nelements++;
		}
		return;
	}

	// get all nonzero indices
	public int[] getAllIndices() {
		int[] idx = new int[nelements];
		int counter = 0;
		for(int i = 0; i < len; i++){
			if(elements[i] != 0){
				idx[counter++] = i;
			}
		}
		return idx;
	}

	// get all nonzero values
	public int[] getAllValues() {
		int[] val = new int[nelements];
		int counter = 0;
		for(int i = 0; i < len; i++){
			if(elements[i] != 0){
				val[counter++] = elements[i];
			}
		}
		return val;
	}

	// adding otherV into the current matrix, return the result as a new sparseM
	public SparseVec addition(SparseVec otherV) {
		
		// if the length is inconsistent, do nothing and return
		if(otherV.getLength() != len)
			return null;

		SparseVec newVec = new ArraySparseVec(len);
		
		int tmp;
		for(int i = 0; i < len; i++){
			tmp = getElement(i) + otherV.getElement(i);
			newVec.setElement(i,tmp);	// setElement take care of updating nelements
		}
		return newVec;
	}

	// adding otherV into the current matrix, return the result as a new sparseM
	public SparseVec subtraction(SparseVec otherV) {
		
		// if the length is inconsistent, do nothing and return
		if(otherV.getLength() != len)
			return null;

		SparseVec newVec = new ArraySparseVec(len);
		
		int tmp;
		for(int i = 0; i < len; i++){
			tmp = getElement(i) - otherV.getElement(i);
			newVec.setElement(i,tmp);	// setElement take care of updating nelements
		}
		return newVec;
	}

	// thisM .* otherM, return the result as a new sparseM
	public SparseVec multiplication(SparseVec otherV) {
		
		// if the length is inconsistent, do nothing and return
		if(otherV.getLength() != len)
			return null;

		SparseVec newVec = new ArraySparseVec(len);
		
		int tmp;
		for(int i = 0; i < len; i++){
			tmp = getElement(i) * otherV.getElement(i);
			newVec.setElement(i,tmp);	// setElement take care of updating nelements
		}
		return newVec;
	}
}
