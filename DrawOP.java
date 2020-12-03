import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.math.*;

public class DrawOP{
	PanelEX p;
	int DelayTime;
	boolean Delay;
	Color color;
	Color t;
	BufferedImage bi;
	Graphics g;
	boolean stop;
	Graphics2D g2;
	int mask=0;
	int RGB;
	public static final int PUSHBACK=1;
	public static final int XOR=2;
	public static final int ONSCREEN=4;
	public static final int BOTH=8;
	
	DrawOP(PanelEX p){
		this.p=p;
		stop=false;
		DelayTime=0;
		color=Color.black;
	}
	public void initGraphics(){
		g=p.getGraphics();
		bi=p.bi;
		g2=(Graphics2D)bi.getGraphics();
		g.setColor(color);
		g2.setColor(color);
	}
	public int getPixel(int x,int y){
		//System.out.println(Integer.toBinaryString(bi.getRGB(x,y)));
		return bi.getRGB(x,y);
	}
	public void setMask(int m){
		mask=m;
	}
	public void setDelay(){
		Delay=true;
	}
	public void clrDelay(){
		Delay=false;
	}
	public void setColor(Color c){	//AreaFillOP must overwrite this function
		color=c;
	}
	public void setDelayTime(int t){
		DelayTime=t;
	}
	public void stop(){
		stop=true;
	}
	public void start(){
		stop=false;
	}
	void setPixel(int x,int y){
		if(x>=p.width || x<=0 || y>=p.height || y<=0) return;
		if(mask==ONSCREEN){
			g.drawLine(x,y,x,y);
		}
		else if(mask==PUSHBACK){
			RGB=bi.getRGB(x,y);
			t=new Color(RGB);
			g.setColor(t);
			g.drawLine(x,y,x,y);
		}
		else if(mask==XOR){	//for XOR mode
			int ti;
			RGB=bi.getRGB(x,y);
			ti=color.getRGB();
			t=new Color(ti^RGB);
			g.setColor(t);
			g.drawLine(x,y,x,y);
			g2.setColor(t);
			g2.drawLine(x,y,x,y);
		}
		else{
			//BOTH ON SCREEN AND BI
			g.drawLine(x,y,x,y);
			g2.drawLine(x,y,x,y);
		}
		if(Delay==false) return;
		try{	Thread.sleep(DelayTime);
		}catch(InterruptedException e){}
	}
}