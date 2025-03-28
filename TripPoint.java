import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Has multiple setters and getters to manipulate the private fields.
 * Reads trip.csv files to then calculate haversine distances.
 * Added two different ways to detect stops and creates a new movingTrip array.
 * 
 * @author Bryan Ho
 * @version 1.1
 */
public class TripPoint {
	private double lat;
	private double lon;
	private int time;
	private static ArrayList<TripPoint> trip;
	private static ArrayList<TripPoint> movingTrip;
	
	/**
	 * Constructor for the TripPoint object
	 * 
	 * @param time --> this is the time stamp for the coordinates
	 * @param lat  --> Latitude value
	 * @param lon  --> Longitude Value
	 */
	TripPoint(int time, double lat, double lon){
		this.lat = lat;
		this.lon = lon;
		this.time = time;
	}
	
	/**
	 * ints are mutable, this getter returns a copy to
	 * maintain encapsulation.
	 * 
	 * @return --> gives back a copy of time
	 */
	public int getTime() {
		int copyOfTime = time;
		return copyOfTime;
	}
	
	/**
	 * doubles are mutable, this getter returns a copy to
	 * maintain encapsulation.
	 * 
	 * @return --> gives back a copy of latitude is given
	 */
	public double getLat() {
		double copyOfLat = lat;
		return copyOfLat;
	}
	
	/**
	 * doubles are mutable, this getter returns a copy to
	 * maintain encapsulation.
	 * 
	 * @return --> gives back a copy of longitude
	 */
	public double getLon() {
		double copyOfLon = lon;
		return copyOfLon;
	}
	
	/**
	 * Creates a copy of the <TripPoint> trip array to
	 * maintain encapsulation, no memory addresses are
	 * given away by calling this method.
	 * 
	 * @return -> gives back a copy of the trip
	 */
	public static ArrayList<TripPoint> getTrip(){
		
		ArrayList<TripPoint> copyOfTrip = new ArrayList<>();
		
		for(TripPoint tripPointObject: trip) {
			copyOfTrip.add(new TripPoint(tripPointObject.getTime(), 
										 tripPointObject.getLat(),
										 tripPointObject.getLon()));
		}
		
		return trip;
	}

	/**
	 * Creates a copy of the <TripPoint> movingTrip array to
	 * maintain encapsulation, no memory addresses are
	 * given away by calling this method.
	 * 
	 * @return -> gives back a copy of the movingTrip
	 */
    public static ArrayList<TripPoint> getMovingTrip() {
    	ArrayList<TripPoint> copyOfMovingTrip = new ArrayList<>();
		
		for(TripPoint tripPointObject: movingTrip) {
			copyOfMovingTrip.add(new TripPoint(tripPointObject.getTime(), 
										 	   tripPointObject.getLat(),
										 	   tripPointObject.getLon()));
		}
		
		return copyOfMovingTrip;
	}
	
	/**
	 * Uses TripPoints to pull out lon and lat. Then converts from degrees to radians,
	 * then inputs them into the haversine distance formula.
	 * 
	 * @return --> gives back the arc distance in km
	 */
	public static double haversineDistance(TripPoint a, TripPoint b) {
		
		double latA = Math.toRadians(a.getLat());
	    double latB = Math.toRadians(b.getLat());
	    double deltaLat = latB - latA;
	    double deltaLon = Math.toRadians(b.getLon() - a.getLon());
		
	    double haversineLat = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2);
	    double haversineLon = Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
		
		double internalDistance = haversineLat + Math.cos(latA) * Math.cos(latB) * haversineLon;
		
		double radiusOfEarth = 6371.0; //in km
		
		double distance = 2 * radiusOfEarth * Math.asin(Math.sqrt(internalDistance));
		
	    return distance;
	}
	
	/**
	 * Calculates the average speed using distance/time in hours.
	 * Uses Math.abs to work in any TripPoint order.
	 * 
	 * @param a --> any TripPoint
	 * @param b --> any TripPoint
	 * @return  --> gives back the avgspeed in km/hr
	 */
	public static double avgSpeed(TripPoint a, TripPoint b) {
		
		double distance = Math.abs(haversineDistance(a, b));
		double time = Math.abs((a.getTime() - b.getTime()) / 60.0);
		
		return distance/(time * 1.0);
	}
	
    /**
     * Calculates the average moving speed by calculating the total moving distance first.
     * 
     * @return --> average speed while moving as a double in km/hr
     */
    public static double avgMovingSpeed() {
    	
    	double totalMovingDistance = 0;
    	TripPoint previousPoint = null;
    	
    	for(TripPoint tp: movingTrip) {
    		if(previousPoint == null) {
    			previousPoint = tp;
    			continue;
    		}
    		totalMovingDistance += haversineDistance(previousPoint, tp);	
    		previousPoint = tp;
    	}
    	
    	return totalMovingDistance/movingTime();
    }
	
	/**
	 * totalTime() adds all the numbers up from the trip and divides by 60.0
	 * to return a proper double for the total time.
	 * 
	 * @return --> total time as a double
	 */
	public static double totalTime() {
		return trip.get(trip.size() - 1).getTime()/60.0;
	}
	
    /**
     * Calculates the total moving time.
     * 
     * @return --> the time spent moving as a double
     */
    public static double movingTime() {
    	return ((movingTrip.size() - 1) * 5) / 60.0;
    }

    /**
     * Calculates the total stopped time by taking the difference between tripTime and movingTime
     * 
     * @return --> time spent stopped as a double
     */
    public static double stoppedTime() {
    	double time = totalTime() - movingTime();
    	return time;
    }
    
	/**
	 * Loops through the trip to get all points, then calls haversineD
	 * to get all the distances, then finally rounds at the end
	 * 
	 * @return --> gives back the properly rounded total distance
	 */
	public static double totalDistance() {
		
		ArrayList<TripPoint> recentTrip = getTrip();
		
		double totalDistance = 0;
		
		TripPoint previousPoint = null;
		
		for(TripPoint tp: recentTrip) {
			if(previousPoint == null) {
				previousPoint = tp;
				continue;
			}
			totalDistance += haversineDistance(previousPoint, tp);
			previousPoint = tp;
		}
		
		return totalDistance;
	}
	
	/**
	 * This method is what creates the actual "trip" ArrayList.
	 * First it reads the file, then it will assign the values to the 
	 * correct variables for the class by calling the TripPoint() method.
	 * 
	 * 
	 * {@value} parsedTime --> Extracted time from the file reader
	 * {@value} parsedLat  --> Extracted Latitude from the file reader
	 * {@value} parsedLon  --> Extracted Longitude from the file reader
	 * 
	 * @param   filename   --> name of the file being read "triplog.csv"
	 * @throws Exception 
	 */
	public static void readFile(String filename) throws IOException, FileNotFoundException {
		try {
			File fileFound = new File(filename);
			FileReader fileBeingRead = new FileReader(fileFound);
			BufferedReader lineBeingRead = new BufferedReader(fileBeingRead);
			
			String line = lineBeingRead.readLine();
			
			trip = new ArrayList<>();
			
			while((line = lineBeingRead.readLine()) != null) {
				String[] splitLine = line.split(",");
				
				int parsedTime = Integer.parseInt(splitLine[0]); 
				double parsedLat = Double.parseDouble(splitLine[1]);
				double parsedLon = Double.parseDouble(splitLine[2]);
				
				trip.add(new TripPoint(parsedTime, parsedLat, parsedLon));
			}
			
			lineBeingRead.close();
			fileBeingRead.close();
			
		} catch (Exception e) {
			System.out.println("Could not find file!");
			throw e;
		}
	}
	
    /**
     * Detects stops using method h1 by checking the distance between each point in the trip array.
     * Then uses a 0.6km (inclusive) threshold to filter out stops.
     * 
     * @return --> number of stops using the h1 method
     */
    public static int h1StopDetection() {
    	
    	movingTrip = new ArrayList<>();
    	boolean first = true;
    	TripPoint previousPoint = null;
    	int count = 0;
    	
    	for(TripPoint tp: trip) {
    		
    		if(first) {
    			first = false;
    		} else if(haversineDistance(previousPoint, tp) <= 0.6) {
    			count++;
    			continue;
    		}
    			
    		movingTrip.add(tp);
    		previousPoint = tp;
    		
    	}
    	
    	return count;
    }

    /**
     * Detects stops using method h2 by creating a streak of stops array. If that array reaches 3 stops or more,
     * the points in the array are not added to the movingTrip. If the streak is broken before 3, then all points in the array are added.
     * 
     * @return --> number of stops using the h2 method 
     */
    public static int h2StopDetection() {
    	
    	movingTrip = new ArrayList<>();
    	TripPoint previousPoint = trip.get(0);
    	int stops = 0;
    	ArrayList<TripPoint> currentStreak = new ArrayList<>();
    	
    	for(TripPoint tp: trip) {
    		
    		if(haversineDistance(previousPoint, tp) <= 0.51) {
    			currentStreak.add(tp);
    			
    		} else {
    			// Streak is broken
    			if (currentStreak.size() >= 3) {
    				// It was a stop, count it but don't add points to movingTrip
    				stops += currentStreak.size();
    			} else {
    				// Wasn't a stop, add all points from the streak to movingTrip
    				for(TripPoint point : currentStreak) {
    						movingTrip.add(point);
    				}
    			}
    			// Start new streak with current point
    			currentStreak.clear();
    			currentStreak.add(tp);
    		}
    		
    		
    		previousPoint = tp;
    	}
    	
    	// Handle the final streak
    	if(currentStreak.size() >= 3) {
    		stops += currentStreak.size();
    	} else {
    		// Add remaining points if not a stop
    		for(TripPoint point : currentStreak) {
    			if(!movingTrip.contains(point)) {
    				movingTrip.add(point);
    			}
    		}
    	}
    	
    	return stops;
    }
    public static void main (String[] args) throws Exception {
    	TripPoint.readFile("triplog.csv");
    	System.out.println(h1StopDetection());
    	System.out.println(h2StopDetection());
    	System.out.println(haversineDistance(32.823532,-106.271736,32.82357,-106.271736));
    	// Print all stats for testing
    	printTripStats();
    }
    
    /**
     * Calculates the haversine distance between two sets of coordinates
     * Implementation that takes double parameters directly for coordinates
     * 
     * @param lata - Latitude of first point
     * @param lona - Longitude of first point
     * @param latb - Latitude of second point
     * @param lonb - Longitude of second point
     * @return - Distance in kilometers
     */
    private static double haversineDistance(double lata, double lona, double latb, double lonb) {
        double latA = Math.toRadians(lata);
        double latB = Math.toRadians(latb);
        double deltaLat = latB - latA;
        double deltaLon = Math.toRadians(lonb - lona);
        
        double haversineLat = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2);
        double haversineLon = Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        
        double internalDistance = haversineLat + Math.cos(latA) * Math.cos(latB) * haversineLon;
        
        double radiusOfEarth = 6371.0; //in km
        
        double distance = 2 * radiusOfEarth * Math.asin(Math.sqrt(internalDistance));
        
        return distance;
    }

	/**
     * Prints all trip statistics in a format suitable for JUnit tests.
     * Includes stop counts, moving points counts, distances, and times for both h1 and h2 methods.
     */
    public static void printTripStats() {
        System.out.println("===== TRIP STATISTICS =====");
        System.out.println("Total points: " + trip.size());
        
        // --- Run h1 and get its stats ---
        int h1Stops = h1StopDetection();
        // Save a copy of the movingTrip after h1
        ArrayList<TripPoint> h1MovingTripCopy = new ArrayList<>(movingTrip);
        
        // Calculate h1 stats
        double h1MovingDistance = calculateTotalDistance(h1MovingTripCopy);
        double h1MovingTime = calculateMovingTime(h1MovingTripCopy);
        double h1StoppedTime = totalTime() - h1MovingTime;
        double h1AvgSpeed = h1MovingDistance / h1MovingTime;
        
        // --- Run h2 and get its stats ---
        int h2Stops = h2StopDetection();
        // Save a copy of the movingTrip after h2
        ArrayList<TripPoint> h2MovingTripCopy = new ArrayList<>(movingTrip);
        
        // Calculate h2 stats
        double h2MovingDistance = calculateTotalDistance(h2MovingTripCopy);
        double h2MovingTime = calculateMovingTime(h2MovingTripCopy);
        double h2StoppedTime = totalTime() - h2MovingTime;
        double h2AvgSpeed = h2MovingDistance / h2MovingTime;
        
        // Print h1 stats
        System.out.println("\n----- h1 Stats (Displacement) -----");
        System.out.println("h1 Stop Points: " + h1Stops);
        System.out.println("h1 Moving Points: " + h1MovingTripCopy.size());
        System.out.println("h1 Moving Distance (km): " + String.format("%.2f", h1MovingDistance));
        System.out.println("h1 Moving Time (hr): " + String.format("%.2f", h1MovingTime));
        System.out.println("h1 Stopped Time (hr): " + String.format("%.2f", h1StoppedTime));
        System.out.println("h1 Average Moving Speed (km/hr): " + String.format("%.2f", h1AvgSpeed));
        
        // Print h2 stats
        System.out.println("\n----- h2 Stats (Proximity) -----");
        System.out.println("h2 Stop Points: " + h2Stops);
        System.out.println("h2 Moving Points: " + h2MovingTripCopy.size());
        System.out.println("h2 Moving Distance (km): " + String.format("%.2f", h2MovingDistance));
        System.out.println("h2 Moving Time (hr): " + String.format("%.2f", h2MovingTime));
        System.out.println("h2 Stopped Time (hr): " + String.format("%.2f", h2StoppedTime));
        System.out.println("h2 Average Moving Speed (km/hr): " + String.format("%.2f", h2AvgSpeed));
        
        System.out.println("\n===== END STATISTICS =====");
    }
    
    /**
     * Helper method to calculate total distance for a specific trip array
     */
    private static double calculateTotalDistance(ArrayList<TripPoint> tripArray) {
        double totalDistance = 0;
        TripPoint previousPoint = null;
        
        for(TripPoint tp: tripArray) {
            if(previousPoint == null) {
                previousPoint = tp;
                continue;
            }
            totalDistance += haversineDistance(previousPoint, tp);
            previousPoint = tp;
        }
        
        return totalDistance;
    }
    
    /**
     * Helper method to calculate moving time for a specific trip array
     */
    private static double calculateMovingTime(ArrayList<TripPoint> tripArray) {
        // Each point represents 5 minutes, but first point doesn't count as "movement"
        return ((tripArray.size() - 1) * 5) / 60.0;
    }
}
