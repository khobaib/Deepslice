package com.deepslice.utilities;

import java.util.Comparator;

import com.deepslice.model.LocationPoints;

public class DistanceComparator implements Comparator<LocationPoints> {
	public int compare(LocationPoints o1, LocationPoints o2) {
		
		
		if (o1.getDistance() < o2.getDistance()) {
			return -1;
		} else if (o1.getDistance() > o2.getDistance()) {
			return 1;
		} else {
			return 0;
		}
	}
}
