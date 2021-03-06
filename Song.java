package Models;
/*
 * Created By:
 * Rohan Joshi: rj408
 * Nicholas Cheniara: njc129
 */

public class Song {
    private String id;
    private String name;
    private String artist;
    private String album;
    private String year;

    public Song(String name, String artist, String album, String year) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
    public String toString() {
    	return "Name: "+name+" Artist: "+artist+" Album: "+album+" Year: "+year;
    }
}