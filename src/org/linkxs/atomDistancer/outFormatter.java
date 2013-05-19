package org.linkxs.atomDistancer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class outFormatter {
	public String outFile;
	public String inp;
	
	ArrayList<String> process;

	Scanner inpStr = null;
	
	public outFormatter(String outFile) {
		this.outFile = outFile;
		process = new ArrayList<String>();
		
		try {
			inpStr = new Scanner(new FileInputStream(outFile));
			System.out.println(">   Loading output file for formatting...");
		} catch (FileNotFoundException e) {
			System.out.println("!!! File " + outFile + " not found. \n"
					+ "    Exiting. ");
		}
		
		while (inpStr.hasNext()) {
			inp = inpStr.nextLine();
			if (inp.equals("0   0    N/A 0 0")) {
				System.out.println("Found a trash line in output file. Skipping");
			} else if (inp.equals("")) {
				System.out.println("Found an empty line in output file. Skipping");
			} else if (inp.contentEquals("0 0")) {
				this.out(inp + " Atom not found");
			}
			
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
		
	}
	
	public void out(String it) {
		
	}
}
