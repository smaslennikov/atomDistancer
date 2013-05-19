/*
 * File Name:          atomDistancerDriver.java
 * Programmer:         Slava Maslennikov
 *
 * Overall Plan:
 * 1. import necessary classes: 
 *  a. IO used for file reading/buffering/writing
 *  b. util used for Scanner, used input, etc. 
 *  c. text used for number formatting for output
 * 2. create variables:
 *  a. Strings: 
 * 	i.  outFile: stores name of the output file
 * 	ii. pdb: stores pdb filename
 * 	iii.lol: stores lol filename
 * 	iv. upl: stores upl filename
 * 	v.  inp: used for temporary input storage, as well as for some buffer 
 * 		processing
 * 	vi. lolimd: stores lower limit double, but as a String
 * 	vii.uplimd: stores upper limit double, but as a String
 * 	iix.res1: stores name of the first segment/residue out of lol/upl file
 * 	ix. res2: stores name of the second segment/residue out of lol/upl file
 * 	x.  atom1: stores name of the first atom out of the lol/upl file
 * 	xi. atom2: stores name of the second atom out of the lol/upl file
 * 	xii.temp: is used for temporary/trash storage, sometimes to dump a line from
 * 		the buffer. 
 *  b. Integers: 
 * 	i.  y: used for loop control in multiple loops
 * 	ii. x: used for loop control in multiple loops
 * 	iii.lolDists: amount of entries (minus comments) in the LOL file
 * 	iv. uplDists: amount of entries (minus comments) in the UPL file
 * 	v.  pdbAtoms: amount of atom entries in the PDB file
 * 	vi. res1Num: stores the number of the first residue
 * 	vii.res2Num: stores the number of the second residue
 * 	iix.i: used for fake loops
 *  c. String arrays:
 * 	i.  atom: 2D array that stores ALL the atoms found in the PDB file given. 
 * 	ii. uplim: stores upper limits given in the UPL file
 * 	iii.lolim: stores lower limits give in the LOL file
 *  d. Doubles:
 * 	i.  dist: stores the distance between two given atoms. We get it from 
 * 		atomDistancer.calcDist as a double. 
 *  e. Booleans:
 * 	i.  found: stores whether an upper or lower limit has been found
 *  f. Objects:
 * 	i.  Scanner kbd: used for user input through keyboard. 
 * 	ii. Scanner inpStrPdb: used for PDB file buffer. 
 * 	iii.Scanner inpStrLol: used for LOL file buffer.
 * 	iv. Scanner inpStrUpl: used for UPL file buffer. 
 * 	v.  PrintWriter outStrNC: used for output stream buffer (NC = no check for
 * 		duplicates, which wasn't requested)
 * 	vi. NumberFormat df: makes sure there are only two decimal places for output
 * 2. prompt for file names
 *  a. store each in its corresponding variable
 * 3. store the data that we'll be using later in corresponding arrays
 * 4. run through the LOL file
 *  a. find distance for each pair of atoms of given residues. 
 *  b. find out if the pair exists in the UPL list
 *  c. if the upper limit exists, throw everything to the write method
 *  d. if it doesn't, use N/A in its place. 
 * 5. do the same for the UPL file
 * 6. prompt the user whether they want to run the program again with different 
 * 	files
 * 7. write method: 
 *  a. inputs all the output: residues' names, numbers, atoms' names, lower 
 * 	and upper limits, and lastly - actual distance. 
 *  b. open an output stream, write to it, close it. 
 *  c. if something goes wrong, call the outToScreen method
 * 8. outToScreen method: just output the given line to screen. 
 * 9. DONE!	
 */
package org.linkxs.atomDistancer;

import java.io.*; // import the necessary classes
import java.util.*;
import java.text.*;

class atomDistancerDriver {
	static String	outFile	= "", inp;	// create the needed global variables
	static int		y		= 0;		// their function is described above
										
	static void letsDoThis() { // the functions of the following variables are
								// described above in a
								// descriptive (and lengthy) manner.
		String pdb = "", lol = "", upl = "", lolimd = "", uplimd = "";
		String res1 = "", res2 = "", atom1 = "", atom2 = "", temp;
		String[][][] atom;
		String[][] uplim, lolim;
		int x = 0, lolDists = 0, uplDists = 0, pdbAtoms = 0, i = 0, tempint = 0;
		int pdbAtomsFin = 0, currentModel = 0;
		int res1Num = 0, res2Num = 0, pdbMdls = 0;
		double dist = 0.0, avg = 0.0, stDev = 0.0, stDevTemp = 0.0;
		double totalDist = 0.0;
		double[] dists;
		Scanner kbd = new Scanner(System.in);
		Scanner inpStrPdb = null, inpStrLol = null, inpStrUpl = null;
		boolean found = false;
		// this huge while loop makes sure we can start over the program in the
		// end, if the user wants to.
		while (x == 0) {
			y = 0;
			stDev = 0.0;
			stDevTemp = 0.0;
			lolDists = 0; // RESET EVERYTHING. Takes lots of space..
			uplDists = 0; // and typing fairies.
			pdbAtoms = 0;
			pdbMdls = 0;
			totalDist = 0.0;
			avg = 0.0;
			pdb = "";
			lol = "";
			upl = "";
			lolimd = "";
			uplimd = "";
			res1 = "";
			res2 = ""; // they say that every time you make a for each loop,
			atom1 = ""; // a typing fairy dies.
			atom2 = "";
			temp = "";
			outFile = "";
			inp = "";
			y = 0;
			x = 0;
			res1Num = 0;
			res2Num = 0; // they also say that every time you finish a line of
			dist = 0.0; // code, a typing fairy looks in your general direction
			inpStrPdb = null; // and turns into a smurf.
			inpStrLol = null;
			inpStrUpl = null; // a flying, shiny smurf with a stick.
			found = false; // and facial hair.
			while (y == 0) // this while loop makes sure we get a PDB filename
			{ // from the user
				System.out.print("?   Please enter PDB file name: ");
				pdb = kbd.nextLine(); // input the filename
				try {
					inpStrPdb = new Scanner(new FileInputStream(pdb + ".pdb"));
					System.out.println(">   Loading PDB...");
					y = 1; // let the user know what's happening if successfull
				} // End the loop.
				catch (FileNotFoundException e) {
					System.out.println("!!! File " + pdb + ".pdb not found. \n"
							+ "    Try again."); // If file isn't there, ask for
													// it again.
				} // Until it IS there.
			}
			y = 0; // reset the loopy variable.
			while (y == 0) // same thing, but this time for the LOL filename
			{
				System.out.print("?   Please enter LOL file name: ");
				lol = kbd.nextLine();
				try {
					inpStrLol = new Scanner(new FileInputStream(lol + ".lol"));
					System.out.println(">   Loading LOL...");
					y = 1;
				} catch (FileNotFoundException e) {
					System.out.println("!!! File " + lol + ".lol not found. \n"
							+ "    Try again. ");
				}
			}
			y = 0;
			while (y == 0) // and once more for UPl.
			{
				System.out.print("?   Please enter UPL file name: ");
				upl = kbd.nextLine();
				try {
					inpStrUpl = new Scanner(new FileInputStream(upl + ".upl"));
					System.out.println(">   Loading UPL...");
					y = 1;
				} catch (FileNotFoundException e) {
					System.out.println("!!! File " + upl + ".upl not found. \n"
							+ "    Try again. ");
				}
			}
			System.out.print("?   Please enter output file name: ");
			outFile = kbd.nextLine(); // lastly, get the output filename
			System.out.println(">   Successfully acquired files.");
			System.out.println(">   Scanning files for data\n");
			scanPdb: while (inpStrPdb.hasNext()) {
				temp = inpStrPdb.next();
				if (temp.equals("MODEL"))
					pdbMdls++;
				else if (temp.equals("ATOM"))
					pdbAtoms++;
				else if (temp.equals("TER")) {
					if (pdbAtoms > pdbAtomsFin || pdbAtomsFin == 0)
						pdbAtomsFin = pdbAtoms;
					pdbAtoms = 0;
				}
				temp = inpStrPdb.nextLine();
			}
			System.out.println(">   " + pdb + ".pdb has " + pdbMdls
					+ " models and maximum " + pdbAtomsFin
					+ " atoms per model listed.");
			while (inpStrLol.hasNextLine()) {
				if (!inpStrLol.nextLine().substring(0, 1).equals("#"))
					lolDists++;
			}
			System.out.println(">   " + lol + ".lol has " + lolDists
					+ " lower limit distances for atoms. ");
			while (inpStrUpl.hasNext()) {
				if (!inpStrUpl.next().equals("#"))
					uplDists++;
				temp = inpStrUpl.nextLine();
			}
			System.out.println(">   " + upl + ".upl has " + uplDists
					+ " upper limit distances for atoms. ");
			atom = new String[pdbMdls][pdbAtomsFin + 1][6];
			uplim = new String[uplDists][7];
			lolim = new String[lolDists][7];
			inpStrPdb = null;
			inpStrLol = null;
			inpStrUpl = null;
			try {
				inpStrPdb = new Scanner(new FileInputStream(pdb + ".pdb"));
				inpStrLol = new Scanner(new FileInputStream(lol + ".lol"));
				inpStrUpl = new Scanner(new FileInputStream(upl + ".upl"));
			} catch (FileNotFoundException e) {
				System.out
						.println("!!! Error reloading either pdb, lol, or "
								+ "upl file. Make sure it still exists and is readable, and "
								+ "then restart. ");
				break;
			}
			i = 0;
			currentModel = 0;
			while (inpStrPdb.hasNext()) {
				temp = inpStrPdb.next();
				if (temp.equals("MODEL")) {
					currentModel = (inpStrPdb.nextInt() - 1);
					i = 0;
				} else if (temp.equals("ATOM")) {
					atom[currentModel][i][2] = inpStrPdb.next();
					atom[currentModel][i][2] = inpStrPdb.next();
					if (atom[currentModel][i][2].equals("H"))
						atom[currentModel][i][2] = "HN";
					atom[currentModel][i][1] = inpStrPdb.next();
					atom[currentModel][i][0] = inpStrPdb.next();
					atom[currentModel][i][3] = inpStrPdb.next();
					atom[currentModel][i][4] = inpStrPdb.next();
					atom[currentModel][i][5] = inpStrPdb.next();
					i++;
				}
				temp = inpStrPdb.nextLine();
				// System.out.println(currentModel + " " + i + " "
				// + atom[currentModel][i][2] + " " + atom[currentModel][i][1]
				// + " " + atom[currentModel][i][0] + " "
				// + atom[currentModel][i][3] + " " + atom[currentModel][i][4]
				// + " " + atom[currentModel][i][5]);
			}
			
			i = 0;
			while (inpStrUpl.hasNext()) {
				inp = inpStrUpl.next();
				if (inp.indexOf('#') == -1) {
					uplim[i][0] = inp;
					uplim[i][1] = inpStrUpl.next();
					uplim[i][2] = inpStrUpl.next();
					uplim[i][3] = inpStrUpl.next();
					uplim[i][4] = inpStrUpl.next();
					uplim[i][5] = inpStrUpl.next();
					uplim[i][6] = inpStrUpl.next();
					i++;
				}
				inp = inpStrUpl.nextLine();
			}
			i = 0; // reset the increment variable
			while (inpStrLol.hasNext()) // same thing here, but with LOL.
			{
				inp = inpStrLol.next();
				if (inp.indexOf('#') == -1) {
					lolim[i][0] = inp;
					lolim[i][1] = inpStrLol.next();
					lolim[i][2] = inpStrLol.next();
					lolim[i][3] = inpStrLol.next();
					lolim[i][4] = inpStrLol.next();
					lolim[i][5] = inpStrLol.next();
					lolim[i][6] = inpStrLol.next();
					i++;
				}
				inp = inpStrLol.nextLine();
			}
			i = 0;
			inpStrLol = null;
			inpStrUpl = null;
			try {
				inpStrLol = new Scanner(new FileInputStream(lol + ".lol"));
				inpStrUpl = new Scanner(new FileInputStream(upl + ".upl"));
			} catch (FileNotFoundException e) {
				System.out
						.println("!!! Error reloading either lol, or "
								+ "upl file. Make sure it still exists and is readable, and "
								+ "then restart. ");
				break;
			}
			
			dists = new double[pdbMdls + 1];
			while (inpStrLol.hasNext()) {
				inp = inpStrLol.next();
				if (inp.indexOf('#') == -1) {
					res1Num = Integer.parseInt(inp);
					res1 = inpStrLol.next();
					atom1 = inpStrLol.next();
					res2Num = inpStrLol.nextInt();
					res2 = inpStrLol.next();
					atom2 = inpStrLol.next();
					lolimd = inpStrLol.next();
					for (int j = 0; j < pdbMdls; j++) {
						dists[j] = atomDistancer.calcDist(j, atom, res1Num,
								res1, atom1, res2Num, res2, atom2);
						totalDist += dists[j];
						// System.out.println(j+ " " + dists[j] + " " +
						// totalDist);
					}
					avg = totalDist / pdbMdls;
					for (int j = 0; j < pdbMdls; j++) {
						stDevTemp += Math.pow((dists[j] - avg), 2);
					}
					stDev = (Math.sqrt((stDevTemp) / (pdbMdls + 1)));
					search: for (int j = 0; j < uplim.length; j++) {
						while (Integer.parseInt(uplim[j][0]) == res1Num) {
							if (Integer.parseInt(uplim[j][3]) == res2Num) {
								if (!uplim[j][1].equals(res1)
										|| !uplim[j][4].equals(res2))
									System.out
											.println("!!! Residue "
													+ "number doesn't match the name in the"
													+ " pdb file. Will attempt to continue.");
								else {
									write(res1Num, res1, atom1, res2Num, res2,
											atom2, lolimd, uplim[j][6], avg,
											stDev);
									found = true;
									break search;
								}
							} else
								j++;
							if (j > uplim.length - 1)
								break search;
						}
					}
				}
				if (!found)
					write(res1Num, res1, atom1, res2Num, res2, atom2, lolimd,
							"N/A", avg, stDev);
				inp = inpStrLol.nextLine();
				found = false;
				stDev = 0.0;
				stDevTemp = 0.0;
				avg = 0.0;
				totalDist = 0.0;
			}
			found = false;
			stDev = 0.0;
			stDevTemp = 0.0;
			avg = 0.0;
			totalDist = 0.0;
			while (inpStrUpl.hasNext()) {
				inp = inpStrUpl.next();
				if (inp.indexOf('#') == -1) {
					res1Num = Integer.parseInt(inp);
					res1 = inpStrUpl.next();
					atom1 = inpStrUpl.next();
					res2Num = inpStrUpl.nextInt();
					res2 = inpStrUpl.next();
					atom2 = inpStrUpl.next();
					uplimd = inpStrUpl.next();
					for (int j = 0; j < pdbMdls; j++) {
						dists[j] = atomDistancer.calcDist(j, atom, res1Num,
								res1, atom1, res2Num, res2, atom2);
						totalDist += dists[j];
					}
					avg = totalDist / pdbMdls;
					for (int j = 0; j < pdbMdls; j++) {
						stDevTemp += Math.pow((dists[j] - avg), 2);
					}
					stDev = (Math.sqrt((stDevTemp) / (pdbMdls + 1)));
					search2: for (int j = 0; j < lolim.length; j++) {
						while (Integer.parseInt(lolim[j][0]) == res1Num) {
							if (Integer.parseInt(lolim[j][3]) == res2Num) {
								if (!lolim[j][1].equals(res1)
										|| !lolim[j][4].equals(res2))
									System.out
											.println("!!! Residue "
													+ "number doesn't match the name in the"
													+ " pdb file. Will attempt to continue.");
								else {
									write(res1Num, res1, atom1, res2Num, res2,
											atom2, lolim[j][6], uplimd, avg,
											stDev);
									found = true;
									break search2;
								}
							} else
								j++;
							if (j > lolim.length - 1)
								break search2;
						}
					}
				}
				if (!found)
					write(res1Num, res1, atom1, res2Num, res2, atom2, "N/A",
							uplimd, avg, stDev);
				inp = inpStrUpl.nextLine();
				found = false;
				stDev = 0.0;
				stDevTemp = 0.0;
				avg = 0.0;
				totalDist = 0.0;
			}
			System.out.println(">   Exiting! Run again? (Y/N)");
			inp = kbd.nextLine();
			if (inp.equals("n") || inp.equals("N"))
				x = 1;
		}
		inpStrPdb.close();
		inpStrLol.close();
		inpStrUpl.close();
		
		outFormatter format = new outFormatter(outFile);
	}
	
	static void write(int r1n, String r1, String a1, int r2n, String r2,
			String a2, String lolim, String uplim, double avg, double stDev) {
		y = 0;
		Scanner kbd = new Scanner(System.in);
		PrintWriter outStr = null;
		NumberFormat df = new DecimalFormat("#########.##");
		while (y == 0) {
			try {
				outStr = new PrintWriter(new FileOutputStream(outFile, true));
				y = 1;
			} catch (FileNotFoundException e) {
				System.out.println("!!! File " + outFile + " not found. \n"
						+ "Would you like to try again? (Y/N)");
				inp = kbd.nextLine();
				if (inp.equals("n") || inp.equals("N")) {
					y = 1;
					System.out.println(">   Outputting to screen..");
					outToScreen(r1n, r1, a1, r2n, r2, a2, lolim, uplim, avg,
							stDev);
				}
			}
		}
		outStr.println(r1n + " " + r1 + " " + a1 + " " + r2n + " " + r2 + " "
				+ a2 + " " + lolim + " " + uplim + " " + df.format(avg) + " "
				+ df.format(stDev));
		outStr.close();
	}
	
	static void outToScreen(int r1n, String r1, String a1, int r2n, String r2,
			String a2, String lolim, String uplim, double avg, double stDev) {
		NumberFormat df = new DecimalFormat("#########.##");
		System.out.println(r1n + " " + r1 + " " + a1 + " " + r2n + " " + r2
				+ " " + a2 + " " + lolim + " " + uplim + " " + df.format(avg)
				+ " " + df.format(stDev));
	}
}
