import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.math.*;

public class ClipOP extends DrawOP{
	int xmin,ymin,xmax,ymax;
	int clipAlgo;
	private final int xminmask=8;
	private final int xmaxmask=2;
	private final int yminmask=4;
	private final int ymaxmask=1;
	public static final int MidPoint    =3;
	public static final int Sutherland	=7;
	public static final int Liang_Basky =8;
	ClipOP(PanelEX p){
		super(p);
	}

	public LineEX clip(int x1,int y1,int x2,int y2){
		switch(clipAlgo){
			case Sutherland:
				//Sy1tem.out.println("Sutherland");
				return clipSutherland(x1,y1,x2,y2);
			case Liang_Basky:
				return clipLiangBasky(x1,y1,x2,y2);
			case MidPoint:
				return clipMidPoint(x1,y1,x2,y2);
		}
		return null;
	}
	private int getCode(int x,int y){
		//code xmin ymin xmax ymax
		//Sy1tem.out.println("m "+xmin+" "+ymin+" "+xmax+" "+ymax);
		//Sy1tem.out.println(" x "+x+" "+y);
		int code=0;
		if(x>xmax)	code|=xmaxmask;
		else if(x<xmin)	code|=xminmask;
		if(y>ymax) code|=ymaxmask;
		else if(y<ymin) code|=yminmask;
		//Sy1tem.out.println(Integer.toBinary1tring(code));
		return code;
	}
	private LineEX clipSutherland(int x1,int y1,int x2,int y2){
		int code1,code2;
		double t;
		int t1,t2;
		code1=getCode(x1,y1);
		code2=getCode(x2,y2);
		
		if((code1|code2)==0) return new LineEX(x1,y1,x2,y2);	//needn't clip
		if((code1&code2)!=0) return null;	//clipped completely
		if((x2-x1)==0){
			if(y1>y2){
				y1=y1>ymax?ymax:y1;
				y2=y2<ymin?ymin:y2;
			}
			else{
				y1=y1<ymin?ymin:y1;
				y2=y2>ymax?ymax:y2;
			}
			return new LineEX(x1,y1,x2,y2);
		}
		if((y2-y1)==0){
			if(x1>x2){
				x1=x1>xmax?xmax:x1;
				x2=x2<xmin?xmin:x2;
			}
			else{
				x1=x1<xmin?xmin:x1;
				x2=x2>xmax?xmax:x2;
			}
			return new LineEX(x1,y1,x2,y2);
		}
		while(true){
			if((code1|code2)==0) return new LineEX(x1,y1,x2,y2);	//needn't clip
			if((code1&code2)!=0) return null;	//clipped completely
			if(code1!=0){
				if((code1&xminmask) != 0){
					t=(double)(xmin-x1)/(x2-x1);
					x1=xmin;
					y1=(int)(y1+t*(y2-y1));
					code1=getCode(x1,y1);
					continue;
				}
				if((code1&xmaxmask) != 0){
					t=(double)(xmax-x1)/(x2-x1);
					x1=xmax;
					y1=(int)(y1+t*(y2-y1));
					code1=getCode(x1,y1);
					continue;
				}
				if((code1&yminmask) != 0){
					t=(double)(ymin-y1)/(y2-y1);
					x1=(int)(x1+t*(x2-x1));
					y1=ymin;
					code1=getCode(x1,y1);
					continue;
				}
				if((code1&ymaxmask) != 0){
					t=(double)(ymax-y1)/(y2-y1);
					x1=(int)(x1+t*(x2-x1));
					y1=ymax;
					code1=getCode(x1,y1);
				}
			}
			if(code2!=0){
				if((code2&xminmask) != 0){
					t=(double)(xmin-x1)/(x2-x1);
					x2=xmin;
					y2=(int)(y1+t*(y2-y1));
					code2=getCode(x2,y2);
					continue;
				}
				if((code2&xmaxmask) != 0){
					t=(double)(xmax-x1)/(x2-x1);
					x2=xmax;
					y2=(int)(y1+t*(y2-y1));
					code2=getCode(x2,y2);
					continue;
				}
				if((code2&yminmask) != 0){
					t=(double)(ymin-y1)/(y2-y1);
					x2=(int)(x1+t*(x2-x1));
					y2=ymin;
					code2=getCode(x2,y2);
					continue;
				}
				if((code2&ymaxmask) != 0){
					t=(double)(ymax-y1)/(y2-y1);
					x2=(int)(x1+t*(x2-x1));
					y2=ymax;
					code2=getCode(x2,y2);
				}
			}
		}
	}
	
	private LineEX clipLiangBasky(int x1,int y1,int x2,int y2){
		double k;
		int L,R,t;
		double xt,xu,yt,yu;
		double x1t,x2t,y1t,y2t;
		boolean exchangedX=false;
		if(x1>x2){
			t=x1;	x1=x2;	x2=t;
			t=y1;	y1=y2;	y2=t;
			exchangedX=true;
		}
		if(x1==x2){	//下两个if为特殊情况的处理
			if((x1>xmax)||(x1<xmin)) return null;
			if(y1>y2){
				if((y2>ymax)||(y1<ymin)) return null;
				y1=y1>ymax?ymax:y1;
				y2=y2<ymin?ymin:y2;
			}
			else{
				if((y1>ymax)||(y2<ymin)) return null;
				y1=y1<ymin?ymin:y1;
				y2=y2>ymax?ymax:y2;
			}
			return new LineEX(x1,y1,x2,y2);
		}
		if(y1==y2){
			if((y1>ymax)||(y1<ymin)) return null;
			if((x1>xmax)||(x2<xmin)) return null;
			x1=x1>xmax?xmax:x1;
			x2=x2<xmin?xmin:x2;
			return new LineEX(x1,y1,x2,y2);
		}

		k=(double)(y2-y1)/(x2-x1);
		if((k<=1.0)&&(k>=-1.0)){
			L=xmin>x1?xmin:x1;
			R=xmax<x2?xmax:x2;
			if(L>R)	return null;
			xt=(ymin-y1)/k+x1;
			xu=(ymax-y1)/k+x1;
			if(k>0){
				if((L>xu)||(xt>R)) return null;
				x1t=L>xt?L:xt;
				x2t=R<xu?R:xu;
			}
			else{
				if((L>xt)||(xu>R)) return null;
				x1t=L>xu?L:xu;
				x2t=R<xt?R:xt;
			}
			y1t=k*(x1t-x1)+y1;
			y2t=k*(x2t-x1)+y1;
			if(exchangedX) return new LineEX((int)x2t,(int)y2t,(int)x1t,(int)y1t);
			return new LineEX((int)x1t,(int)y1t,(int)x2t,(int)y2t);
		}
		else{
			yt=(xmax-x1)*k+y1;
			yu=(xmin-x1)*k+y1;
			if(k<-1.0){
				L=ymax>y1?y1:ymax;
				R=ymin>y2?ymin:y2;
				if((R>L)||(L<yt)||(R>yu)){
					//System.out.println("L>R");
					return null;
				}
				y1t=L>yu?yu:L;
				y2t=R>yt?R:yt;
			}
			else{
				L=ymin>y1?ymin:y1;
				R=ymax>y2?y2:ymax;
				if((R<L)||(L>yt)||(R<yu)){
					//System.out.println("L>xx");
					return null;
				}
				y1t=L>yu?L:yu;
				y2t=R>yt?yt:R;
			}
			x1t=(y1t-y1)/k+x1;
			x2t=(y2t-y1)/k+x1;
			if(exchangedX) return new LineEX((int)x2t,(int)y2t,(int)x1t,(int)y1t);
			return new LineEX((int)x1t,(int)y1t,(int)x2t,(int)y2t);
		}
	}
	
	private LineEX clipPart(int x1,int y1,int x2,int y2){
		int xm,ym,codem;
		int xt=x1,yt=y1;
		xm=(x1+x2)>>1;
		ym=(y1+y2)>>1;
		codem=getCode(xm,ym);
		while(true){
			if(codem!=0){
				x2=xm;
				y2=ym;
				xm=(x1+x2)>>1;
				ym=(y1+y2)>>1;
				codem=getCode(xm,ym);
				if((xm==x2)&&(ym==y2)) return new LineEX(xt,yt,x1,y1);
			}
			else{
				x1=xm;
				y1=ym;
				xm=(x1+x2)>>1;
				ym=(y1+y2)>>1;	//code1 must be 0,yin han
				codem=getCode(xm,ym);
				if(xm==x1 && (ym==y1)) return new LineEX(xt,yt,x1,y1);
			}
		}
	}
	private LineEX clipMidPoint(int x1,int y1,int x2,int y2){
		int xm,ym;
		int code1,code2,codem;
		LineEX line1,line2;
		xm=(x1+x2)>>1;
		ym=(y1+y2)>>1;
		code1=getCode(x1,y1);
		code2=getCode(x2,y2);
		codem=getCode(xm,ym);
		if((code1|code2)==0) return new LineEX(x1,y1,x2,y2);
		if((code1&code2)!=0) return null;
		if(code1==0)	//code1=0,code2!=0
			return clipPart(x1,y1,x2,y2);
		if(code2==0)	//code1!=0,code2=0
			return clipPart(x2,y2,x1,y1);
		//System.out.println("ok");
		while(true){
			//System.out.println(x1+" "+y1+" "+x2+" "+y2+" "+xm+" "+ym);
			if((code1&code2)!=0) return null;
			if((code1|code2)==0) return new LineEX(x1,y1,x2,y2);
			if(codem!=0 && (xm==x1) && (ym==y1)) return null;
			if(codem==0){
				line1=clipPart(xm,ym,x1,y1);
				line2=clipPart(xm,ym,x2,y2);
				return new LineEX(line1.x2,line1.y2,line2.x2,line2.y2);
			}
			if((codem&code1)!=0){
				x1=xm;
				y1=ym;
				code1=codem;
			}
			if((codem&code2)!=0){
				x2=xm;
				y2=ym;
				code2=codem;
			}
			xm=(x1+x2)>>1;
			ym=(y1+y2)>>1;
			codem=getCode(xm,ym);
		}
	}
	public void setClipAlgo(int algo){
		clipAlgo=algo;
	}
	public void setClipRect(int xmin,int ymin,int xmax,int ymax){
		//Sy1tem.out.println(xmin+" "+ymin+" "+xmax+" "+ymax);
		this.xmin=xmin;
		this.ymin=ymin;
		this.xmax=xmax;
		this.ymax=ymax;
	}
	public void pushBack(int x1,int y1,int x2,int y2){
		clrDelay();
		setMask(LineOP.PUSHBACK);
		initGraphics();
		int t;
		if(x1>x2){t=x1;x1=x2;x2=t;}
		if(y1>y2){t=y1;y1=y2;y2=t;}
		drawRectEX(x1,y1,x2,y2);
	}
	public void pushForward(int x1,int y1,int x2,int y2){
		clrDelay();
		setMask(LineOP.ONSCREEN);
		initGraphics();
		int t;
		if(x1>x2){t=x1;x1=x2;x2=t;}
		if(y1>y2){t=y1;y1=y2;y2=t;}
		drawRectEX(x1,y1,x2,y2);
	}
	public void drawRectBoth(int x1,int y1,int x2,int y2){
		clrDelay();
		setMask(LineOP.BOTH);
		initGraphics();
		g.setColor(Color.BLUE);
		g2.setColor(Color.BLUE);
		int t;
		if(x1>x2){t=x1;x1=x2;x2=t;}
		if(y1>y2){t=y1;y1=y2;y2=t;}
		drawRectEX(x1,y1,x2,y2);
	}
	public void drawRectEX(int x1,int y1,int x2,int y2){
		int i;
		for(i=y1;i<=y2;i++){
			setPixel(x1,i);
			setPixel(x2,i);
		}
		for(i=x1;i<=x2;i++){
			setPixel(i,y1);
			setPixel(i,y2);
		}
	}
}