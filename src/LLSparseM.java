
public class LLSparseM implements SparseM {
	private int nrows, ncols; // number of rows and columns of the matrix
	private int nelements = 0; // number of nonzero elements, initialized to be
	// zero
	//private int size = 0; // size for Rowhead
	//private LLSparseVec LV;
	private Rowhead head, tail;

	public LLSparseM(int nr, int nc) {
		if (nr <= 0)
			nr = 1; // if zero or negative nr, set nr = 1;
		if (nc <= 0)
			nc = 1; // if zero or negative nc, set nc = 1;
		nrows = nr;
		ncols = nc;
	}

	public boolean isEmpty() {
		return (nelements == 0);
	}

	@Override
	public int nrows() {
		return nrows;
	}

	@Override
	public int ncols() {
		return ncols;
	}

	@Override
	public int numElements() {
		return nelements;
	}

	public Rowhead getHead() {
		return head;
	}

	private boolean outOfBounds(int ridx, int cidx) {
		return ((ridx < 0) || (ridx >= nrows) || (cidx < 0) || (cidx >= ncols));
	}

	private Node<Integer> getNode(int ridx, int cidx) {
		if (outOfBounds(ridx, cidx))
			return null;
		Rowhead temp = head;
		while (temp != null && temp.getHead() != ridx) { // Find the right
															// rowHead
			temp = temp.getdown();
		}
		if (temp == null) {
			return null;
		}
		LLSparseVec tempV = temp.getright();
		return tempV.getNode(cidx);
	}

	@Override
	public int getElement(int ridx, int cidx) {
		if (outOfBounds(ridx, cidx))
			return Integer.MIN_VALUE;
		Node<Integer> row = getNode(ridx, cidx); // find the node of the element
		if (row == null) {
			return 0;
		}
		return row.getelement(); // return the element
	}

	@Override
	public void clearElement(int ridx, int cidx) {
		if (outOfBounds(ridx, cidx))
			return;
		if (this.isEmpty()) {
			return;
		}
		Rowhead temp = head;
		int num;
		if (nelements == 1) {
			num = temp.getright().getElement(cidx);
			tail = null;
			head = null;
			if (num != 0) { // if the original element is nonzero, decrement
							// nelements by 1
				nelements--;
			}
			return;
		}
		while (temp != null) {
			num = temp.getright().getElement(cidx);
			if (temp.getHead() == ridx) {
				temp.getright().clearElement(cidx);
				if (temp.getright().getAllValues().length == 0) {
					if (temp.getup() == null) { // this is first Rowhead
						temp.getdown().setup(null);
						if (num != 0) {
							nelements--;
						}
						return;
					}
					if (temp.getdown() == null) { // this is the last Rowhead
						temp.getup().setdown(null);
						if (num != 0) {
							nelements--;
						}
						return;
					}
					Rowhead tempR = temp.getup();
					tempR.setdown(temp.getdown());
					if (num != 0) {
						nelements--;
					}
					return;
				}
				if (num != 0) {
					nelements--;
				}
			}
			temp = temp.getdown();
		}
	}

	@Override
	public void setElement(int ridx, int cidx, int val) {
		if (outOfBounds(ridx, cidx))
			return;
		if (val == 0)
			return;
		else {
			Rowhead newnode = new Rowhead(ridx); // get the node of element
			Rowhead temp = head;
			if (this.isEmpty()) {
				tail = newnode;
				head = tail;
				LLSparseVec newV = new LLSparseVec(ncols);
				newnode.setright(newV);
				newnode.getright().setElement(cidx, val);
				nelements++;
				return;
			}
			while (temp != null) {
				if (newnode.getHead() == temp.getHead()) {
					/* LLSparseVec tempV = temp.getright() ; */
					if (temp.getright() == null) {
						LLSparseVec newV = new LLSparseVec(ncols);
						temp.setright(newV);
						temp.getright().setElement(cidx, val);
						nelements++;
						return;
					}
					if(temp.getright().getElement(cidx) == 0){
						nelements++;
					}
					temp.getright().setElement(cidx, val);	
					return;
				}
				if (newnode.getHead() < temp.getHead()) {
					LLSparseVec newV = new LLSparseVec(ncols);
					if (temp == head) {
						temp.setup(newnode);
						newnode.setdown(temp);
						head = newnode;
						newnode.setright(newV);
						newnode.getright().setElement(cidx, val);
						nelements++;
						return;
					}
					Rowhead n = temp.getup();
					temp.setup(newnode);
					newnode.setdown(temp);
					newnode.setup(n);
					n.setdown(newnode);
					newnode.setright(newV);
					newnode.getright().setElement(cidx, val);
					nelements++;
					return;
				}
				if (newnode.getHead() > temp.getHead()) {
					if (temp.getdown() == null) { // if temp is the last
													// node,
						LLSparseVec newV = new LLSparseVec(ncols);
						temp.setdown(newnode);
						newnode.setup(temp);
						tail = newnode;
						newnode.setright(newV);
						newnode.getright().setElement(cidx, val);
						nelements++;
						return;
					}
					temp = temp.getdown();
				}
			}

		}
	}

	@Override
	public int[] getRowIndices() {
		Rowhead temp = head;
		int count = 0;
		while (temp != null) {
			temp = temp.getdown();
			count++;
		}
		int[] index = new int[count];
		count = 0;
		temp = head;
		while (temp != null) {
			index[count] = temp.getHead();
			temp = temp.getdown();
			count++;
		}
		return index;
	}

	@Override
	public int[] getOneRowColIndices(int ridx) {
		Rowhead temp = head;
		while (temp != null && temp.getHead() != ridx) {
			temp = temp.getdown();
		}
		LLSparseVec tempv = temp.getright();
		return tempv.getAllIndices();
	}

	@Override
	public int[] getOneRowValues(int ridx) {
		Rowhead temp = head;
		while (temp != null && temp.getHead() != ridx) {
			temp = temp.getdown();
		}
		LLSparseVec tempv = temp.getright();
		return tempv.getAllValues();
	}

	@Override
	public SparseM addition(SparseM otherM) {
		// if the number of rows and cols are inconsistent, do nothing and
		// return
		if ((otherM.nrows() != nrows) || (otherM.ncols() != ncols))
			return null;
		SparseM newM = new LLSparseM(nrows, ncols);
		int temp;
		for (int r = 0; r < nrows; r++) {
			for (int c = 0; c < ncols; c++) {
				temp = this.getElement(r, c) + otherM.getElement(r, c);
				newM.setElement(r, c, temp); // setElement take care of updating
												// nelements
			}
		}
		return newM;
	}

	@Override
	public SparseM subtraction(SparseM otherM) {
		// if the number of rows and cols are inconsistent, do nothing and
		// return
		if ((otherM.nrows() != nrows) || (otherM.ncols() != ncols))
			return null;
		SparseM newM = new LLSparseM(nrows, ncols);
		int temp;
		for (int r = 0; r < nrows; r++) {
			for (int c = 0; c < ncols; c++) {
				temp = this.getElement(r, c) - otherM.getElement(r, c);
				newM.setElement(r, c, temp); // setElement take care of updating
												// nelements
			}
		}
		return newM;
	}

	@Override
	public SparseM multiplication(SparseM otherM) {
		// if the number of rows and cols are inconsistent, do nothing and
		// return
		if ((otherM.nrows() != nrows) || (otherM.ncols() != ncols))
			return null;
		SparseM newM = new LLSparseM(nrows, ncols);
		int temp;
		for (int r = 0; r < nrows; r++) {
			for (int c = 0; c < ncols; c++) {
				temp = this.getElement(r, c) * otherM.getElement(r, c);
				newM.setElement(r, c, temp); // setElement take care of updating
												// nelements
			}
		}
		return newM;
	}

}
