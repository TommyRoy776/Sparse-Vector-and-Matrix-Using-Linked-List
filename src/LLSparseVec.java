public class LLSparseVec implements SparseVec {
	private int len = 0; // length of the vector
	private int nelements = 0; // number of nonzero elements, initialized to be
								// zero
	private Node<Integer> head, tail;

	public boolean isEmpty() {
		return (nelements == 0);
	}

	public LLSparseVec(int len) {
		this.len = len;
	}

	@Override
	public int getLength() {
		return len;
	}

	private boolean outOfBounds(int idx) {
		return ((idx < 0) || (idx >= len));
	}

	@Override
	public int numElements() {
		return nelements;
	}

	public Node<Integer> getNode(int idx) {
		Node<Integer> temp = head;
		if (outOfBounds(idx))
			return null;
		while (temp != null && temp.getindex() != idx) { // Find the right node
			temp = temp.getnext();
		}
		if (temp == null) {
			return null;
		}
		return temp;
	}

	public Node<Integer> getHead() {
		return head;
	}

	/*public int getTail() {
		return tail.getelement();
	}*/

	@Override
	public int getElement(int idx) {
		if (outOfBounds(idx))
			return Integer.MIN_VALUE;
		Node<Integer> temp = head;
		while (temp != null && temp.getindex() != idx) { // Find the right node
			temp = temp.getnext();
		}
		if (temp == null) { // If the index was not found, return 0
			return 0;
		}
		return temp.getelement(); // If the index was found, return
	}

	@Override
	public void clearElement(int idx) {
		if (outOfBounds(idx))
			return;
		if (getNode(idx) == null) {
			return;
		}
		Node<Integer> temp = getNode(idx); // get the reference of the node
		int num = temp.getelement();
		Node<Integer> n;
		if (nelements == 1) { //if only 1 element in vector  
			tail = null;
			head = null;
			if (num != 0) { // if the original element is nonzero, decrement
							// nelements by 1
				nelements--;
			}
			return;
		}
		if (temp.getnext() == null) { // if temp is the last node
			n = temp.getpre();
			n.setnext(null);
			tail = n;
			if (num != 0) { // if the original element is nonzero, decrement
							// nelements by 1
				nelements--;
			}
			return;
		}
		if (temp.getpre() == null) { // if temp is the first node
			n = temp.getnext();
			n.setpre(null);
			head = n;
			if (num != 0) { // if the original element is nonzero, decrement
							// nelements by 1
				nelements--;
			}
			return;
		}
		
		n = temp.getpre();  //if the node element in the middle
		n.setnext(temp.getnext());
		if (num != 0) { // if the original element is nonzero, decrement
						// nelements by 1
			nelements--;
		}
	}

	@Override
	public void setElement(int idx, int val) {
		if (outOfBounds(idx))
			return;
		if (val == 0)
			clearElement(idx);
		else {
			Node<Integer> newNode = new Node<Integer>(val, idx);
			if (this.isEmpty()) {
				tail = newNode;
				head = tail;
				nelements++;
				return;
			}
			Node<Integer> temp = head;
			while (temp != null) {
				if (newNode.getindex() == temp.getindex()) { // if the node
																// exists
					temp.setElement(val);
					return;
				}
				if (newNode.getindex() < temp.getindex()) { // if the index of
															// newNode < index
															// of temp
					if (temp == head) { // if temp is head, new node would be
										// the head
						temp.setpre(newNode);
						newNode.setnext(temp);
						head = newNode;
						nelements++;
						return;
					}
					Node<Integer> n = temp.getpre();  //fit the node in the right position
					temp.setpre(newNode);
					newNode.setnext(temp);
					newNode.setpre(n);
					n.setnext(newNode);
					nelements++;
					return;
				}
				if (newNode.getindex() > temp.getindex()) {
					if (temp.getnext() == null) { // if temp is the last node,
													// new node would be tail
						temp.setnext(newNode);
						newNode.setpre(temp);
						tail = newNode;
						nelements++;
						return;
					}
					temp = temp.getnext();
				}

			}
			/*
			 * tail.setnext(newNode); newNode.setpre(tail); tail = newNode;
			 * size++;
			 */
		}
	}

	@Override
	public int[] getAllIndices() {
		int[] idx = new int[nelements];
		// int[] idx = new int[20]; //test
		int i = 0;
		Node<Integer> temp = head;
		while (temp != null) {
			idx[i] = temp.getindex();
			temp = temp.getnext();
			i++;
		}
		return idx;
	}

	@Override
	public int[] getAllValues() {
		int[] val = new int[nelements];
		// int[] val = new int[20]; //test
		int i = 0;
		Node<Integer> temp = head;
		while (temp != null) {
			val[i] = temp.getelement();
			temp = temp.getnext();
			i++;
		}
		return val;
	}

	@Override
	public SparseVec addition(SparseVec otherV) {
		if (otherV.getLength() != len) // if the length is inconsistent, do
										// nothing and return
			return null;
		SparseVec newVec = new LLSparseVec(len);
		int tmp;
		for (int i = 0; i < len; i++) {
			tmp = getElement(i) + otherV.getElement(i);
			newVec.setElement(i, tmp); // setElement take care of updating
										// nelements*/
		}
		return newVec;
	}

	@Override
	public SparseVec subtraction(SparseVec otherV) {
		if (otherV.getLength() != len) // if the length is inconsistent, do
										// nothing and return
			return null;
		SparseVec newVec = new LLSparseVec(len);
		int tmp;
		for (int i = 0; i < len; i++) {
			tmp = getElement(i) - otherV.getElement(i);
			newVec.setElement(i, tmp); // setElement take care of updating
										// nelements*/
		}
		return newVec;
	}

	@Override
	public SparseVec multiplication(SparseVec otherV) {
		if (otherV.getLength() != len) // if the length is inconsistent, do
										// nothing and return
			return null;
		SparseVec newVec = new LLSparseVec(len);
		int tmp;
		for (int i = 0; i < len; i++) {
			tmp = getElement(i) * otherV.getElement(i);
			newVec.setElement(i, tmp); // setElement take care of updating
										// nelements*/
		}
		return newVec;
	}

}
