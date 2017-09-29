package topcoder.datascience;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RevolvingDoors {
	int Y, X;
	char[][] charMap = null;
	int[] endLoc = null; 
	int[][] direction = new int[][] {{0,1},  {-1,0},  {0,-1}, {1,0}}; // RIGHT, UP, LEFT, DOWN
	int[][] diagonals = new int[][] {{1,1},  {1,-1},  {-1,1}, {-1,-1}};	// DownRight, DownLeft, UpRight, UpLeft
	int[][] directionDiags = new int[][] {{0,2}, {2,3}, {1,3}, {0,1}}; // Diagonals according to moves.
	Map<String, Integer> totalMap = new TreeMap<>();
	int totalVisitedCnt = 0;

	private int turnDoor(String doorCoord, Map<String, String[]> revolvingDoors){
		if(revolvingDoors.containsKey(doorCoord)) {
			String[] door = doorCoord.split(",");
			String[] value = revolvingDoors.get(doorCoord);
			String[] first = value[0].split(",");
			String[] second = value[1].split(",");
			String[] newValue = new String[2];
			if (Integer.parseInt(door[0]) == Integer.parseInt(first[0])) {	//	Y is same, must be horizontal
				if (Integer.parseInt(door[1]) == Integer.parseInt(first[1])) {
					System.err.println("Your program does not work");
					return 0;
				}
				charMap[Integer.parseInt(door[0])-1][Integer.parseInt(door[1])] = '|';
				charMap[Integer.parseInt(door[0])+1][Integer.parseInt(door[1])] = '|';
				newValue[0] = (Integer.parseInt(door[0])-1)+","+Integer.parseInt(door[1]);
				newValue[1] = (Integer.parseInt(door[0])+1)+","+Integer.parseInt(door[1]);
			} else {
				charMap[Integer.parseInt(door[0])][Integer.parseInt(door[1])-1] = '-';
				charMap[Integer.parseInt(door[0])][Integer.parseInt(door[1])+1] = '-';
				newValue[0] = Integer.parseInt(door[0])+","+(Integer.parseInt(door[1])-1);
				newValue[1] = Integer.parseInt(door[0])+","+(Integer.parseInt(door[1])+1);
			}
			charMap[Integer.parseInt(first[0])][Integer.parseInt(first[1])] = ' ';
			charMap[Integer.parseInt(second[0])][Integer.parseInt(second[1])] = ' ';
			revolvingDoors.put(doorCoord, newValue);
			return 1;
		}
		return 0;
	}

	private String turnDoor(String doorCoord, Map<String, String[]> revolvingDoors, List<String> requestOpen){
		int retval = turnDoor(doorCoord, revolvingDoors);
		if (retval == 1) {
			requestOpen.remove(doorCoord);
			return doorCoord;
		}
		return null;
	}

	private String activateDoor(int[] position, Map<String, String[]> revolvingDoors, List<String> requestOpen){
		for (int diag = 0; diag < diagonals.length; diag++) {
			String pos = (position[0]+diagonals[diag][0])+","+(position[1]+diagonals[diag][1]);
			if(revolvingDoors.containsKey(pos) && requestOpen.contains(pos)) {
				return turnDoor(pos, revolvingDoors, requestOpen);
			}
		}
		return null;
	}

	private void resetDoors(Map<String, String[]> revolvingDoors){
		for(Entry<String, String[]> rDor : revolvingDoors.entrySet()) {
			String[] keySplit = rDor.getKey().split(",");
			String[] value = rDor.getValue();
			String[] first = value[0].split(",");
			String[] second = value[1].split(",");
			if (charMap[Integer.parseInt(first[0])][Integer.parseInt(first[1])] == ' '
					&& charMap[Integer.parseInt(second[0])][Integer.parseInt(second[1])] == ' ') {
				if (Integer.parseInt(keySplit[0]) == Integer.parseInt(first[0])) {
					charMap[Integer.parseInt(first[0])][Integer.parseInt(first[1])] = '-';
					charMap[Integer.parseInt(second[0])][Integer.parseInt(second[1])] = '-';
					charMap[Integer.parseInt(keySplit[0])-1][Integer.parseInt(keySplit[1])] = ' ';
					charMap[Integer.parseInt(keySplit[0])+1][Integer.parseInt(keySplit[1])] = ' ';
				} else {
					charMap[Integer.parseInt(first[0])][Integer.parseInt(first[1])] = '|';
					charMap[Integer.parseInt(second[0])][Integer.parseInt(second[1])] = '|';
					charMap[Integer.parseInt(keySplit[0])][Integer.parseInt(keySplit[1])+1] = ' ';
					charMap[Integer.parseInt(keySplit[0])][Integer.parseInt(keySplit[1])-1] = ' ';
				}
			}
		}
	}

	private List<Integer> findDiagonals(int[] coord, Map<String, String[]> revolvingDoors) {
		List<Integer> list = new ArrayList<>();
		for (int diag = 0; diag <= diagonals.length; diag++) {
			if (diag == diagonals.length) break;
			if(revolvingDoors.containsKey((coord[0]+diagonals[diag][0])+","+(coord[1]+diagonals[diag][1]))) {
				list.add(diag);
			}
		}
		return list;
	}

	public int turns(String[] map){
		Y = map.length;
		X = map[0].length();
		charMap = new char[Y][X];
		Map<String, String[]> revolvingDoors = new HashMap<String, String[]>();
		List<String> requestOpen = new ArrayList<>();
		int lastX=0, lastY = 0;
		Set<String> dashList = new HashSet<>();
		Set<String> bangList = new HashSet<>();
		for (int i = 0; i < Y; i++) {
			for (int j = 0; j < X; j++) {
				charMap[i][j] = map[i].charAt(j);
				switch (charMap[i][j]) {
				case 'S':
					lastX = j;
					lastY = i;
					break;
				case 'E':
					endLoc = new int[]{i,j}; 
					break;
				case 'O':
					String[] aDoor = null;
					if (dashList.contains(i+","+(j-1))) {
						aDoor = new String[]{i+","+(j-1), ""};
						dashList.remove(i+","+(j-1));
					} else if (bangList.contains((i-1)+","+j)) {
						aDoor = new String[]{(i-1)+","+j, ""};
						bangList.remove((i-1)+","+j);
					}
					revolvingDoors.put(i+","+j, aDoor);
					break;
				case '-':
					if(revolvingDoors.containsKey(i+","+(j-1))) {
						revolvingDoors.get(i+","+(j-1))[1] = i+","+j;
					} else {
						dashList.add(i+","+j);
					}
					break;
				case '|':
					if(revolvingDoors.containsKey((i-1)+","+j)) {
						revolvingDoors.get((i-1)+","+j)[1] = i+","+j;
					} else {
						bangList.add(i+","+j);
					}
				}
			}
		}

//		List<String> intList = new ArrayList<>();
		Map<String, Integer> visited = new TreeMap<>();
		return turns(lastY, lastX, requestOpen, visited, revolvingDoors, -1, 0);
//		while (true) {
//		String one = intList.get(0);
//		String[] oneSplit = one.split(",");
//		intList.remove(0);
//		if (activateDoor(new int[] {lastY, lastX}, requestOpen)) {
//			retVal++;
//		}
//		lastY = Integer.parseInt(oneSplit[0]);
//		lastX = Integer.parseInt(oneSplit[1]);
//		currDirection = Integer.parseInt(oneSplit[2]);
//		}
	}

	private int turns(int lastY, int lastX, List<String> requestOpen, Map<String, Integer> visited, Map<String, String[]> revolvingDoors, int currDirection, int retVal){
		List<String> intList = new ArrayList<>();
		if (visited.containsKey(lastY+","+lastX)) {
			visited.put(lastY+","+lastX, visited.get(lastY+","+lastX)+1);
		} else {
			visited.put(lastY+","+lastX, 1);
		}
		totalVisitedCnt++;
		for (int move=0; move < direction.length; move++) {	// All possible moves
			int newY = lastY+direction[move][0];
			int newX = lastX+direction[move][1];
			if (newY < 0 || newX < 0 || newY >= Y || newX >= X){continue;}
			char nextMove = charMap[newY][newX];
			switch (nextMove) {
			case '#':		// Can not occupy these spaces.
			case 'O':
				break;
			case '-':	// Find revolving door
			case '|':
				int[][] spaces;
				if (direction[move][0] == 0) {
					spaces = new int[][]{{lastY-1,lastX,lastY-1,newX},{lastY+1,lastX,lastY+1,newX}};
				} else {		// if (direction[move][1] == 0) 
					spaces = new int[][]{{lastY,lastX+1,newY,lastX+1},{lastY,lastX-1,newY,lastX-1}};
				}
//				System.out.println("checking (" + lastY + "," + lastX + ") with " + nextMove + " " + move);
				// diags should be found right/left | or up/down - only, i.e facing door
				if ((move % 2 == 0 && nextMove == '|') || (move % 2 == 1 && nextMove == '-')) {
					List<Integer> diags = findDiagonals(new int[]{lastY, lastX}, revolvingDoors);
					if (!diags.isEmpty()) {
						boolean moveDoor = true;
						for (int i = 0; i < spaces.length; i++) {
//							System.out.println("checking " + spaces[i][0] + "," + spaces[i][1] + "," + spaces[i][2] + "," + spaces[i][3] + ".");
							if (spaces[i][0] >= 0 && spaces[i][0] < Y && spaces[i][1] >= 0 && spaces[i][1] < X
									&& charMap[spaces[i][0]][spaces[i][1]] == ' ' && !totalMap.containsKey(spaces[i][0]+","+spaces[i][1])
								&& spaces[i][2] >= 0 && spaces[i][2] < Y && spaces[i][3] >= 0 && spaces[i][3] < X 
									&& charMap[spaces[i][2]][spaces[i][3]] == ' ' && !totalMap.containsKey(spaces[i][0]+","+spaces[i][1])) {
								moveDoor = false;
								break;
							}
						}
//						if (direction[move][0] == 0) {		// look for blocking walls.
//							if (lastY+(-1*diagonals[diag][0]) >= 0 || lastY+(-1*diagonals[diag][0]) < Y || charMap[lastY+(-1*diagonals[diag][0])][lastX] != ' ') {
//								moveDoor = true;
//							}
//						} else {
//							if (lastY+(-1*diagonals[diag][1]) >= 0 || lastY+(-1*diagonals[diag][1]) < X || charMap[lastY][lastX+(-1*diagonals[diag][1])] != ' ') {
//								moveDoor = true;
//							}
//						}
						for (int diag : diags) {
							if (moveDoor && !requestOpen.contains((lastY+diagonals[diag][0])+","+(lastX+diagonals[diag][1]))) {
								if (directionDiags[move][0] == diag || directionDiags[move][1] == diag) {
									System.out.println("Facing door " + (lastY+diagonals[diag][0])+","+(lastX+diagonals[diag][1]) + " as (" + lastY + ","+ lastX + "," + move +") " + diag);
									requestOpen.add((lastY+diagonals[diag][0])+","+(lastX+diagonals[diag][1]));
								} else {
									System.out.println("Facing door " + (lastY+diagonals[diag][0])+","+(lastX+diagonals[diag][1]) + " as (" + lastY + ","+ lastX + "," + move +") " + diag + " but in wrong direction.");
								}
							}
						}
					} else {System.err.println("Error not facing door!!");}
				} else {	// Facing door edge
					int[] hings = new int[]{direction[move][0]*2, direction[move][1]*2};
					boolean moveDoor = true;
					for (int i = 0; i < spaces.length; i++) {
						if (spaces[i][0] >= 0 && spaces[i][0] < Y && spaces[i][1] >= 0 && spaces[i][1] < X
								&& charMap[spaces[i][0]][spaces[i][1]] == ' '
							&& spaces[i][2] >= 0 && spaces[i][2] < Y && spaces[i][3] >= 0 && spaces[i][3] < X 
								&& charMap[spaces[i][2]][spaces[i][3]] == ' ') {
							moveDoor = false;
							break;
						}
					}
					if (moveDoor) {
						requestOpen.add((lastY+hings[0])+","+(lastX+hings[1]));
						System.out.println("Head on " + (lastY+hings[0])+","+(lastX+hings[1]) + " as (" + lastY + ","+ lastX + "," + move +")");
					}
					continue;
				}
//				if (diag < diagonals.length) {
//					int[] boldMove = new int[] {diagonals[diag][0]*direction[move][0], diagonals[diag][1]*direction[move][1]};
//					if (newY+((-1*boldMove[0])*diagonals[diag][0])>=0 && newY+((-1*boldMove[0])*diagonals[diag][0])< Y
//							&& charMap[newY+((-1*boldMove[0])*diagonals[diag][0])][newX+(-1*boldMove[1])] == ' ') {	// Is there a space after the door?
//						requestOpen.add((lastY + diagonals[diag][0])+","+(lastX + diagonals[diag][1]));
//						continue;	// Cannot move forward
//					}
//				} else {
//					if (currDirection != (move+2) % (direction.length)) {
//						if (nextMove == '-') {
//							if (currDirection == move || currDirection == -1) {	// Request to open door must be head on
//								if(revolvingDoors.containsKey((lastY+hings[0])+","+(lastX+hings[1]))) {	// lastY+","+(lastX+direction[move][1]-1)
//									if (charMap[lastY-1][lastX] == ' ' || charMap[lastY+1][lastX] == ' ') {
//									} else if (charMap[lastY-1][lastX+direction[move][1]+1] == ' ' || charMap[lastY+1][lastX+direction[move][1]+1] == ' ') {
//										requestOpen.add((lastY+hings[0])+","+(lastX+hings[1]));
//										continue;	// Cannot move forward
//									} else if (charMap[lastY-1][lastX+direction[move][1]+1] == '|' || charMap[lastY+1][lastX+direction[move][1]+1] == '|') {
//										continue;	// Cannot move forward
//									}
//								}
//							} else {
//								if(revolvingDoors.containsKey((lastY+1)+","+lastX)) {
//									requestOpen.add((lastY+1)+","+lastX);
//									continue;	// Cannot move forward
//								} else if(revolvingDoors.containsKey((lastY-1)+","+lastX)) {
//									requestOpen.add((lastY-1)+","+lastX);
//									continue;	// Cannot move forward
//								}
//							}
//						} else {
//							int oldDiag = findDiagonals(new int[]{lastY, lastX}, revolvingDoors);
//							if (currDirection == move || currDirection == -1) {	// Request to open door must be head on
//								if (oldDiag < diagonals.length) {
//									if (lastY+(-1*diagonals[oldDiag][0]) > -1 && lastX+diagonals[oldDiag][1] > -1 && lastX+diagonals[oldDiag][1] < X && lastY+(-1*diagonals[oldDiag][0]) < Y
//											&& charMap[lastY+(-1*diagonals[oldDiag][0])][lastX] == ' ' && charMap[lastY+(-1*diagonals[oldDiag][0])][lastX+diagonals[oldDiag][1]] == ' ') {
//									} else {
//										if (charMap[lastY+hings[0]][lastX+hings[1]] == ' ' || charMap[lastY+(-1*diagonals[oldDiag][0])][newX] == ' ') {
//											requestOpen.add((lastY+diagonals[oldDiag][0])+","+(lastX+diagonals[oldDiag][1]));
//										}
//									}
//								}
//								if(revolvingDoors.containsKey((lastY+hings[0])+","+(lastX+hings[1]))) {	// lastY+","+(lastX+direction[move][1]-1)
//									if (charMap[lastY-1][lastX] == ' ' || charMap[lastY+1][lastX] == ' ') {
//									} else if (charMap[lastY-1][lastX+direction[move][1]+1] == ' ' || charMap[lastY+1][lastX+direction[move][1]+1] == ' ') {
//										requestOpen.add((lastY+hings[0])+","+(lastX+hings[1]));
//										continue;	// Cannot move forward
//									}
//								}
//							} else {
//								if (currDirection % 2 == 1) {	// must be head on
//									if (oldDiag < diagonals.length) {
//										if (lastY+(-1*diagonals[oldDiag][0]) > -1 && lastX+diagonals[oldDiag][1] > -1 && lastX+diagonals[oldDiag][1] < X && lastY+(-1*diagonals[oldDiag][0]) < Y
//												&& charMap[lastY+(-1*diagonals[oldDiag][0])][lastX] == ' ' && charMap[lastY+(-1*diagonals[oldDiag][0])][lastX+diagonals[oldDiag][1]] == ' ') {
//										} else {
//											if (charMap[lastY+hings[0]][lastX+hings[1]] == ' ' || charMap[lastY+(-1*diagonals[oldDiag][0])][newX] == ' ') {
//												requestOpen.add((lastY+diagonals[oldDiag][0])+","+(lastX+diagonals[oldDiag][1]));
//											}
//										}
//									}
//								}
////									if(revolvingDoors.containsKey((lastY+direction[move][0]+1)+","+lastX)) {
////										if (currDirection == 3 &&
////												(charMap[lastY+direction[move][0]+1][lastX-1] == ' ' || charMap[lastY+direction[move][0]+1][lastX+1] == ' ')) {
////											requestOpen.add((lastY+direction[move][0]+1)+","+lastX);
////											continue;	// Cannot move forward
////										}
////									} else if(revolvingDoors.containsKey((lastY+direction[move][0]-1)+","+lastX)) {
////										if (currDirection == 1 && 
////												(charMap[lastY+direction[move][0]-1][lastX-1] == ' ' || charMap[lastY+direction[move][0]-1][lastX+1] == ' ')) {
////											requestOpen.add((lastY+direction[move][0]-1)+","+lastX);
////											continue;	// Cannot move forward
////										}
////									}
//								}
//						}
//					}
//				}		// still needs to be put into list.
			default:
				if (currDirection == -1) {
					intList.add((lastY+direction[move][0])+","+(lastX+direction[move][1]+","+move));
					totalMap.put((lastY+direction[move][0])+","+(lastX+direction[move][1]), 1);
				} else {
					if (currDirection != (move+2) % (direction.length)) {
						if (!totalMap.containsKey((lastY+direction[move][0])+","+(lastX+direction[move][1]))) {
							totalMap.put((lastY+direction[move][0])+","+(lastX+direction[move][1]),1);
							intList.add((lastY+direction[move][0])+","+(lastX+direction[move][1]+","+move));
						} else {
							if (totalMap.get((lastY+direction[move][0])+","+(lastX+direction[move][1])) < 3) {
								totalMap.put((lastY+direction[move][0])+","+(lastX+direction[move][1]), totalMap.get((lastY+direction[move][0])+","+(lastX+direction[move][1]))+1);
								intList.add((lastY+direction[move][0])+","+(lastX+direction[move][1]+","+move));
							}
							
						}
					}
				}
			}
		}

		String doorTurn = activateDoor(new int[] {lastY, lastX}, revolvingDoors, requestOpen);
		if (doorTurn != null) {
			retVal++;
			System.out.println("("+lastY+","+lastX+") opens door at " + doorTurn + ".");
		}
		if (lastY == endLoc[0] && lastX == endLoc[1]) return retVal;
		if (intList.isEmpty()) return -1;
		int min = 1000;
		System.out.println("("+lastY+","+lastX+"," + currDirection + ") ret: " + retVal + " in " + intList + " V " + visited);
		for (String intObj : intList) {
			String[] intSplit = intObj.split(",");
			int newY = Integer.parseInt(intSplit[0]);
			int newX = Integer.parseInt(intSplit[1]);
			int currDir = Integer.parseInt(intSplit[2]);
			if (!visited.containsKey(newY+","+newX) || visited.get(newY+","+newX) <= 1) {
				if (lastY != newY) {currDirection = lastY - newY < 0 ? 3 : 1;}
				if (lastX != newX) {currDirection = lastX - newX < 0 ? 0 : 2;}
				Map<String, Integer> tempVisited = new TreeMap<>(visited);
				Map<String, String[]> tempRevolvingDoors = new TreeMap<String, String[]>(revolvingDoors);
				int iTemp = turns(newY, newX, requestOpen, tempVisited, tempRevolvingDoors, currDir, retVal);
				System.out.print("<("+lastY+","+lastX+") ("+newY+","+newX+") "+retVal+","+iTemp+"> ");
				resetDoors(tempRevolvingDoors);
				if (iTemp >= 0 && iTemp < min) {
					min = iTemp;
					if (iTemp <= retVal) { return iTemp;}
				}
			}
		}
		return min == 1000 ? -1 : min;
	}

}
