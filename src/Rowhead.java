
public class Rowhead {
	private int head; //Row head index
	private LLSparseVec right; //store a LLsparseVec
	private Rowhead down; //pointer to next row head
	private Rowhead up;
	
	
	public Rowhead(int head){
		this.head = head;
		this.right = null;
		this.down = null;
	} 
	
	public int getHead(){ 
		return head;
	}
	
	public LLSparseVec getright() {
		return right;
	}
	public Rowhead getdown() {
		return down;
	}
	public Rowhead getup() {
		return up;
	}
	public void setright(LLSparseVec v) {
		this.right = v ;
	}
	public void setdown(Rowhead h){
		this.down = h;
	}
	public void setup(Rowhead h){
		this.up = h;
	}
}

