package com.example.gamelogic;

public class Music {
	String name;
	String artist;
	String path;
	public Music(String name, String artist,String path) {
		this.name = name;
		this.artist = artist;
		this.path = path;
	}
	
	public String getArtist(){
		return this.artist;
	}
	public String getName(){
		return this.name;
	}
	
	public String getPath() {
		return this.path;
	}
	
}
