package Assignment5_q2;

import java.awt.*;
import java.awt.event.*;

public class tree extends Frame
{  public static void main(String[] args)
   {  
	new tree();
   }
   tree()
   {  super("Click left or right mouse button to change the level");
      addWindowListener(new WindowAdapter()
         {public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(800, 600);
      add("Center", new CvFractalGrammars());
      show();
   }  
   
   class CvFractalGrammars extends Canvas
   {  String fileName, axiom, strF, strf, strX, strY;
      int maxX, maxY, level = 5; 
      double xLast, yLast, dir, rotation, dirStart, fxStart, fyStart,
         lengthFract, reductFact;  
      double thicknessRatio = 1.5;
      CvFractalGrammars()
      { 
         axiom = "X";
         strF = "FF";
         strf = "";
         strX = "F[+X]F[-X]+X";
         strY = "";
         rotation = 20.0;
         dirStart = 90;
         fxStart = 0.5;
         fyStart = 0.05;
         lengthFract = 0.40;
         reductFact = 0.5;               
         addMouseListener(new MouseAdapter()
         {  public void mousePressed(MouseEvent evt)
            {  if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
               {  level--;      // Right mouse button decreases level
                  if (level < 1)
                     level = 1;
               }
               else            
                  level++;      // Left mouse button increases level
               repaint();
            }
         });
      }

      Graphics g;
      int iX(double x){return (int)Math.round(x);}
      int iY(double y){return (int)Math.round(maxY-y);}

      void drawTo(Graphics g, double x, double y)
      {  g.drawLine(iX(xLast), iY(yLast), iX(x) ,iY(y));         	  
         xLast = x;
         yLast = y;
      }

      // Drawing branches with relative thickness 
      void drawBranch(Graphics g, double x, double y, double thickness){ 	      
   	      double radxa = Math.PI/180 *(dir+90), 
   	    		  radxb = Math.PI/180 *(dir-90), 
   	    		  radxc = radxb,
   	    		  radxd= radxa;  // Degrees -> radians    
   	      
 	      double xa = xLast + thickness * Math.cos(radxa), ya = yLast + thickness * Math.sin(radxa), 
 	    		  xb = xLast + thickness * Math.cos(radxb), yb = yLast + thickness * Math.sin(radxb),
 	    		  xc = x + thickness/thicknessRatio * Math.cos(radxc), yc = y + thickness/thicknessRatio * Math.sin(radxc), 
 	    		  xd = x + thickness/thicknessRatio * Math.cos(radxd), yd = y + thickness/thicknessRatio * Math.sin(radxd);
 	      
   	      int [] xx = new int[]{iX(xa), iX(xb), iX(xc),iX(xd)};
       	  int [] yy = new int[]{iY(ya), iY(yb), iY(yc),iY(yd)}; 
       	  
       	  g.setColor(Color.BLACK);
       	  g.drawPolygon(xx, yy, 4);	
       	  g.fillPolygon(xx, yy, 4);	
      }
      
      // Drawing Leaves
      void drawLeaf(Graphics g, double x, double y, double branchDir){    	     	  
    	  double leafLen = 30, leafdir = 50, leafWidth = 10;
    	  
    	  double radxc = Math.PI/180 * branchDir, 
    			  radxb = Math.PI/180 * (branchDir+leafdir), 
    			  radxd = Math.PI/180 * (branchDir-leafdir); // Degrees -> radians    	  
    	  
    	  double xa = x,ya = y, 
    			  xb = x + leafWidth * Math.cos(radxb) , yb = y + leafWidth * Math.sin(radxb),
    			  xc = x + leafLen * Math.cos(radxc), yc = y + leafLen * Math.sin(radxc),
    			  xd = x + leafWidth * Math.cos(radxd), yd = y + leafWidth * Math.sin(radxd); 
    	  
   	      int [] xx = new int[]{iX(xa), iX(xb), iX(xc),iX(xd)};
    	  int [] yy = new int[]{iY(ya), iY(yb), iY(yc),iY(yd)};    	
    	  
    	  g.setColor(Color.GREEN);
    	  g.fillPolygon(xx, yy, 4);
    	  g.setColor(Color.BLACK);
    	  g.drawPolygon(xx, yy, 4);
      }
      
      void moveTo(Graphics g, double x, double y)
      {  xLast = x;
         yLast = y;
      }

      public void paint(Graphics g) 
      {  Dimension d = getSize();
         maxX = d.width - 1;
         maxY = d.height - 1; 
         xLast = fxStart * maxX;
         yLast = fyStart * maxY;
         dir = dirStart;   // Initial direction in degrees
         turtleGraphics(g, axiom, level, lengthFract * maxY);  
      }

      public void turtleGraphics(Graphics g, String instruction, 
         int depth, double len) 
      {  double xMark=0, yMark=0, dirMark=0;
         double thickness = len/4;         
         double rad = Math.PI/180 * dir, // Degrees -> radians
         		dx = len * Math.cos(rad), dy = len * Math.sin(rad);
         for (int i=0;i<instruction.length();i++) 
         {  char ch = instruction.charAt(i);
            switch(ch)
            {
            case 'F': // Step forward and draw
               // Start: (xLast, yLast), direction: dir, steplength: len        		   		 
       		 if(i==0) drawBranch(g, xLast + dx, yLast + dy, thickness);
       		 else drawBranch(g, xLast + dx, yLast + dy, thickness/thicknessRatio);    		 
                if (depth == 0) drawTo(g, xLast + dx, yLast + dy);
                else turtleGraphics(g, strF, depth - 1, reductFact * len);             
               break;
            case 'f': // Step forward without drawing
               // Start: (xLast, yLast), direction: dir, steplength: len
               if (depth == 0)
               {  
                  moveTo(g, xLast + dx, yLast + dy);
               }
               else
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
               dir -= rotation; break;
            case '-': // Turn left
               dir += rotation; break;
            case '[': // Save position and direction
               xMark = xLast; yMark = yLast; dirMark = dir; break;
            case ']': // Back to saved position and direction
               xLast = xMark; yLast = yMark; dir = dirMark; 
               if(depth==0){
                  if(i==9) drawLeaf(g, xLast, yLast, dir);
               }
               break;
            }
         }
      }
   }
}