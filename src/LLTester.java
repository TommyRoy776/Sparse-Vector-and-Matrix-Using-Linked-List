
public class LLTester {

	public static void main(String[] args) {
		LLSparseVec V = new LLSparseVec(20); 
		V.setElement(4, 4);
		V.setElement(2, 2);
		V.setElement(11, 11);
		V.setElement(1, 1);
		V.setElement(3, 3);
		V.setElement(13, 13);
		V.setElement(0, 5);
		V.clearElement(0);
		V.clearElement(0);
		//System.out.println(V.getNode(0).getelement());
		 Node temp = V.getHead();
		 while(temp != null){
			 System.out.println(temp.getelement());
			 temp = temp.getnext();
		 }
		 System.out.println(V.numElements());
		 System.out.println(V.getElement(18));
	}

}
