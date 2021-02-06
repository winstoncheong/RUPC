import java.text.DecimalFormat;

public class Pea{
	// Pea Initial Location (Muzzle)
	private double xPeaInitial, yPeaInitial;

	// Pea Coordinates
	private double xPea, yPea;

	// Pea Shooter Angle and Speed
	private int angle, velocity;
	private double horizontalVelocity, verticalVelocity;
	private double gravity = 9.81;
	private boolean hasHitWall = false;

	// time vars
	private double startTime;
	private double collisionTime;
	private boolean isAlive;

	private double muzzleLength = 40;

	private static DecimalFormat df = new DecimalFormat("###.#");

	public Pea(String... args){
		angle = Integer.parseInt(args[0]);
		velocity = Integer.parseInt(args[1]);
		startTime = Integer.parseInt(args[2]);

		// calculate initial position
		xPeaInitial = (muzzleLength * Math.cos(Math.toRadians(angle)) + 5);
		yPeaInitial = (muzzleLength * Math.sin(Math.toRadians(angle)) + 5);
		xPea = xPeaInitial;
		yPea = yPeaInitial;
		horizontalVelocity = (velocity * Math.cos(Math.toRadians(angle)));
		verticalVelocity = (velocity * Math.sin(Math.toRadians(angle)));

		isAlive = false;
	}

	public void updatePosition(double currentTime){
		double time = currentTime - startTime;
		if(hasHitWall)
			yPea -= (gravity * Math.pow((currentTime - collisionTime), 2));
		else{
			xPea = xPeaInitial + (horizontalVelocity * time);
			yPea = yPeaInitial + (verticalVelocity * time - (gravity * Math.pow(time, 2)) / 2);
		}

	}

	public void checkHasHitWall(double time){
		if((xPea >= Year2012.roomWidth) || (yPea >= Year2012.roomHeight)){
			if(!hasHitWall) // If this is the first time, save the collision time.
			{
				collisionTime = time;
				if(yPea >= Year2012.roomHeight)
					System.out.println("       hit ceiling @ t = " + df.format(time));
				else if(xPea >= Year2012.roomWidth)
					System.out.println("       hit wall @ t = " + df.format(time));
			}
			hasHitWall = true;
		}
	}

	public boolean hasHitFloor(double time){
		if(yPea < 0){
			System.out.println("       hit floor @ t = " + df.format(time));
			return true;
		}
		return false;
	}

	// ====== getters and setters ====/

	public double getxPeaInitial(){
		return xPeaInitial;
	}

	public double getyPeaInitial(){
		return yPeaInitial;
	}

	public double getxPea(){
		return xPea;
	}

	public double getyPea(){
		return yPea;
	}

	public double getAngle(){
		return angle;
	}

	public boolean isAlive(){
		return isAlive;
	}

	public void setAlive(boolean isAlive){
		this.isAlive = isAlive;
	}

	public double getStartTime(){
		return startTime;
	}

}
