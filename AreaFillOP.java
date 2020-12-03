import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.math.*;

public class AreaFillOP extends DrawOP{
	AreaFillOP(PanelEX p,LineOP line){
		super(p);
		this.line=line;
		top=0;
		num=3;
		linelist=new LineEX[21];
		xstack=new int[20];
		ystack=new int[20];
	}
	public void SeedFill(){
		int ct=color.getRGB();
		//System.out.println(Integer.toBinaryString(ct));
		//try{Thread.sleep(500);}catch(Exception e){}
		int cf;
		setDelay();
		setMask(AreaFillOP.BOTH);
		initGraphics();
		int[] xs=new int[1280*800*5];
		int[] ys=new int[1280*800*5];
		int[][] table={{0,1},{0,-1},{1,0},{-1,0}};
		int point=1;
		int x,y;
		xs[0]=xstack[top-1];
		ys[0]=ystack[top-1];
		while(point!=0){
			point--;
			x=xs[point];
			y=ys[point];
			setPixel(x,y);
			for(int i=0;i<4;i++){
				cf=getPixel(x+table[i][0],y+table[i][1]);
				if(cf != ct){
					xs[point]=x+table[i][0];
					ys[point]=y+table[i][1];
					point++;
				}
			}
		}
	}
	public void ScanbeamFill(){
		setDelay();
		setMask(AreaFillOP.BOTH);
		initGraphics();
		xstack[num]=xstack[0];
		ystack[num]=ystack[0];
		LineEX head,t,yhead;
		yhead=new LineEX();
		int i,y;
		for(i=0;i<num;i++){
			line.pushBack(xstack[i],ystack[i],xstack[i+1],ystack[i+1]);
			linelist[i]=new LineEX(xstack[i],ystack[i],xstack[i+1],ystack[i+1]);
		}
		for(i=0;i<height;i++)	p[i]=new LineEX();

		for(i=0;i<num;i++){
			if(linelist[i].ys==-1) continue;
			head=p[linelist[i].ys];
			while(head.next!=null){
				if( linelist[i].xs < head.next.xs || (linelist[i].xs==head.next.xs)&&(linelist[i].ki<head.next.ki)){
					linelist[i].next=head.next;
					head.next=linelist[i];
					break;
				}
				head=head.next;
			}
			if(head.next==null){
				head.next=linelist[i];
			}
		}
		for(y=0;y<height;y++){
			head=yhead;
			while(head.next!=null){
				if(y > head.next.ye){
					head.next=head.next.next;
				}
				else{
					head.next.xs+=head.next.ki;
					head=head.next;
				}
			}
			head=yhead;
			while(head.next!=null){
				for(i=(int)head.next.xs;i<(int)head.next.next.xs;i++)
					setPixel(i,y);
				head=head.next.next;
			}
			head=yhead;
			if(p[y].next!=null){  //insert node
				while(head.next!=null){
					if(p[y].next==null) break;
					if(head.next.xs > p[y].next.xs || (head.next.xs == p[y].next.xs)&&(head.next.ki > p[y].next.ki)){
						t=p[y].next.next;
						p[y].next.next=head.next;
						head.next=p[y].next;
						p[y].next=t;
					}
					head=head.next;
				}
				if(p[y].next!=null){
					head.next=p[y].next;
				}
			}
		}
	}
	public void EdgeFill(){
		setDelay();
		setMask(AreaFillOP.XOR);
		initGraphics();
		int xmax=0;
		int i,j,m;
		float k,xt;
		xstack[num]=xstack[0];
		ystack[num]=ystack[0];

		for(i=0;i<num;i++){
			line.pushBack(xstack[i],ystack[i],xstack[i+1],ystack[i+1]);
			if(xstack[i]>xmax)	xmax=xstack[i];
		}
		/*try{
			Thread.sleep(1000);
		}catch(Exception e){}*/
		for(i=0;i<num;i++){
			if(ystack[i+1]==ystack[i]) continue;
			k=(float)(xstack[i+1]-xstack[i])/(ystack[i+1]-ystack[i]);
			if(ystack[i]>ystack[i+1]){
				xt=xstack[i+1];
				for(j=ystack[i+1];j<ystack[i];j++){
					for(m=(int)xt;m<=xmax;m++)
						setPixel(m,j);
					xt+=k;
				}
			}
			else if(ystack[i]<ystack[i+1]){
				xt=xstack[i];
				for(j=ystack[i];j<ystack[i+1];j++){
					for(m=(int)xt;m<=xmax;m++)
						setPixel(m,j);
					xt+=k;
				}
			}
		}
	}
	
	public int add(int x,int y){
		xstack[top]=x;
		ystack[top]=y;
		top++;
		if(top<num) return STACKNOTFULL;
		else if(top==num) return STACKFULL;
		else if(top==num+1) return STACKFULLWITHSEED;
		else return ERROR;
		
	}
	public void drawLineEX(){
		if(top==1) return;
		line.fastMidPoint(xstack[top-2],ystack[top-2],xstack[top-1],ystack[top-1]);
	}
	public void drawLineEXNotOnBuffer(){
		if(top==1) return;
		line.pushForward(xstack[top-2],ystack[top-2],xstack[top-1],ystack[top-1]);
	}
	public void drawLineFromLastToFirst(){
		if(top==1) return;
		line.fastMidPoint(xstack[top-1],ystack[top-1],xstack[0],ystack[0]);
	}
	public void drawLineFromLastToFirstNotOnBuffer(){
		if(top==1) return;
		line.pushForward(xstack[top-1],ystack[top-1],xstack[0],ystack[0]);
    }
	public void emptyStack(){
		top=0;
	}
	public void setColor(Color c){
		color=c;
	}
	public void setEdgeNum(int n){
		num=n;
	}
	public void setHeight(int n){
		height=n;
		p=new LineEX[n];
	}
	int num,top,height;
	int[] xstack,ystack;
	LineOP line;
	LineEX[] p,linelist;
	public static final int STACKFULL=1;
	public static final int STACKFULLWITHSEED=2;
	public static final int STACKNOTFULL=3;
	public static final int ERROR=4;
}