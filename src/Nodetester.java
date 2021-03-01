
public class Nodetester {

	public static void main(String[] args) {
		int len = 5;
		int size =0;
		Node<Integer> head = null; 
		Node<Integer> tail = null;

		for(int i =0; i<len; i++){
		Node<Integer> newNode = new Node<Integer>(i,i);
		  if (size == 0) {
				tail = newNode;
				head = tail;
				size++;
				continue;
			  }
		  tail.setnext(newNode);
		  tail = newNode;
		   size++;		
		}
		
		System.out.println(head.getnext().getnext().getnext().getnext().getelement());
	  }
	
	}

