package Assignment1;

import java.awt.*;
import java.awt.event.*;

public class DashedLines extends Frame{
	public static void main(String[] args) {new DashedLines();}
	DashedLines() {
		super("Drawing DashedLines");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		setSize(800,600);
		add("Center", new CvDashedLines());
		setVisible(true);
	}
}

class CvDashedLines extends Canvas {
	public void paint(Graphics g) {
		g.drawLine(100,100,100,500);
		g.drawLine(100,500,700,500);
		g.drawLine(700,500,700,100);
		g.drawLine(700,100,100,100);
		Lines.dashedLine(g, 200, 200, 200, 400, 20);
		Lines.dashedLine(g, 200, 400, 600, 400, 20);
		Lines.dashedLine(g, 600, 400, 600, 200, 20);
		Lines.dashedLine(g, 600, 200, 200, 200, 20);
		Lines.dashedLine(g, 100, 100, 200, 200, 20);
		Lines.dashedLine(g, 100, 500, 200, 400, 20);
		Lines.dashedLine(g, 700, 500, 600, 400, 20);
		Lines.dashedLine(g, 700, 100, 600, 200, 20);
	}
}

class Lines {
	public static void dashedLine(Graphics g, int xA, int yA, int xB, int yB, int dashLength) {
		int len = (int) Math.sqrt((xA - xB)*(xA - xB) + (yA - yB)*(yA - yB));
		if (len <= 2*dashLength) {
			g.drawLine(xA, yA, xB, yB);
		}
		else {
			
			// when we have n dashes & (n-1) gaps, the dashed line will start and end with a dash & not a space.
			int n = (len / dashLength)/2;
			
			// Projection of dash on x and y axis to determine the end point of the dash
			int xdash = ((xB - xA) / n)/2;
			int ydash = ((yB - yA) / n)/2;
			
			// Projection of gap on x and y axis to determine the end point of the gap
			int xgap = ((xB - xA) - n*xdash) / (n - 1);
			int ygap = ((yB - yA) - n*ydash) / (n - 1);
			
			System.out.println("Length : "+len);
			System.out.println("Number of segments : "+n);
			
			// Printing each dash & setting staring point for next dash
			for (int i = 0; i < n ; i++) {
				g.drawLine(xA, yA, (xA + xdash), (yA + ydash));
				xA = xA + xgap + xdash;
				yA = yA + ygap + ydash;
			}
		}
	}
}