import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.math.*;

public class LineOP extends DrawOP{
	ClipOP clip;
	LineEX clipSeg;
	LineEX l;
	
	LineOP(PanelEX p,ClipOP clip){
		super(p);
		this.clip=clip;
	}
	public void fastMidPoint(int x1,int y1,int x2,int y2){
		clrDelay();
		setMask(LineOP.BOTH);
		initGraphics();
		drawLineM(x1,y1,x2,y2);
	}
	public void Breasham(int x1,int y1,int x2,int y2){
		l=clip.clip(x1,y1,x2,y2);
		if(l==null)
			return;
		else{
			x1=l.x1;
			y1=l.y1;
			x2=l.x2;
			y2=l.y2;
		}
		setDelay();
		setMask(LineOP.BOTH);
		initGraphics();
		drawLineB(x1,y1,x2,y2);
	}
	public void DDA(int x1,int y1,int x2,int y2){
		l=clip.clip(x1,y1,x2,y2);
		if(l==null) return;
		else{
			x1=l.x1;
			y1=l.y1;
			x2=l.x2;
			y2=l.y2;
		}
		setDelay();
		setMask(LineOP.BOTH);
		initGraphics();
		drawLineD(x1,y1,x2,y2);
	}
	public void MidPoint(int x1,int y1,int x2,int y2){
		l=clip.clip(x1,y1,x2,y2);
		if(l==null) return;
		else{
			x1=l.x1;
			y1=l.y1;
			x2=l.x2;
			y2=l.y2;
		}
		setDelay();
		setMask(LineOP.BOTH);
		initGraphics();
		drawLineM(x1,y1,x2,y2);
	}
	public void pushBack(int x1,int y1,int x2,int y2){
		clrDelay();
		setMask(LineOP.PUSHBACK);
		initGraphics();
		drawLineM(x1,y1,x2,y2);
	}
	public void pushForward(int x1,int y1,int x2,int y2){
		clrDelay();
		setMask(LineOP.ONSCREEN);
		initGraphics();
		drawLineM(x1,y1,x2,y2);
	}
	public void drawLineB(int startX,int startY,int endX,int endY){
		int x1,y1,tempSx,tempSy;
		float deltaX,deltaY,Pk;
		boolean changed;
		x1=startX;
		y1=startY;
		deltaX=Math.abs(startX-endX);
		deltaY=Math.abs(startY-endY);
        if(startX<=endX) tempSx=1; else tempSx=-1;
		if(startY<=endY) tempSy=1; else tempSy=-1;

		if(deltaX<deltaY){
			float temp;
			temp=deltaX;
			deltaX=deltaY;
			deltaY=temp;
			changed=true;
		}
		else
			changed=false;
		Pk=2*deltaY-deltaX;
		setPixel(x1,y1);
		for(int i=1;i<=deltaX;i++){
			if(Pk<0){
				if(changed==true)
					y1=y1+tempSy;
				else
					x1=x1+tempSx;

				setPixel(x1,y1);
				Pk=Pk+2*deltaY;
			}
			else{
				x1=x1+tempSx;
				y1=y1+tempSy;

				setPixel(x1,y1);
				Pk=Pk+2*deltaY-2*deltaX;
			}
		}
	}/*
	public void drawLineB(int startX,int startY,int endX,int endY){
		int x1,y1,tempSx,tempSy;
		float deltaX,deltaY,Pk;
		boolean changed;
		x1=startX;
		y1=startY;
		deltaX=Math.abs(startX-endX);
		deltaY=Math.abs(startY-endY);
        if(startX<=endX) tempSx=1; else tempSx=-1;
		if(startY<=endX) tempSy=1; else tempSy=-1;

		if(deltaX<deltaY)
		{
			float temp;
			temp=deltaX;
			deltaX=deltaY;
			deltaY=temp;
			changed=true;
		}
		else
			changed=false;

		Pk=2*deltaY-deltaX;
		setPixel(x1,y1);
		for(int i=1;i<deltaX;i++)
		{
			if(Pk<0)
			{
				if(changed==true)
					y1=y1+tempSy;
				else
					x1=x1+tempSx;

				setPixel(x1,y1);
				Pk=Pk+2*deltaY;
			}
			else
			{
				
				x1=x1+tempSx;
				y1=y1+tempSy;

				setPixel(x1,y1);
				Pk=Pk+2*deltaY-2*deltaX;
			}
		}
	}*/
	public void drawLineD(int x1,int y1,int x2,int y2){
		int x,y;
		float x0,y0;
		float dx,dy,k;
		x=x1;
		y=y1;
		x0=x1+0.5f;
		y0=y1+0.5f;
		dx=(float)(x1-x2);
		dy=(float)(y1-y2);
		if(x1==x2){
			if(dy<=0){
				for(y=y1;y<=y2;y++)
					setPixel(x,y);
			}
			else{
				for(y=y1;y>=y2;y--)
					setPixel(x,y);
			}
		}
		else if(y1==y2){
			if(dx<=0){
				for(x=x1;x<=x2;x++)
					setPixel(x,y);
			}
			else{
				for(x=x1;x>=x2;x--)
					setPixel(x,y);
			}
		}
		else{
			k=Math.abs(dy/dx);
			
			if(k<=1){				
				if(dx<=0){
					for(x=x1;x<=x2;x++){
				    	setPixel(x,(int)y0);
						if(dy<=0)	y0=y0+k;
						else	y0=y0-k;
				    }
				}
				else{
					for(x=x1;x>=x2;x--){
				    	setPixel(x,(int)y0);
						if(dy<=0)	y0=y0+k;
						else	y0=y0-k; 
				    }
				}
			}
			else{
				if(dy>=0){
					for(y=y1;y>=y2;y--){
						setPixel((int)x0,y);
						if(dx<=0)	x0=x0+1/k;
						else	x0=x0-1/k;
					}
				}
				else{
					for(y=y1;y<=y2;y++){
						setPixel((int)x0,y);
						if(dx<=0)	x0=x0+1/k;
						else	x0=x0-1/k;
						 
					}
				}
			}
		}
	}
	public void drawLineM(int startX,int startY,int endX,int endY){
        int a, b ;
		a = startY - endY ;
		b = endX - startX ;
		int iDirectionX, iDirectionY ;
		iDirectionX = iDirectionY = 1 ;
		if(b<0)
			iDirectionX = -1 ;
		if(a>0)
			iDirectionY = -1 ;
		int iDistance,iDeltaSmall, iDeltaBig ,iCurrX, iCurrY ;
		int iStep ;
		iDeltaSmall = 2*a*b*iDirectionX*iDirectionY ;
		iCurrX = startX ;
		iCurrY = startY ;
		if(Math.abs(b) > Math.abs(a))
		{
			iDeltaBig = 2*b*b + iDeltaSmall ;
			iDistance = b*b   + iDeltaSmall ;
			iStep = Math.abs(b) ;
			while (iStep-- > 0)
			{
				setPixel(iCurrX,iCurrY);
				iCurrX += iDirectionX ;
				if(iDistance < 0)
				{
					iCurrY    += iDirectionY ;
				    iDistance += iDeltaBig   ;
				}
				else
				{
					iDistance += iDeltaSmall ;
				}
			}
		}
		else
		{
			iDeltaBig = 2*a*a + iDeltaSmall ;
			iDistance = a*a   + iDeltaSmall ;
			iStep = Math.abs(a) ;
			while (iStep-- > 0) 
			{
				setPixel(iCurrX,iCurrY);
				iCurrY += iDirectionY ;
				if(iDistance < 0)
				{
					iCurrX    += iDirectionX ;
					iDistance += iDeltaBig   ;
				}
				else
				{
					iDistance += iDeltaSmall  ;
				}
			}
		} 
		setPixel(iCurrX,iCurrY);
	}
}
	