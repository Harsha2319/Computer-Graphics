package Assignment5_q1;

import java.awt.*;
import java.awt.event.*;

public class KochCurve extends Frame {
	public static void main(String[] args) {
		new KochCurve();
	}
	KochCurve() {
		super("Click left or right mouse button to change the level");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		setSize(1200, 1000);
		add("Center", new CvFractalGrammars());
		setVisible(true);
	}
}

class CvFractalGrammars extends Canvas {
	String fileName, axiom, strF, strf, strX, strY;
	int maxX, maxY, level = 2;
	double xLast, yLast, dir, rotation, dirStart, fxStart, fyStart,
	lengthFract, reductFact;
	
	void error(String str) {
		System.out.println(str);
		System.exit(1);
	}

	CvFractalGrammars() {
		axiom = "F-F-F-F";
		strF = "FF-F-F-F-F-F+F ";
		strf = "";
		strX = "";
		strY = "";
		rotation = 45;
		dirStart = 0;
		fxStart = 0.5;
		fyStart = 0.1;
		lengthFract = 0.02;
		reductFact = 1;
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
					level--; // Right mouse button decreases level
					if (level < 1) level = 1;
				} else
					level++; // Left mouse button increases level
				repaint();
			}
		});
	}
	
	Graphics g;
	int iX(double x) {return (int) Math.round(x);}
	int iY(double y) {return (int) Math.round(maxY - y);}
	
	void drawTo(Graphics g, double x, double y) {
		g.drawLine(iX(xLast), iY(yLast), iX(x), iY(y));
		xLast = x; yLast = y;
	}
	
	void moveTo(Graphics g, double x, double y) {
		xLast = x; yLast = y;
	}
	
	public void paint(Graphics g) {
		Dimension d = getSize();
		maxX = d.width - 1; maxY = d.height - 1;
		xLast = fxStart * maxX; yLast = fyStart * maxY;
		dir = dirStart; // Initial direction in degrees
		turtleGraphics(g, axiom, level, lengthFract * maxY);
	}
	
	public void turtleGraphics(Graphics g, String instruction, int depth, double len) {
		double xMark = 0, yMark = 0, dirMark = 0;
		for (int i = 0; i < instruction.length(); i++) {
			double rad = Math.PI / 180 * dir, // Degrees -> radians
					dx = len * Math.cos(rad), dy = len * Math.sin(rad);
			char ch = instruction.charAt(i);
			switch (ch) {
			case 'F': // Step forward and draw
				// Start: (xLast, yLast), direction: dir, steplength: len
				if (depth == 0) {
					drawTo(g, xLast + 2*dx/3, yLast + 2*dy/3);
				} else
					turtleGraphics(g, strF, depth - 1, reductFact * len);
				break;
			case 'f': // Step forward without drawing
				// Start: (xLast, yLast), direction: dir, steplength: len
				if (depth == 0) {
					moveTo(g, xLast + 2*dx/3, yLast + 2*dy/3);
				} else
					turtleGraphics(g, strf, depth - 1, reductFact * len);
				break;
			case 'X':
				if (depth > 0)
					turtleGraphics(g, strX, depth - 1, reductFact * len);
				break;
			case 'Y':
				if (depth > 0)
					turtleGraphics(g, strY, depth - 1, reductFact * len);
				break;
			case '+': // Turn right
				dir -= rotation;
				rad = Math.PI / 180 * dir; // Degrees -> radians
				dx = len * Math.cos(rad); dy = len * Math.sin(rad);
				drawTo(g, xLast + 1*dx/3, yLast + 1*dy/3);
				dir -= rotation;
				break;
			case '-': // Turn left
				dir += rotation;
				rad = Math.PI / 180 * dir; // Degrees -> radians
				dx = len * Math.cos(rad); dy = len * Math.sin(rad);
				drawTo(g, xLast + 1*dx/3, yLast + 1*dy/3);
				dir += rotation;
				break;
			case '[': // Save position and direction
				xMark = xLast; yMark = yLast;
				dirMark = dir;
				break;
			case ']': // Back to saved position and direction
				xLast = xMark; yLast = yMark;
				dir = dirMark;
				break;
			}
		}
	}
}