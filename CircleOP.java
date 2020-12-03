import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.math.*;

public class CircleOP extends DrawOP{
	CircleOP(PanelEX p){
		super(p);
	}
	public void pushBack(int x,int y,int r){
		clrDelay();
		setMask(CircleOP.PUSHBACK);
		initGraphics();
		drawCircleB(x,y,r);
	}
	public void pushForward(int x,int y,int r){
		clrDelay();
		setMask(CircleOP.ONSCREEN);
		initGraphics();
		drawCircleB(x,y,r);
	}

	public void Breasham(int x,int y,int r){
		setDelay();
		setMask(CircleOP.BOTH);
		initGraphics();
		drawCircleB(x,y,r);
	}
	public void DDA(int x,int y,int r){
		setDelay();
		setMask(CircleOP.BOTH);
		initGraphics();
		drawCircleD(x,y,r);
	}
	public void MidPoint(int x,int y,int r){
		setDelay();
		setMask(CircleOP.BOTH);
		initGraphics();
		drawCircleM(x,y,r);
	}
	
	public void drawCircleB(int ox,int oy,int r){
		int d;
		int x=0,y=r;
		d=(x+1)*(x+1)+(y-1)*(y-1)-r*r;
		while(y>=0){ 
			setPixel(x+ox,y+oy);
			setPixel(x+ox,-y+oy);
			setPixel(-x+ox,y+oy);
			setPixel(-x+ox,-y+oy);
			if(d<0){
				if( (2*d+2*y-1) <= 0){
					//h
					d=d+2*x+3;
					x++;
				}
				else{
					//d
					d=d+2*x+3-2*y+3;
					x++;
					y--;
				}
			}
			else if(d>0){
				if( (2*d-2*x-1) <= 0){
					//d
					d=d+2*x+3-2*y+3;
					x++;
					y--;
				}
				else{
					//v
					d=d-2*y+3;
					y--;
				}
			}
			else{
				//d
				d=d+2*x+3-2*y-1;
				x++;
				y--;
			}
		}
	}
	public void drawCircleM(int ox,int oy,int r){
		double f=1-r;
		int x=0,y=r;
		while(x<y){
			setPixel(x+ox,y+oy);
			setPixel(x+ox,-y+oy);
			setPixel(-x+ox,y+oy);
			setPixel(-x+ox,-y+oy);
			setPixel(y+ox,x+oy);
			setPixel(y+ox,-x+oy);
			setPixel(-y+ox,x+oy);
			setPixel(-y+ox,-x+oy);
			if(f<0){
				x++;
				f=f+2*x+1;
			}
			else{
				x++;
				y--;
				f=f+2*x+1-2*y;
			}
		}
	}
	public void drawCircleD(int ox,int oy,int r){
		double x=0,y=r,e;
		int n;
		int xt=0,yt=r;
		for(n=0;;n++)
			if(Math.pow(2,n)>y) break;
		e=1/Math.pow(2,n);
		while(y>0){
			setPixel(xt+ox,yt+oy);
			setPixel(xt+ox,-yt+oy);
			setPixel(-xt+ox,yt+oy);
			setPixel(-xt+ox,-yt+oy);
			x=x+e*y;
			y=(1+e*e)*y-e*x;
			xt=(int)x;
			yt=(int)y;
		}
	}
}