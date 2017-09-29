package topcoder.datascience;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BreathFirstSearches {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Must specify the BFS test to run.");
			System.exit(-1);
		}

		if (args[0].equals("minPresses")) {
			SmartWordToy smartWordToy = new SmartWordToy();
			Date d1 = new Date();
			String[] forbid = new String[] {"a a a z", "a a z a", "a z a a", "z a a a", "a z z z", "z a z z", "z z a z", "z z z a"};
//			System.out.println("Distance aaaa to zzzz " + distance("aaaa", "zzzz"));
			System.out.println("From aaaa to zzzz " + smartWordToy.minPresses("aaaa", "zzzz", forbid) + " constraint no single original letter.");
			forbid = new String[] {};
			System.out.println("From aaaa to bbbb " + smartWordToy.minPresses("aaaa", "bbbb", forbid) + " no constraints.");
//			System.out.println("Distance aaaa to mmnn " + distance("aaaa", "mmnn"));
			System.out.println("From aaaa to mmnn " + smartWordToy.minPresses("aaaa", "mmnn", forbid) + " no constraints.");
			forbid = new String[] {"bz a a a", "a bz a a", "a a bz a", "a a a bz"};
			System.out.println("From aaaa to zzzz " + smartWordToy.minPresses("aaaa", "zzzz", forbid) + " \"bz\" constraints.");
			forbid = new String[] {"cdefghijklmnopqrstuvwxyz a a a", 
					 "a cdefghijklmnopqrstuvwxyz a a", 
					 "a a cdefghijklmnopqrstuvwxyz a", 
					 "a a a cdefghijklmnopqrstuvwxyz"};
			System.out.println("From aaaa to zzzz " + smartWordToy.minPresses("aaaa", "zzzz", forbid) + " some constraints.");
			Date d2 = new Date();
			long diff = d2.getTime() - d1.getTime();
			System.out.println("Time " + diff + " milliseconds.");
		} else if (args[0].equals("fastKnight")) {
			CaptureThemAll captureThemAll = new CaptureThemAll();
			System.out.println("a1, b3, c5 (2): " + captureThemAll.fastKnight("a1", "b3", "c5"));
			System.out.println("b1, c3, a3 (3): " + captureThemAll.fastKnight("b1", "c3", "a3"));
			System.out.println("a1, a2, b2 (6): " + captureThemAll.fastKnight("a1", "a2", "b2"));
			System.out.println("a5, b7, e4 (3): " + captureThemAll.fastKnight("a5", "b7", "e4"));
			System.out.println("h8, e2, d2 (6): " + captureThemAll.fastKnight("h8", "e2", "d2"));
		} else if (args[0].equals("WalkingHome")) {
			WalkingHome walkingHome = new WalkingHome();
			String[] cityMap = new String[]{"S.|..","..|.H"};
			System.out.println("First " + walkingHome.fewestCrossings(cityMap));
			cityMap = new String[]{"S.|..", "..|.H", "..|..", "....."};
			System.out.println("Second " + walkingHome.fewestCrossings(cityMap));
			cityMap = new String[]{"S.||...", "..||...", "..||...", "..||..H"};
			System.out.println("Third " + walkingHome.fewestCrossings(cityMap));
			cityMap = new String[]{"S.....", "---*--", "...|..", "...|.H"};			// Why 1?
			System.out.println("Fourth " + walkingHome.fewestCrossings(cityMap));
			cityMap = new String[]{"S.F..", "..F..", "--*--", "..|..", "..|.H"};	// Why 2?
			System.out.println("Fifth " + walkingHome.fewestCrossings(cityMap));
			cityMap = new String[]
				{"H|.|.|.|.|.|.|.|.|.|.|.|.|.",
				 "F|F|F|F|F|F|F|F|F|F|F|F|F|-",
				 "S|.|.|.|.|.|.|.|.|.|.|.|.|."};
			System.out.println("Sixth " + walkingHome.fewestCrossings(cityMap));
			cityMap = new String[]{"S-H"};
			System.out.println("No Go  " + walkingHome.fewestCrossings(cityMap));
		} else if (args[0].equals("RevolvingDoors")) {
			try {
				BufferedReader br = new BufferedReader(new FileReader("src/topcoder/datascience/revolvingDoors.dat"));
				try {
				    String line = br.readLine();
				    StringBuffer sb = new StringBuffer();
				    int lineNumber = 0;
				    sb.append("   0123456789012345678901234567890"+System.lineSeparator());

					List<String> readMap = new ArrayList<>();
				    while (line != null) {
				        if (line.equals("")) {
							RevolvingDoors revolvingDoor = new RevolvingDoors();
							System.out.print(sb.toString());
							System.out.println("Answer: " + revolvingDoor.turns(readMap.toArray(new String[0])));
							readMap.clear();
					        sb = new StringBuffer();
						    sb.append("   0123456789012345678901234567890"+System.lineSeparator());
							lineNumber = 0;
					        line = br.readLine();
				        }
				        sb.append(lineNumber + ") " +line);
				        sb.append(System.lineSeparator());
						readMap.add(line);
						lineNumber++;
				        line = br.readLine();
				    }
				} finally {
				    br.close();
				}
			} catch (IOException ioe) {System.err.println(ioe);}
		}
	}
}
