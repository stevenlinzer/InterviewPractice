package topcoder.datascience;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import structures.UnionFind;
import structures.UnionFindUtils;

public class LargestCircle {
	public int radiusO4(String[] grid) {
		int height = grid.length;
		int width = grid[0].length();
		int maxRadius = Math.min(height/2, width/2);

		for (int r = maxRadius; r >= 1; r--) {
			int r2 = r*r;
			for (int cx = r; cx <= width - r; cx++)		// Start columns from r (radius) separate from right border.
				for (int cy = r; cy <= height - r; cy++) {	// Start rows from r (radius) separate from bottom.
					boolean wallFlag = false;
					boolean spaceFlag = false;
					for (int y = 0; y < height; y++) {
						int cx1 = Math.abs(cx - y);
						int cx2 = Math.abs(cx - y - 1);
						int cxMin = Math.min(cx1,cx2)*Math.min(cx1,cx2);
						int cxMax = Math.max(cx1,cx2)*Math.max(cx1,cx2);
//						System.out.println(r+" R**2=" +r2+ " ("+cy+","+cx+") CX min "+cxMin +", CX max "+cxMax);
						for (int x = 0; x < width; x++) {
							int cy1 = Math.abs(cy - x);
							int cy2 = Math.abs(cy - x - 1);
							int cyMin = Math.min(cy1,cy2)*Math.min(cy1,cy2);
							int cyMax = Math.max(cy1,cy2)*Math.max(cy1,cy2);
							//	x**2 + y**2
							int min = cxMin + cyMin;
							int max = cxMax + cyMax;

//							System.out.print("  ("+y+","+x+") CY Min "+ cyMin + ", CY max "+cyMax+", MIN "+ min + ", MAX " + max + " ");
							if (min >= r2 || max <= r2) {
//								System.out.println("continue");
								continue;
							}

							if (grid[y].charAt(x) == '#') {
//								System.out.println("#");
								wallFlag = true;
								break;
							}  else {
								spaceFlag = true;
//								System.out.println(". and is clean.");
							}
						}
						if (wallFlag) break;	// Hit wall in this row.
					}
					if (!wallFlag) {
						if (spaceFlag) {
							System.out.println("("+cy+","+cx+")");
							return r;	// Has not hit a wall all (y,x) 
						}
						return 0;
					}
				}
		}
		return 0;
	}

	public int radius(String[] grid) {
		if (grid == null || grid.length < 1) return 0;
		UnionFind uf = new UnionFind(grid.length * grid[0].length());
		UnionFindUtils.makeFriends(grid, uf);
//		UnionFindUtils.printDetails(uf);
		List<String> singles = new ArrayList<>();
		Map<Integer, List<int[]>> spaceMap = new HashMap<>();
		for (List<Integer> connList : uf.getConnectedLists()) {
			int index = connList.get(0);
			int y = index / grid[0].length();
			int x = index % grid[0].length();
			if (grid[y].charAt(x) == '.') {
				if (connList.size() < 4) {
					singles.add(y+","+x);
					continue;
				}
				if (!spaceMap.containsKey(uf.root(index))) {
					spaceMap.put(uf.root(index), new ArrayList<>());
				}
				for (Integer connIndex : connList) {
					y = connIndex / grid[0].length();
					x = connIndex % grid[0].length();
					if (spaceMap.get(uf.root(index)).isEmpty()) {
						spaceMap.get(uf.root(index)).add(new int[]{y,x,y,x});
					} else {
						boolean found = false;
						for (int[] shapeList : spaceMap.get(uf.root(index))) {
							if (shapeList[0] == shapeList[2] && shapeList[2] == y && shapeList[3] + 1 == x) {
								shapeList[3] = x;
								found = true;
								break;
							} else if (shapeList[1] == shapeList[3] && shapeList[3] == x && shapeList[2] + 1 == y) {
								shapeList[2] = y;
								found = true;
								break;
							}
						}
						if (!found) {
							spaceMap.get(uf.root(index)).add(new int[]{y,x,y,x});
						}
					}
				}
			}
		}

		for (Entry<Integer, List<int[]>> e : spaceMap.entrySet()) {
			List<int[]> removeList = new ArrayList<int[]>();
			int firstMatch = 0;
			for (int i = firstMatch+1; i <e.getValue().size(); i++) {
				if(e.getValue().get(firstMatch)[2]+1 == e.getValue().get(i)[2] 
						&& e.getValue().get(firstMatch)[1] == e.getValue().get(i)[1] && e.getValue().get(firstMatch)[3] == e.getValue().get(i)[3]) {
					e.getValue().get(firstMatch)[2]++;
					removeList.add(e.getValue().get(i));
				} else if(e.getValue().get(firstMatch)[3]+1 == e.getValue().get(i)[3] 
						&& e.getValue().get(firstMatch)[0] == e.getValue().get(i)[0] && e.getValue().get(firstMatch)[2] == e.getValue().get(i)[2]) {
					e.getValue().get(firstMatch)[3]++;
					removeList.add(e.getValue().get(i));
				} else {
					firstMatch = i;
				}
			}
			if (removeList.size() > 0) {
				e.getValue().removeAll(removeList);
			}
		}

		List<int[]> rectangles = new ArrayList<>();
		Map<Integer, List<int[]>> lineMap = new HashMap<>();
		for (Entry<Integer, List<int[]>> e : spaceMap.entrySet()) {
//			System.out.print("SP " + e.getKey() + " ");
			for (int[] ints : e.getValue()) {
				int rise = ints[2] - ints[0];
				int run = ints[3] - ints[1];
//				System.out.print("(" +ints[0] + "," + ints[1] + "," + ints[2] + "," + ints[3] + ") rise " + rise + ", run " + run + " ");
				if (rise > 0 && run > 0) {
					rectangles.add(ints);
				} else if (rise > 0 || run > 0) {
					int lineKey = rise - run > 0 ? rise : run;
					if (!lineMap.containsKey(lineKey)) {
						lineMap.put(lineKey, new ArrayList<int[]>());
					}
					lineMap.get(lineKey).add(ints);
				}
			}
//			System.out.println();
		}
		int maxRad = 0;
		if (rectangles.size() == 1) {
			int rise = rectangles.get(0)[2] - rectangles.get(0)[0];
			int run = rectangles.get(0)[3] - rectangles.get(0)[1];
			int centerX = rectangles.get(0)[0] + (rise/2);
			int centerY = rectangles.get(0)[1] + (run/2);
			double rad = Math.min(Math.sqrt(rise*rise + run*run), Math.min(grid.length, grid[0].length())) / 2;
//			System.out.print("Center (" + centerX + "," + centerY + ") " + rad + " ");
			if (isCircleInRectangle(rectangles, (int)(rad-1), centerY, centerX)) maxRad = (int)(rad < 1.0 ? 1 : Math.round(rad)-1);
		} else if (rectangles.size() > 1) {
			int centerStartX = -1;
			int centerStartY = -1;
			int centerEndX = -1;
			int centerEndY = -1;
			int minDiam = -1;
			int maxDiam = -1;
			double maxRadius = 0;
			for (int[] r : rectangles) {
				int rise = r[2] - r[0];
				int run = r[3] - r[1];
				double rad = Math.min(Math.sqrt(rise*rise + run*run), Math.min(grid.length, grid[0].length())) / 2;
				if (rad > maxRadius) {
					centerStartX = r[1] + (int)(rad/2);
					centerStartY = r[0] + (int)(rad/2);
					centerEndX = r[3] - (int)(rad/2);
					centerEndY = r[2] - (int)(rad/2);
					maxRadius = rad;
				}
				if (minDiam == -1) {
					minDiam = (int)rad;
					maxDiam = minDiam;
				} else {
					maxDiam += (int)rad;
				}
			}
			for (int r = maxDiam; r >= minDiam; r--) {
				for (int y = centerStartY; y < centerEndY; y++) {
					for (int x = centerStartX; x < centerEndX; x++) {
						if (isCircleInRectangle(rectangles, (int)(r-1), y, x)) {
							if ((int)(r-1) > maxRad) maxRad = (int)(r-1);
							break;
						}
					}
				}
			}
		}
		if (lineMap.size() > 0) {			// Process lines;
			for (Entry<Integer, List<int[]>> e : lineMap.entrySet()) {
				if (e.getValue().size() == 4) {
//					String[] markers = new String[4];
//					int width = -1;
//					int height = -1;
					int[] bottom = null;
					int[] left = null;
					int[] right = null;
					int[] top = null;
					int[] riseRun = new int[4];
//					int[] starts = new int[4];
//					int[] ends = new int[4];
					for (int i = 0; i < e.getValue().size(); i++) {
						int rise = e.getValue().get(i)[2] - e.getValue().get(i)[0];
						int run = e.getValue().get(i)[3] - e.getValue().get(i)[1];
						if (rise - run > 0) {
//							markers[i] = "rise";
							riseRun[i] = rise;
							if (left == null) left = e.getValue().get(i);
							else {
								if (left[1] < e.getValue().get(i)[1]) right = e.getValue().get(i);
								else {
									right = left;
									left = e.getValue().get(i);
								}
							}
//							starts[i] = e.getValue().get(i)[0];
//							ends[i] = e.getValue().get(i)[2];
//							if (height == -1) height = e.getValue().get(i)[1];
//							else {
//								if (height > e.getValue().get(i)[1]) {height -= e.getValue().get(i)[1];}
//								else height = e.getValue().get(i)[1] - height;
//							}
						} else {
//							markers[i] = "run";
							riseRun[i] = run;
							if (top == null) top = e.getValue().get(i);
							else {
								if (top[0] < e.getValue().get(i)[0]) bottom = e.getValue().get(i);
								else {
									bottom = top;
									top = e.getValue().get(i);
								}
							}
//							starts[i] = e.getValue().get(i)[1];
//							ends[i] = e.getValue().get(i)[3];
//							if (width == -1) width = e.getValue().get(i)[0];
//							else {
//								if (width > e.getValue().get(i)[0]) {width -= e.getValue().get(i)[0];}
//								else width = e.getValue().get(i)[0] - width;
//							}
						}
//						if (i > 0) {
//							if (riseRun[0] != riseRun[i]) {
//								isCircle = false;
//								break;
//							}
//						}
					}
					boolean isCircle = bottom != null && top != null && left != null && right != null
							&& left[0] == right[0] && top[1] == bottom[1];
					if (isCircle) {
						int raduis = (right[1] - left[1])/2;
						int centerX = left[1] + raduis;
						int centerY = top[0] + raduis;
						if (isCircleInLines(top, bottom, left, right, singles, raduis+1, centerY, centerX)) {
							if (raduis > maxRad) maxRad = raduis;
						}
//						if (centerEndX - centerStartX == centerEndY - centerStartY) {
//							int centerY = centerStartY + raduis;
//							int centerX = centerStartX + (centerEndX - centerStartX)/2;
//						}
					}
				}
			}
		}

		System.out.println("Returned Radius " + maxRad);
		return maxRad;
	}

	private boolean isCircleInLines(int[] top, int[] bottom, int[] left, int[] right, List<String> singles, int radius, int centerY, int centerX) {
		for (int i = 0; i < radius; i++) {
			double thisRad = Math.sqrt(radius*radius - i*i);
			if (centerY < thisRad || centerX < thisRad) return false;
			if (centerY < i || centerX < i) return false;
			int minusX = (int)Math.ceil((double)centerX - thisRad);		//  + (i == 0 ? 1 : 0)
			int minusX1 = (thisRad % 0.5 == 0.0 ? minusX+1 : minusX);
			int minusXi = (int)Math.ceil((double)centerX - i);
			int plusX = (int)Math.ceil((double)centerX + thisRad);
			int plusXi = (int)Math.ceil((double)centerX + i);
			int minusY = (int)Math.ceil((double)centerY - thisRad);		//  + (i == 0 ? 1 : 0)
			int minusY1 = (thisRad % 0.5 == 0.0 ? minusY+1 : minusY);
			int minusYi = (int)Math.ceil((double)centerY - i);
			int plusY = (int)Math.ceil((double)centerY + thisRad);
			int plusYi = (int)Math.ceil((double)centerY + i);

			int found = 0;
			if (i > radius/2) {
				minusYi++;
				minusXi++;
				plusY++;
				plusX++;
				if (i > radius/2+1) {
					if (singles.contains(plusY+","+plusXi))found++;
					if (singles.contains(minusY+","+minusXi))found++;
					if (singles.contains(plusYi+","+minusX))found++;
					if (singles.contains(minusYi+","+plusX))found++;
				}
			}
			if ((minusX == left[1] || minusX1 == left[1]) && minusYi >= left[0] && minusYi <= left[2]) {
				//  left centerX-i and centerX-thisRad
				found++;
			}
			if ((minusY == top[0] || minusY1 == top[0]) && minusXi >= top[1] && minusXi <= top[3]) {
				//  top centerY+i and centerY+thisRad
				found++;
			}
			if (plusX == right[1] && plusYi >= right[0] && plusYi <= right[2]) {
				//  right centerX+i and centerX+thisRad
				found++;
			}
			if (plusY == bottom[0] && plusXi >= bottom[1] && plusXi <= bottom[3]) {
				//  bottom centerY-i and centerY-thisRad
				found++;
			}
			if (found<4) return false; 
		}
		return true;
	}
	private boolean isCircleInRectangle(List<int[]> rectangles, int maxRadius, int centerY, int centerX) {
		// r = SQRT(x**2+y**2) => SQRT(r**2-x**2) = y
		for (int i = 0; i <= maxRadius; i++) {
			double y = Math.sqrt(maxRadius*maxRadius - i*i);
			int minusX = (int)Math.ceil((double)centerX - y);
			int plusX = (int)Math.ceil((double)centerX + y);
			int minusY = (int)Math.ceil((double)centerY - y);
			int plusY = (int)Math.ceil((double)centerY + y);
			int plusYi = (int)Math.ceil((double)centerY + i);
//			System.out.println(i + "," + y + " ("+plusY + "," + plusX+")("+minusY + "," + plusX+")("+plusY + "," + minusX+")("+minusY + "," + minusX+")");
			if (minusY < 0 || minusX < 0) {
				return false;
			}
			boolean found = false;
			for (int[] r : rectangles) {
				if (plusY >= r[0] && plusY <= r[2] && plusX >= r[1] && plusX <= r[3]) {
					found = true;
					break;
				}
			}
			if (!found) return false; 
		}
//		System.out.println("For radius " + maxRadius + " Center (" + centerY + "," + centerX + ")");
		return true;
	}

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/topcoder/datascience/LargestCircle.dat"));
			try {
			    String line = br.readLine();
			    StringBuffer sb = new StringBuffer();
			    int lineNumber = 0;
			    sb.append("    ");
			    for (int i = 0; i < line.length(); i++) sb.append(i%10);
			    sb.append(System.lineSeparator());

				List<String> readMap = new ArrayList<>();
			    while (line != null) {
			        if (line.equals("")) {
						System.out.print(sb.toString());
						LargestCircle lc = new LargestCircle();
//						System.out.println("Answer: " + lc.radiusO4(readMap.toArray(new String[0])));
						lc.radius(readMap.toArray(new String[0]));
//						lc.isClear(readMap.toArray(new String[0]), 4, 6, 8);
						readMap.clear();
						lineNumber = 0;
				        line = br.readLine();
				        if (line != null && line.length()>0) {
					        sb = new StringBuffer();
						    sb.append("    ");
						    for (int i = 0; i < line.length(); i++) sb.append(i%10);
						    sb.append(System.lineSeparator());
				        }
			        }
			        if (lineNumber/10 == 0) sb.append(" ");
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
