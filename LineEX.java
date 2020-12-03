public class LineEX{
	int ys,ye;
	double xs,ki;
	public int x1,y1,x2,y2;
	LineEX next;
	LineEX(){
		ki=0;
		ys=ye=-1;
		next=null;
	}
	LineEX(int x1,int y1,int x2,int y2){
		//System.out.println(x1+" "+y1+" "+x2+" "+y2+"");
		this.x1=x1;
		this.x2=x2;
		this.y1=y1;
		this.y2=y2;
		if(y1<y2){
			ys=y1;
			ye=y2;
			xs=x1;
			ki=(double)(x1-x2)/(y1-y2);
		}
		else if(y1>y2){
			ys=y2;
			ye=y1;
			xs=x2;
			ki=(double)(x1-x2)/(y1-y2);
		}
		else{
			ys=-1;
			ki=0;
		}
		next=null;
	}
}