package github.yeori.agorithm.kmeans_demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import github.yeori.agorithm.kmeans_demo.Observation;

public class MapReader {

	private FileInputStream out;

	private int mapW, mapH;
	private List<Observation> data = new ArrayList<Observation>();
	public MapReader(File input) {
		try {
			out = new FileInputStream(input);
			init();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("no such file: " + input.getAbsolutePath(), e);
		}
	}
	private void init() {
		java.util.Properties prop = new java.util.Properties();
		try {
			prop.load( out );
			mapW = Integer.parseInt(prop.getProperty("map.w"));
			mapH = Integer.parseInt(prop.getProperty("map.h"));
			int sz = Integer.parseInt(prop.getProperty("sample.size"));
			for ( int i = 0 ; i < sz ; i++) {
				String key = String.format("sample.%d", i);
				parseXY( prop.getProperty(key));
			}
		} catch (IOException e) {
			throw new RuntimeException("stream error");
		}
	}
	private void parseXY(String xyLine) {
		String [] xy = xyLine.split(",");
		int x = Integer.parseInt(xy[0].trim());
		int y = Integer.parseInt(xy[1].trim());
		data.add(new Observation(x, y));
		
	}
	
	public int getMapWidth(){
		return mapW;
	}
	
	public int getMapHeight() {
		return mapH;
	}
	
	public List<Observation> getObservations() {
		return data;
	}
	

}
