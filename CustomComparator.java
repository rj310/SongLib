package Models;
/*
 * Created By:
 * Rohan Joshi: rj408
 * Nicholas Cheniara: njc129
 */

import java.util.Comparator;

public class CustomComparator implements Comparator<Song> {
    @Override
    public int compare(Song o1, Song o2) {
    	if(o1.getName().toLowerCase().equals(o2.getName().toLowerCase())) {
    		return o1.getArtist().toLowerCase().compareTo(o2.getArtist().toLowerCase());
    	}
    	else {
    		return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
    	}
    }
    

}
