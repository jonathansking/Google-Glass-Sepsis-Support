package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayDeque;

public class Global {
	
	public static final int maxRecentPatients = 5; 
    public static ArrayDeque<Patient> recentPatients = new ArrayDeque<Patient>();
    public static boolean once = true; // needed until scanner works, recentpatient
    
    // adds patient to Queue, maintaining a max
    public static void pushRecentPatient(String id, String name) {
    	if( recentPatients.size() == maxRecentPatients )
    		recentPatients.removeLast();
    	recentPatients.addFirst( new Patient(id, name) );
    }
    
    public static void printPatients() {
    	for( Patient p : recentPatients ) {
    		System.out.println( p );
    	}
    }
    
}
