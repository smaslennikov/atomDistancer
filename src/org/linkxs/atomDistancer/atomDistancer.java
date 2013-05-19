/*
 * File Name:          atomDistancer.java
 * Programmer:         Slava Maslennikov
 *
 * Overall Plan:
 * method calcDist takes in an array of atoms, residues' numbers and names, 
 * atoms' names, and returns a double. 
 * 
 * 1. create needed variables:
 * 	Doubles:
 * 	x1, x2 - store x coordinates of the atoms
 * 	y1, y2 - store y coordinates of the atoms
 * 	z1, z2 - store z coordinates of the atoms
 * 	dist - stores the final distance to be returned
 */
package org.linkxs.atomDistancer;

class atomDistancer {
	static double	x1, y1, z1, x2, y2, z2, dist;	// create the variables

	static double calcDist(int i, String[][][] atom, int res1Num, String res1,
			String atom1, int res2Num, String res2, String atom2) {
		x1 = 0; // reset the variables
		y1 = 0;
		z1 = 0;
		x2 = 0;
		y2 = 0;
		z2 = 0;
		for (int j = 0; j < atom[0].length; j++) { // running through the list
													// of atoms
			if (atom[i][j][0] != null) {
				if (res1Num == Integer.parseInt(atom[i][j][0])) {
					if (!atom[i][j][1].equals(res1))
						System.out
								.println("!!! Residue number doesn't match "
										+ "the name in the pdb file. Will attempt to continue. ");
					else { // if match,
						if (atom[i][j][2].equals(atom1)) {
							x1 = Double.parseDouble(atom[i][j][3]);
							y1 = Double.parseDouble(atom[i][j][4]);
							z1 = Double.parseDouble(atom[i][j][5]);
							// System.out.println(x1 + " " + y1 + " " + z1);
						}
					}
				} else if (res2Num == Integer.parseInt(atom[i][j][0])) {
					if (!atom[i][j][1].equals(res2))
						System.out
								.println("!!! Residue number doesn't match "
										+ "the name in the pdb file. Will attempt to continue. ");
					else {
						if (atom[i][j][2].equals(atom2)) {
							x2 = Double.parseDouble(atom[i][j][3]);
							y2 = Double.parseDouble(atom[i][j][4]);
							z2 = Double.parseDouble(atom[i][j][5]);
						}
					}
				}
			}
		}
		if ( (x1 == 0 && y1 == 0 && z1 == 0) || (x2 == 0 && y2 == 0 && z2 == 0)) {
			return 0;
		}
		dist = (Math.sqrt(Math.abs(Math.pow((x1 - x2), 2)
				+ Math.pow((y1 - y2), 2) + Math.pow((z1 - z2), 2))));
		return dist;
	}
}
