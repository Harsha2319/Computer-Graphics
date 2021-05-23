package Assignment4_q1;

//CubRot2.java: Two rotating cubes with double buffering.
//Uses: Point2D (Section 1.4),
//Point3D, Rota3D (Section 3.9)
import java.awt.*;
import java.awt.event.*;

public class CubeRotate2 extends Frame {
	public static void main(String[] args) {new CubeRotate2();}

	CubeRotate2() {
		super("Two Rotating cubes - in perspective");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { System.exit(0); }
		});
		add("Center", new CvCubeRotate2());
		setSize(1400, 800);
		setVisible(true);
	}
}

class CvCubeRotate2 extends Canvas implements Runnable {
	int centerX, centerY, w, h;
	boolean flag = true;
	Cubes cube = new Cubes();
	Image image; // used for animation effect - double buffering 
	Graphics gImage;
	double alpha = 0;
	Thread thr = new Thread(this);
	
	// controls operation of the thread to create animation
	public void run() {
		try {
			for (;;) {
				alpha += 0.025;
				repaint();
				Thread.sleep(25);
			}
		} catch (InterruptedException e) {
		}
	}
	
	CvCubeRotate2() {thr.start();}
	
	// logical to device coordinate conversion
	int iX(float x) {return Math.round(centerX + x);}
	int iY(float y) {return Math.round(centerY - y);}
	
	// To draw each line in the cube wireframe
	void line(int i, int j) {
		Point2D P = cube.Screen[i], Q = cube.Screen[j];
		gImage.drawLine(iX(P.x), iY(P.y), iX(Q.x), iY(Q.y));
	}
	
	public void paint(Graphics g) {
		Dimension dim = getSize();
		w = dim.width; h = dim.height;
		int maxX = dim.width - 1, maxY = dim.height - 1;
		centerX = maxX / 2; centerY = maxY / 2;
		int minMaxXY = Math.min(maxX, maxY);
		cube.d = cube.rho * minMaxXY / cube.cubeSize;
		cube.updateWorldCoordinates(alpha);
		cube.perspectiveTransformation();
		
		// Creates an off-screen drawable image to be used for double buffering
		image = createImage(w, h);
		// Creates a graphics context for object
		gImage = image.getGraphics();
		
		// clear rectangle - so newly generated cube does not overlap with the old one
		gImage.clearRect(0, 0, w, h);
		// Horizontal lines - bottom
		line(0, 1); line(1, 2); line(2, 3); line(3, 0);
		// Horizontal lines - top
		line(4, 5); line(5, 6); line(6, 7); line(7, 4);
		// Vertical lines
		line(0, 4); line(1, 5); line(2, 6); line(3, 7);
		// Similarly for cube 2
		line(8, 9); line(9, 10); line(10, 11); line(11, 8);
		line(12, 13); line(13, 14); line(14, 15); line(15, 12);
		line(8, 12); line(9, 13); line(10, 14); line(11, 15);
		g.drawImage(image, 0, 0, null);
		
	}
	
	public void update(Graphics g) {paint(g);}
}

class Cubes { 
	float rho, theta = 0, phi = 1, d;
	Point3D[] World_old; // World coordinates - before rotation
	Point3D[] World_new; // World coordinates - After rotation
	Point2D[] Screen; // Screen coordinates
	float v11, v12, v13, v21, v22, v23, v32, v33, v43, cubeSize = 10;
	
	Cubes() {
		World_old = new Point3D[16];
		World_new = new Point3D[16]; 
		Screen = new Point2D[16];
		
		//origin is at the center of the window
		// vertices on the bottom face - cube1
		World_old[0] = new Point3D(0, -3, 0);
		World_old[1] = new Point3D(0, -1, 0);
		World_old[2] = new Point3D(2, -1, 0);
		World_old[3] = new Point3D(2, -3, 0);
		// vertices on the top face - cube1
		World_old[4] = new Point3D(0, -3, 2);
		World_old[5] = new Point3D(0, -1, 2);
		World_old[6] = new Point3D(2, -1, 2);
		World_old[7] = new Point3D(2, -3, 2);
		
		// vertices on the bottom face - cube2
		World_old[8] = new Point3D(0, 1, 0);
		World_old[9] = new Point3D(0, 3, 0);
		World_old[10] = new Point3D(2, 3, 0);
		World_old[11] = new Point3D(2, 1, 0);
		// vertices on the top face - cube2
		World_old[12] = new Point3D(0, 1, 2);
		World_old[13] = new Point3D(0, 3, 2);
		World_old[14] = new Point3D(2, 3, 2);
		World_old[15] = new Point3D(2, 1, 2);
		rho = 10; // For reasonable perspective effect
	}
	
	void updateWorldCoordinates(double alpha) {
		
		// compute new world coordinated for cube1 
		// rotate about edge 0-4 with angle alpha
		Rota3D.initRotate(World_old[0], World_old[4], alpha);
		for (int i = 0; i <= 7; i++)
			World_new[i] = Rota3D.rotate(World_old[i]);
		
		// compute new world coordinated for cube1 
		// rotate about edge 13-9 with angle 2 * alpha
		Rota3D.initRotate(World_old[13], World_old[9], 2 * alpha);
		for (int i = 8; i <= 15; i++)
			World_new[i] = Rota3D.rotate(World_old[i]);
	}
	
	void perspectiveTransformation() {
		
		// Computing view matrix
		float cosTheta = (float) Math.cos(theta), sinTheta = (float) Math.sin(theta), cosPhi = (float) Math.cos(phi), sinPhi = (float) Math.sin(phi);
		v11 = -sinTheta; v12 = -cosPhi * cosTheta; v13 = sinPhi * cosTheta;
		v21 = cosTheta; v22 = -cosPhi * sinTheta; v23 = sinPhi * sinTheta;
		v32 = sinPhi; v33 = cosPhi;
		v43 = -rho;
		
		// View transformation & perspective transformation for each vertex in both the cubes
		for (int i = 0; i <= 15; i++) {
			Point3D P = World_new[i];
			
			// point after View transformation
			Point3D Pe = new Point3D((v11 * P.x + v21 * P.y), (v12 * P.x + v22 * P.y + v32 * P.z), (v13 * P.x + v23 * P.y + v33 * P.z + v43));
			
			// point after perspective transformation
			Screen[i] = new Point2D(-d * Pe.x / Pe.z, -d * Pe.y / Pe.z);
		}
	}
}
