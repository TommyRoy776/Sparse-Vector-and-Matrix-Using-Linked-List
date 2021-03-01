public class Node<E> {
	private E element;
	private Node<E> next;
	private Node<E> pre;
	private int index;
   /* public Node(){
    	element = null;
    	pre = null;
    	next = null;
    }*/
	public Node(E e,int index) {
		element = e;
		pre = null;
		next = null;
		this.index = index;
	}

	public E getelement() {
		return element;
	}

	public Node getpre() {
		return pre;
	}

	public Node getnext() {
		return next;
	}

	public void setnext(Node n) {
		next = n;
	}

	public void setpre(Node p) {
		pre = p;
	}
	public int getindex() {
		return index;
	}
	public void setElement(E value){
		element = value;
	}
}
