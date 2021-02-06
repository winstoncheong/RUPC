public class Bubble{
	private static int bubbleIDAssigner = 1;// static variable to assign bubbles their ID
	private int bubbleID;
	private double xCenter, yCenter, radius, xMovement, yMovement;
	private boolean isPopped;

	public Bubble(String... vars){// constructor process the String[], converting the Strings into ints
		xCenter = Integer.parseInt(vars[0]);
		yCenter = Integer.parseInt(vars[1]);
		radius = Integer.parseInt(vars[2]);
		xMovement = Integer.parseInt(vars[3]);
		yMovement = Integer.parseInt(vars[4]);

		isPopped = false;
		bubbleID = bubbleIDAssigner++;
	}

	public static void resetBubbleIdAssigner(){
		bubbleIDAssigner = 1;
	}

	public void moveBubble(double time){
		xCenter += xMovement / 10;
		yCenter += yMovement / 10;
		if((int) time == time){
			xCenter = Math.round(xCenter);
			yCenter = Math.round(yCenter);
		}
	}

	public boolean hasHitSurface(){
		if(xCenter - radius < 0 || yCenter - radius < 0 || xCenter + radius > Year2012.roomWidth || yCenter + radius > Year2012.roomHeight){
			isPopped = true;
			return true;
		}
		return false;
	}

	public boolean hasPeaHitBubble(double xPea, double yPea){
		// if bubble is already popped, ignore.
		if(isPopped == false && Math.sqrt(Math.pow(xPea - xCenter, 2) + Math.pow(yPea - yCenter, 2)) < radius){
			isPopped = true;
			return true;
		}
		return false;
	}

	// ======= getters and setters =======/

	public int getBubbleID(){
		return bubbleID;
	}

	public double getxCenter(){
		return xCenter;
	}

	public double getyCenter(){
		return yCenter;
	}

	public double getRadius(){
		return radius;
	}

	public double getxMovement(){
		return xMovement;
	}

	public double getyMovement(){
		return yMovement;
	}

	public boolean isPopped(){
		return isPopped;
	}

	public void setPopped(boolean isPopped){
		this.isPopped = isPopped;
	}

}