package com.cburch;

import java.util.ArrayList;

import com.cburch.logisim.data.Location;

public class ElementException {
	private ArrayList<Integer> widths;
	private ArrayList<Location> locations;
	
	public ElementException() {
		widths = new ArrayList<Integer>();
		locations= new ArrayList<Location>();
	}

	public void add(int x){
		widths.add(x);
	}
	
	public void add(Location x){
		locations.add(x);
	}
	
	public ArrayList<Integer> getWidths() {
		return widths;
	}
	
	public ArrayList<Location> getLocations() {
		return locations;
	}
}
