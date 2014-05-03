package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayDeque;

public class Global 
{
	// Constants
	public static final String PATIENT_ID = "Patient info";
	public static final String ERROR_MSG = "Error message";
	public static final String OPTIONS_FILE = "options.sav";
	public static final String PATIENTS_FILE = "patients_file.sav";
	
	// recent patient queue
	public static int maxRecentPatients = 5; 
    public static ArrayDeque<Patient> recentPatients;
    
    // options
    public static OptionsActivity.Options options;
    
    // adds patient to Queue, maintaining a max, handles duplicates
    public static void pushRecentPatient(String id, String name) 
    {
    	Patient p = new Patient(id, name);
    	recentPatients.remove( p );
    	
    	if( recentPatients.size() >= maxRecentPatients )
    		recentPatients.removeLast();

		recentPatients.addFirst( p );
    }
    
    // debugging purposes
    public static void printPatients() 
    {
    	for( Patient p : recentPatients )
    		System.out.println( p );
    }
}
