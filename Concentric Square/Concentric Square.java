package Assignment1;

import java.awt.*;
import java.awt.event.*;
public class Square extends Frame{
	public static void main(String[] args) {new Square();}
	Square() {
		super("Square: 30 squares inside each other");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		setSize(600,400);
		add("Center", new CvSquare());
		setVisible(true);
	}
}

class CvSquare extends Canvas {
	int maxX, maxY, minMaxXY, xCenter, yCenter;
	void initgr() {
		Dimension d = getSize();
		maxX = d.width - 1; maxY = d.height -1;
		minMaxXY = Math.min(maxX, maxY);
		xCenter = maxX / 2; yCenter = maxY / 2;
	}
	
	int iX(float x) {return Math.round(x);}
	int iY(float y) {return maxY - Math.round(y);}
	
	public void paint(Graphics g) {
		initgr();
		float side = 0.95F * minMaxXY, sideHalf = 0.5F * side,
				xA, yA, xB, yB, xC, yC, xD, yD, xA1, yA1, xB1, yB1, xC1, yC1, xD1, yD1, p, q;
				q = 0.5F; p = 1 - q;
				
		// xA, xB, xC, xD are the corner points of the outer square		
		xA = xCenter - sideHalf; yA = yCenter - sideHalf;
		xB = xCenter + sideHalf; yB = yCenter - sideHalf;
		xC = xCenter + sideHalf; yC = yCenter + sideHalf;
		xD = xCenter - sideHalf; yD = yCenter + sideHalf;
		
		// every iteration prints a square within a square, except for the base case
		for (int i = 0; i < 30; i++) {
			
			//Printing square
			g.drawLine(iX(xA), iY(yA), iX(xB), iY(yB));
			g.drawLine(iX(xB), iY(yB), iX(xC), iY(yC));
			g.drawLine(iX(xC), iY(yC), iX(xD), iY(yD));
			g.drawLine(iX(xD), iY(yD), iX(xA), iY(yA));
			
			// Updating edges to draw the next square rotated 45 degrees compared to its outer square
			xA1 = p * xA + q * xB; yA1 = p * yA + q * yB;
			xB1 = p * xB + q * xC; yB1 = p * yB + q * yC;
			xC1 = p * xC + q * xD; yC1 = p * yC + q * yD;
			xD1 = p * xD + q * xA; yD1 = p * yD + q * yA;
			xA = xA1; xB = xB1; xC = xC1; xD = xD1;
			yA = yA1; yB = yB1; yC = yC1; yD = yD1;
		}
	}
}
