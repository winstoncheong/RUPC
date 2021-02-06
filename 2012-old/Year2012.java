import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Year2012{
	public static int roomWidth, roomHeight;
	private static DecimalFormat df = new DecimalFormat("###.#");

	public static void run(BufferedReader r) throws IOException{
		int datasets = Integer.parseInt(r.readLine()); // read in number of datasets
		System.out.println("Analyzing " + datasets + " data set(s)");
		for(int d = 0; d < datasets; d++){// go through all the datasets
			ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
			ArrayList<Pea> peas = new ArrayList<Pea>();

			System.out.println("Data Set " + (d + 1));

			// read in room data into an array, splitting at the space character
			String[] multidata = r.readLine().split(" ");
			roomWidth = Integer.parseInt(multidata[0]);
			roomHeight = Integer.parseInt(multidata[1]);
			System.out.println("   Room: " + roomWidth + " x " + roomHeight);

			int numBubbles = Integer.parseInt(r.readLine());
			System.out.println("   Bubbles: " + numBubbles);
			for(int b = 0; b < numBubbles; b++){// for each bubble
				multidata = r.readLine().split(" ");// read in bubble data into multidata[]
				bubbles.add(new Bubble(multidata));// construct a new bubble, add to bubbles ArrayList
			}

			int peaNum = Integer.parseInt(r.readLine());// read in number of peas

			for(int p = 0; p < peaNum; p++){// for each pea
				multidata = r.readLine().split(" ");// read in pea data into multidata[]
				peas.add(new Pea(multidata));// construct a new pea, add to pea ArrayList
			}
			System.out.println("   Launching " + peaNum + " pea(s)");
			simulator(bubbles, peas);
			Bubble.resetBubbleIdAssigner();
		}
	}

	public static void simulator(ArrayList<Bubble> bubbles, ArrayList<Pea> peas){
		// System.out.println(bubbles);
		// System.out.println(peas);
		int shotCounter = 1, points = 0;
		double time = 0;
		Pea currentPea = null;
		while(!bubbles.isEmpty() && (!peas.isEmpty() || currentPea != null)){// while there are still bubbles and peas
			if(currentPea == null)
				currentPea = peas.remove(0);// take the first pea in the arraylist out.

			if(currentPea.getStartTime() == (int) time && !currentPea.isAlive()){// if time for current pea to be shot, and the current pea isn't
																					// alive
				currentPea.setAlive(true);
				System.out.println("   Shot " + shotCounter++ + " fired at " + (double) currentPea.getStartTime());
			}

			if(currentPea.isAlive()){
				// move the pea
				currentPea.updatePosition(time);
				// check if has contacted surface
				currentPea.checkHasHitWall(time);
			}

			// move bubbles
			for(Bubble b: bubbles)
				b.moveBubble(time);

			// check if pea has hit bubble
			for(Bubble b: bubbles)
				if(b.hasPeaHitBubble(currentPea.getxPea(), currentPea.getyPea())){// if the pea has hit the bubble
					System.out.println("       hit bubble " + b.getBubbleID() + " @ t = " + df.format(time));
					points += (75 - b.getRadius()) * (1 + Math.abs(b.getxMovement()) + Math.abs(b.getyMovement()));// increment score!
				}

			// check if bubble has contacted surface
			for(Bubble b: bubbles){
				if(b.hasHitSurface())
					System.out.println("       (" + b.getBubbleID() + " popped) @ t = " + df.format(time));
			}

			// update bubble ArrayList
			for(int i = 0; i < bubbles.size();)
				if(bubbles.get(i).isPopped())// if the bubble is popped
					bubbles.remove(i);// remove bubble from ArrayList
				else
					i++;// otherwise increment iterator

			if(currentPea.isAlive() && currentPea.hasHitFloor(time))
				currentPea = null;

			time += .1;
		}
		System.out.println();
		System.out.println("  Remaining shots: " + (peas.size() + (currentPea == null ? 0 : 1)));// count the currentPea if it exists
		System.out.print("  Remaining bubbles: ");
		for(Bubble b: bubbles)
			System.out.print(b.getBubbleID() + " ");
		System.out.println();
		System.out.println("  Final score: " + points);
	}

	public static void main(String[] args) throws IOException{
		// run(new BufferedReader(new InputStreamReader(System.in)));
		runTest("sample1.txt");
		// runTest("sample2.txt");
		// runTest("test1.txt");
		// runTest("test2.txt");
	}

	private static void runTest(String fileName) throws IOException{
		BufferedReader r = null;
		String path = System.getProperty("user.dir");
		r = new BufferedReader(new FileReader(path + File.separator + fileName));
		run(r);
	}
}
