import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.math.*;

public class PanelEX extends JPanel implements MouseMotionListener,MouseListener{
	
	PanelEX(MainFrame mf,int width,int height){
		this.mf=mf;
		xmin=ymin=0;
		xmax=width;
		ymax=height;
		resetSize(width,height);
		color=Color.white;
		
		circle=new CircleOP(this);
		clip=new ClipOP(this);
		clip.setClipRect(0,0,width,height);
		clip.setClipAlgo(Sutherland);
		line=new LineOP(this,clip);
		areafill=new AreaFillOP(this,line);
		areafill.setHeight(height);
		setColor(color);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void mousePressed(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON1){
            	xstart=xcurrent=e.getX();
            	ystart=ycurrent=e.getY();
            if(currentOperate==AreaFill){
            	needConnectPoint=true;
            	int t=areafill.add(e.getX(),e.getY());
            	if(t==AreaFillOP.STACKFULL){
            		if(FillAlgo==Edge||FillAlgo==Scanbeam){
            			areafill.drawLineEXNotOnBuffer();
            			areafill.drawLineFromLastToFirstNotOnBuffer();
            		}
            		else{
            			areafill.drawLineEX();
            			areafill.drawLineFromLastToFirst();
            		}
            		if(FillAlgo!=Seed){
            			switch(FillAlgo){
            				case Scanbeam:
            					areafill.ScanbeamFill();
            					break;
            				case Edge:
            					areafill.EdgeFill();
            					break;
            			}
            			areafill.emptyStack();
            		}
            		needConnectPoint=false;
            	}
            	else if(t==AreaFillOP.STACKFULLWITHSEED){
            		areafill.SeedFill();
            		areafill.emptyStack();
            		needConnectPoint=false;
            	}
            	else{
            		if(FillAlgo==Edge || FillAlgo==Scanbeam)	areafill.drawLineEXNotOnBuffer();
            		else	areafill.drawLineEX();
            	}
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
		if(currentOperate==AreaFill) return;
		switch(currentOperate){
			case AreaFill:
				return;
			case DrawLine:
				line.pushBack(xstart,ystart,xcurrent,ycurrent);
				xcurrent=e.getX();
				ycurrent=e.getY();
				line.pushForward(xstart,ystart,xcurrent,ycurrent);
				break;
			case DrawCircle:
				int radix=(int)Math.sqrt((xstart-xcurrent)*(xstart-xcurrent)+(ystart-ycurrent)*(ystart-ycurrent));
				circle.pushBack(xstart,ystart,radix);
				xcurrent=e.getX();
				ycurrent=e.getY();
				radix=(int)Math.sqrt((xstart-xcurrent)*(xstart-xcurrent)+(ystart-ycurrent)*(ystart-ycurrent));
				circle.pushForward(xstart,ystart,radix);
				break;
			case SegmentClip:
				clip.pushBack(xstart,ystart,xcurrent,ycurrent);
				xcurrent=e.getX();
				ycurrent=e.getY();
				clip.pushForward(xstart,ystart,xcurrent,ycurrent);
				break;
		}
    }
    public void mouseReleased(MouseEvent e) {
    	if(e.getButton()!=MouseEvent.BUTTON1)	return;
        if(currentOperate==AreaFill)	return;
        if(currentOperate==DrawLine){
        	line.pushBack(xstart,ystart,xcurrent,ycurrent);
        	switch(LineAlgo){
        		case Breasham:
        			line.Breasham(xstart,ystart,e.getX(),e.getY());
        			break;
        		case DDA:
        			line.DDA(xstart,ystart,e.getX(),e.getY());
        			break;
        		case MidPoint:
        			line.MidPoint(xstart,ystart,e.getX(),e.getY());
        			break;
        	}
        }
        if(currentOperate==DrawCircle){
        	int radix=(int)Math.sqrt( (xcurrent-xstart)*(xcurrent-xstart) + (ycurrent-ystart)*(ycurrent-ystart));
        	circle.pushBack(xstart,ystart,radix);
        	radix=(int)Math.sqrt( (e.getX()-xstart)*(e.getX()-xstart) + (e.getY()-ystart)*(e.getY()-ystart));
        	//System.out.println("ok");
        	switch(CircleAlgo){
        		case Breasham:
        			circle.Breasham(xstart,ystart,radix);
        			break;
        		case DDA:
        			circle.DDA(xstart,ystart,radix);
        			break;
        		case MidPoint:
        			circle.MidPoint(xstart,ystart,radix);
        			break;
        	}
        }
        if(currentOperate==SegmentClip){
        	clip.pushBack(xstart,ystart,xcurrent,ycurrent);
        	if(xstart>e.getX()){
        		xmin=e.getX();	xmax=xstart;
        	}
        	else{
        		xmin=xstart;	xmax=e.getX();
        	}
        	if(ystart>e.getY()){
        		ymin=e.getY();	ymax=ystart;
        	}
        	else{
        		ymin=ystart;	ymax=e.getY();
        	}
        	if(xmin<0) xmin=0;
        	if(xmax>width) xmax=width;
        	if(ymin<0) ymin=0;
        	if(ymax>height) ymax=height;
        	clip.drawRectBoth(xmin,ymin,xmax,ymax);
        	clip.setClipRect(xmin,ymin,xmax,ymax);
        }
    }
    public void mouseMoved(MouseEvent e){
    	if( needConnectPoint && (currentOperate==AreaFill) ){
    		line.pushBack(xstart,ystart,xcurrent,ycurrent);
    		xcurrent=e.getX();
    		ycurrent=e.getY();
    		line.pushForward(xstart,ystart,xcurrent,ycurrent);
    	}
    }
	void resetSize(int width,int height){
		if(this.width==width && this.height==height) return;
		this.width=width;
		this.height=height;
		bi=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		g2=(Graphics2D)bi.getGraphics();
		g2.setColor(Color.black);
		g2.fillRect(0,0,width,height);
		g2.dispose();
		setSize(width,height);
	}
	public void clear(){
		g2=(Graphics2D)bi.getGraphics();
		g2.setColor(Color.black);
		g2.fillRect(0,0,width,height);
		g2.dispose();
		paint(getGraphics());
	}
	public void paint(Graphics g){
		//System.out.println("ok "+xmin+" "+ymin+" "+xmax+" "+ymax);
		g.drawImage(bi,0,0,width,height,this);
		//System.out.println("paint");
	}
	public void update(Graphics g){
		paint(g);
	}	
	public void stop(){
	}
	public void setLineAlgo(int algo){
		LineAlgo=algo;
	}
	public void setCircleAlgo(int algo){
		CircleAlgo=algo;
	}
	public void setFillAlgo(int algo){
		FillAlgo=algo;
	}
	public void setClipAlgo(int algo){
		ClipAlgo=algo;
		clip.setClipAlgo(algo);
	}
	public void setEdgeNum(int n){
		EdgeNum=n;
		areafill.setEdgeNum(n);
		areafill.emptyStack();
		needConnectPoint=false;
	}
	public void setDelayTime(int time){
		DelayTime=time;
		line.setDelayTime(time);
		circle.setDelayTime(time);
		areafill.setDelayTime(time);
		clip.setDelayTime(time);
		areafill.emptyStack();
		needConnectPoint=false;
	}
	public void setColor(Color c){
		color=c;
		line.setColor(c);
		circle.setColor(c);
		areafill.setColor(c);
		clip.setColor(c);
		areafill.emptyStack();
		needConnectPoint=false;
	}
	public void setOperate(int op){
		currentOperate=op;
		needConnectPoint=false;
		areafill.emptyStack();
		//System.out.println(op);
	}
	public Color getColor(){
		return color;
	}
	   
	public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

	MainFrame mf;
	BufferedImage bi;
	LineOP line;
	CircleOP circle;
	AreaFillOP areafill;
	ClipOP clip;
	Graphics2D g2;
	
	int xstart;
	int ystart;
	int xcurrent;
	int ycurrent;
	int xend;
	int yend;
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	int width=0;
	int height=0;
	int currentOperate=DrawLine;
	int LineAlgo=Breasham;
	int CircleAlgo=Breasham;
	int FillAlgo=Scanbeam;
	int ClipAlgo=Sutherland;
	int EdgeNum=3;
	int DelayTime=0;
	Color color;
	boolean needConnectPoint=false;
	
	public static final int Breasham	=1;
	public static final int DDA			=2;
	public static final int MidPoint	=3;
	public static final int Scanbeam	=4;
	public static final int Seed		=5;
	public static final int Edge		=6;
	public static final int Sutherland	=7;
	public static final int Liang_Basky =8;
	
	public static final int DrawLine	=1;
	public static final int DrawCircle	=2;
	public static final int AreaFill	=3;
	public static final int SegmentClip	=4;
}