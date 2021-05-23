package Assignment1;

import java.awt.*;
import java.awt.event.*;

class HexagonGrid extends Frame{
	int margin;
	public static void main(String[] args) {new HexagonGrid(20);}
	HexagonGrid(int margin) {
		super("HexagonGrid");
		this.margin = margin;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		setSize(800,600);
		add("Center", new CvHexagonGrid(margin));
		setVisible(true);
	}
}

class CvHexagonGrid extends Canvas {
	int margin, mouseX, mouseY;
	int vert0X, vert1X, vert2X, vert3X, vert4X, vert5X, vert0Y, vert1Y, vert2Y, vert3Y, vert4Y, vert5Y;
	int temp_vert0Y, temp_vert1Y, temp_vert2Y, temp_vert3Y, temp_vert4Y, temp_vert5Y;
	CvHexagonGrid(int margin) {
		this.margin = margin;
		
		// get radius input from mouse input
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
				mouseX = me.getX();
				mouseY = me.getY();
				repaint();		
			}
		});
	}
	
	public void paint(Graphics g){
		int radius = (int)Math.sqrt((mouseX-margin)*(mouseX-margin) + (mouseY-margin)*(mouseY-margin));
		int height = (int)(Math.round((Math.sqrt(3))*radius));
		int hex_H = NumofHex_H(radius);
		int hex_V = NumofHex_V(height);
		System.out.println("Radius : "+radius);
		System.out.println("Height : "+height);
		System.out.println("No of Hex in a row : "+hex_H);
		System.out.println("No of Hex in a col : "+hex_V);
		FirstHex(radius, height);
		
		// each iteration prints a column of hexagon
		for (int j = 0; j < hex_H; j++){
			reset_Y();

			// each iteration prints a hexagon
			for (int i = 0; i < hex_V; i++){
	
				//Top left line
				g.drawLine(vert0X, temp_vert0Y, vert5X, temp_vert5Y);

				//Bottom left line
				g.drawLine(vert5X, temp_vert5Y, vert4X, temp_vert4Y);

				//top line
				g.drawLine(vert0X, temp_vert0Y, vert1X, temp_vert1Y);
				
				//top right line
				g.drawLine(vert1X, temp_vert1Y, vert2X, temp_vert2Y);
				
				//bottom right line
				g.drawLine(vert2X, temp_vert2Y, vert3X, temp_vert3Y);

				//top for next column, will not draw if j is the last column
				if (j != hex_H - 1){ 
					g.drawLine(vert2X, temp_vert2Y, vert2X + radius, temp_vert2Y);
				}

				// Increment y values to print the next hexagon below the current hexagon
				temp_vert5Y += height;
				temp_vert0Y += height;
				temp_vert4Y += height;
				temp_vert1Y += height;
				temp_vert3Y += height;
				temp_vert2Y += height;
			}
			
			g.drawLine(vert0X, temp_vert0Y, vert1X, temp_vert1Y);

			replicateHorizontally(radius);

		}
	}
	
	// Function setting vertices of the first hexagon with reference to the mouse pointer
	public void FirstHex(int radius, int height){

		vert0X = (mouseX - radius / 2);
		vert0Y = (mouseY - height / 2);

		vert1X = (mouseX + radius / 2);
		vert1Y = (mouseY - height / 2);

		vert2X = (mouseX + radius);
		vert2Y = mouseY;

		vert3X = (mouseX + radius / 2);
		vert3Y = (mouseY + height / 2);

		vert4X = (mouseX - radius / 2);
		vert4Y = (mouseY + height / 2);

		vert5X = (mouseX - radius);
		vert5Y = mouseY;
	}
	
	// 2 columns of hexagons are considered to be a column, as it can be replicated to form the expected pattern
	// Horizontally length occupied by 2 columns is 3 * radius, so we increment by 3 * radius to start with the new column
	public void replicateHorizontally(int radius){

		vert5X += 3 * radius;
		vert0X += 3 * radius;
		vert4X += 3 * radius;
		vert1X += 3 * radius;
		vert3X += 3 * radius;
		vert2X += 3 * radius;

	}
	
	//Function used to reset y values similar to that of the first hexagon after printing each column of hexagons
	public void reset_Y(){

		temp_vert5Y = vert5Y;
		temp_vert0Y = vert0Y;
		temp_vert4Y = vert4Y;
		temp_vert1Y = vert1Y;
		temp_vert3Y = vert3Y;
		temp_vert2Y = vert2Y;
	}
	
	//Function to calculate number of hexagon can to stacked in a row 
	// 2 columns of hexagon are counted as 1 column, it takes ((7/2)*radius) length horizontally as radius/2 was common for 2 hexagons
	public int NumofHex_H(int radius) {
		Dimension d = getSize();
		int maxX = d.width - 1;
		return maxX / ((7/2)*radius);
	}
	
	//Function to calculate number of hexagon can to stacked in a column 
	public int NumofHex_V(int height) {
		Dimension d = getSize();
		int maxY = d.height - 1;
		return maxY / height;
	}
	
}