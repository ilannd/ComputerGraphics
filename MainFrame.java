import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class MainFrame extends JFrame{
	MainFrame(){
		Container c=getContentPane();
		
		drawPanel = new PanelEX(this,720,540);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		ctrlPanel = new JPanel();
		ctrlPanel.setLayout(new BoxLayout(ctrlPanel, BoxLayout.Y_AXIS));
		
		initButtonPanel();
		ctrlPanel.add(buttonPanel);
		
		initLineAlgoPanel();
		ctrlPanel.add(lineAlgoPanel);
		
		initCircleAlgoPanel();
		ctrlPanel.add(circleAlgoPanel);
		
		initFillAlgoPanel();
		ctrlPanel.add(fillAlgoPanel);
		
		initClipAlgoPanel();
		ctrlPanel.add(clipAlgoPanel);
		
		initEdgeNumPanel();
		ctrlPanel.add(edgeNumPanel);
		
		initDelayPanel();
		ctrlPanel.add(delayPanel);
		
		initColorPanel();
		ctrlPanel.add(colorPanel);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(10, 5, 5, 5);
        mainPanel.add(ctrlPanel, gridBagConstraints);

		copyright = new JLabel();
        copyright.setFont(new java.awt.Font("Verdana", 0, 10));
        copyright.setForeground(new java.awt.Color(255, 153, 0));
        copyright.setText("Graphics");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 15, 5);
        mainPanel.add(copyright,gridBagConstraints);
		
		add(mainPanel,BorderLayout.WEST);
		add(drawPanel,BorderLayout.CENTER);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Integrated platform for ComputerGraphics");
        setSize(885,572);
        //setResizable(false);
        show();
	}

	void initButtonPanel(){
		buttonPanel = new JPanel();
		buttonPanel.setBorder(new TitledBorder("Functional area"));
		
		JPanel t = new JPanel(new GridLayout(3,2,5,5));
		ButtonGroup bg=new ButtonGroup();
		
		line = new JToggleButton();
		line.setFont(new Font("Dialog", 0, 10));
		line.setSelected(true);
		line.setText("Draw lines");
		line.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPanel.setOperate(PanelEX.DrawLine);
			}
		});
		bg.add(line);
		t.add(line);
		
		circle = new JToggleButton();
		circle.setFont(new Font("Dialog", 0, 10));
		circle.setText("Circle painting");
		circle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPanel.setOperate(PanelEX.DrawCircle);
			}
		});
		bg.add(circle);
		t.add(circle);
		
		fill = new JToggleButton();
		fill.setFont(new Font("Dialog", 0, 10));
		fill.setText("Graphic fill");
		fill.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPanel.setOperate(PanelEX.AreaFill);
			}
		});
		bg.add(fill);
		t.add(fill);
		
		clip = new JToggleButton();
		clip.setFont(new Font("Dialog", 0, 10));
		clip.setText("Graphic clipping");
		clip.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPanel.setOperate(PanelEX.SegmentClip);
			}
		});
		bg.add(clip);
		t.add(clip);
		
		stop = new JButton();
		stop.setFont(new Font("Dialog", 0, 10));
		stop.setText("Stop");
		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPanel.stop();
			}
		});
		t.add(stop);
		
		clear = new JButton();
		clear.setFont(new Font("Dialog", 0, 10));
		clear.setText("Clear");
		clear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPanel.clear();
			}
		});
		t.add(clear);
		
		buttonPanel.add(t);
	}
	void initLineAlgoPanel(){
		lineAlgoPanel = new JPanel(new BorderLayout(5,10));
		lineAlgoPanel.setBorder(new TitledBorder("Line drawing algorithm"));
		
		JPanel t = new JPanel(new GridLayout());
		algoL = new JComboBox();
		algoL.setFont(new Font("Dialog", 0, 10));
		algoL.addItem("Breasham");
		algoL.addItem("DDA");
		algoL.addItem("Mid Point");
		algoL.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int i = algoL.getSelectedIndex();
				if(i==0) drawPanel.setLineAlgo(PanelEX.Breasham);
				if(i==1) drawPanel.setLineAlgo(PanelEX.DDA);
				if(i==3) drawPanel.setLineAlgo(PanelEX.MidPoint);
			}
		});
		t.add(algoL);
		lineAlgoPanel.add(t);
		drawPanel.setLineAlgo(PanelEX.Breasham);
	}
	void initCircleAlgoPanel(){
		circleAlgoPanel = new JPanel(new BorderLayout(5,10));
		circleAlgoPanel.setBorder(new TitledBorder("Circle drawing algorithm"));
		
		JPanel t = new JPanel(new GridLayout());
		algoC = new JComboBox();
		algoC.setFont(new Font("Dialog", 0, 10));
		algoC.addItem("Breasham");
		algoC.addItem("DDA");
		algoC.addItem("Mid Point");
		algoC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int i = algoC.getSelectedIndex();
				if(i==0) drawPanel.setCircleAlgo(PanelEX.Breasham);
				if(i==1) drawPanel.setCircleAlgo(PanelEX.DDA);
				if(i==3) drawPanel.setCircleAlgo(PanelEX.MidPoint);
			}
		});
		t.add(algoC);
		circleAlgoPanel.add(t);
		drawPanel.setCircleAlgo(PanelEX.Breasham);
	}
	void initFillAlgoPanel(){
		fillAlgoPanel = new JPanel(new BorderLayout(5,10));
		fillAlgoPanel.setBorder(new TitledBorder("Graph filling algorithm"));
		
		JPanel t = new JPanel(new GridLayout());
		algoF = new JComboBox();
		algoF.setFont(new Font("Dialog", 0, 10));
		algoF.addItem("ScanBeam");
		algoF.addItem("Seed");
		algoF.addItem("Edge XOR");
		algoF.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int i = algoF.getSelectedIndex();
				if(i==0) drawPanel.setFillAlgo(PanelEX.Scanbeam);
				if(i==1) drawPanel.setFillAlgo(PanelEX.Seed);
				if(i==2) drawPanel.setFillAlgo(PanelEX.Edge);
			}
		});
		t.add(algoF);
		fillAlgoPanel.add(t);
		drawPanel.setFillAlgo(PanelEX.Scanbeam);
	}
	void initClipAlgoPanel(){
		clipAlgoPanel = new JPanel(new BorderLayout(5,10));
		clipAlgoPanel.setBorder(new TitledBorder("Graph clipping algorithm"));
		
		JPanel t = new JPanel(new GridLayout());
		algoP = new JComboBox();
		algoP.setFont(new Font("Dialog", 0, 10));
		algoP.addItem("Sutherland");
		algoP.addItem("MidPoint");
		algoP.addItem("Liang-Basky");
		algoP.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int i = algoP.getSelectedIndex();
				if(i==0) drawPanel.setClipAlgo(PanelEX.Sutherland);
				if(i==1) drawPanel.setClipAlgo(PanelEX.MidPoint);
				if(i==2) drawPanel.setClipAlgo(PanelEX.Liang_Basky);
			}
		});
		t.add(algoP);
		clipAlgoPanel.add(t);
		drawPanel.setClipAlgo(PanelEX.Sutherland);
	}
	void initEdgeNumPanel(){
		edgeNumPanel = new JPanel(new BorderLayout(5,10));
		edgeNumPanel.setBorder(new TitledBorder("Vertex number of polygon"));
		
		JPanel t = new JPanel(new GridLayout());
		edgeNum=new JComboBox();
		edgeNum.setFont(new Font("Dialog", 0, 10));
		for(int i=3;i<15;i++){
			edgeNum.addItem(i+"");
		}
		edgeNum.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int i = edgeNum.getSelectedIndex();
				drawPanel.setEdgeNum(i+3);
			}
		});
		t.add(edgeNum);
		edgeNumPanel.add(t);
	}
	void initDelayPanel(){
		delayPanel=new JPanel(new BorderLayout(5,10));
		delayPanel.setBorder(new TitledBorder("Delay Time"));
		JPanel t = new JPanel(new GridLayout());
		delay = new JComboBox();
		delay.setFont(new Font("Dialog", 0, 10));
		for(int i=0;i<15;i++)	delay.addItem(i+"");
		delay.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                drawPanel.setDelayTime(delay.getSelectedIndex());
            }
        });
        t.add(delay);
		delayPanel.add(t);
	}
	
	void initColorPanel(){
		colorPanel=new JPanel(new BorderLayout(5,10));
		colorPanel.setBorder(new TitledBorder("Color Option"));
		JPanel t = new JPanel(new GridLayout());
		color=new JButton();
		color.setBackground(drawPanel.getColor());
		color.setPreferredSize(new Dimension(120,25));
		color.setBorder(new LineBorder(Color.black));
		color.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Color c = getColorFromDialog();
                if(c!=null){
                	drawPanel.setColor(c);
                	color.setBackground(c);
                }
            }
        });
        t.add(color);
        colorPanel.add(t);
	}
	Color getColorFromDialog(){
		return JColorChooser.showDialog(this,"Color options",drawPanel.getColor());
	}
	
	public static void main(String args[]){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
      		e.printStackTrace();
      	}
		new MainFrame();
	}
	
	JPanel mainPanel;
	JPanel ctrlPanel;
	JPanel buttonPanel;
	JPanel lineAlgoPanel;
	JPanel circleAlgoPanel;
	JPanel fillAlgoPanel;
	JPanel clipAlgoPanel;
	JPanel edgeNumPanel;
	JPanel delayPanel;
	JPanel colorPanel;
	
	JToggleButton line;
	JToggleButton circle;
	JToggleButton fill;
	JToggleButton clip;
	JButton stop;
	JButton clear;
	JButton color;
	
	JComboBox algoL;
	JComboBox algoC;
	JComboBox algoF;
	JComboBox algoP;
	JComboBox delay;
	JComboBox edgeNum;
	
	JLabel copyright;
	
	GridBagConstraints gridBagConstraints;
	PanelEX drawPanel;
}