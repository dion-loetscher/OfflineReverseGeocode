/*
 * The MIT License (MIT) [OSI Approved License] The MIT License (MIT)
 * 
 * Copyright (c) 2014 Daniel Glasson
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package geocode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import geocode.kdtree.KDTree;

/**
 *
 * Created by Daniel Glasson on 18/05/2014. Uses KD-trees to quickly find the nearest point
 * 
 * ReverseGeoCode reverseGeoCode = new ReverseGeoCode(new FileInputStream("c:\\AU.txt"), true);
 * System.out.println("Nearest to -23.456, 123.456 is " + geocode.nearestPlace(-23.456, 123.456));
 */
public class ReverseGeoCode {
	KDTree<GeoName>		kdTree;

	Map<String, String>	stateMap	= null;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ReverseGeoCode coder = new ReverseGeoCode(new FileInputStream("/Users/dloetscher/cities1000.txt"), true);

		GeoName name = coder.nearestPlace(39.5, -98.3);

		System.out.println(name);

	}

	// Get placenames from http://download.geonames.org/export/dump/
	/**
	 * Parse the zipped geonames file.
	 * 
	 * @param zippedPlacednames
	 *            a {@link ZipInputStream} zip file downloaded from
	 *            http://download.geonames.org/export/dump/; can not be null.
	 * @param majorOnly
	 *            only include major cities in KD-tree.
	 * 
	 * @throws IOException
	 *             if there is a problem reading the {@link ZipInputStream}.
	 * @throws NullPointerException
	 *             if zippedPlacenames is {@code null}.
	 */
	public ReverseGeoCode(ZipInputStream zippedPlacednames, boolean majorOnly) throws IOException {
		// depending on which zip file is given,
		// country specific zip files have read me files
		// that we should ignore
		ZipEntry entry;
		do {
			entry = zippedPlacednames.getNextEntry();
		} while (entry.getName().equals("readme.txt"));

		createKdTree(zippedPlacednames, majorOnly);

	}

	/**
	 * Parse the raw text geonames file.
	 * 
	 * @param placenames
	 *            the text file downloaded from http://download.geonames.org/export/dump/; can not
	 *            be null.
	 * @param majorOnly
	 *            only include major cities in KD-tree.
	 * 
	 * @throws IOException
	 *             if there is a problem reading the stream.
	 * @throws NullPointerException
	 *             if zippedPlacenames is {@code null}.
	 */
	public ReverseGeoCode(InputStream placenames, boolean majorOnly) throws IOException {
		createKdTree(placenames, majorOnly);
	}

	private void createKdTree(InputStream placenames, boolean majorOnly) throws IOException {
		ArrayList<GeoName> arPlaceNames;
		arPlaceNames = new ArrayList<GeoName>();
		// Read the geonames file in the directory
		BufferedReader in = new BufferedReader(new InputStreamReader(placenames));
		String str;
		try {
			while ((str = in.readLine()) != null) {
				GeoName newPlace = new GeoName(str);
				if (!majorOnly || newPlace.majorPlace) {
					arPlaceNames.add(newPlace);
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			in.close();
		}
		kdTree = new KDTree<GeoName>(arPlaceNames);
	}

	public GeoName nearestPlace(double latitude, double longitude) {
		return kdTree.findNearest(new GeoName(latitude, longitude));
	}

	public String usStateShortToLong(String state) {
		setupStateMap();

		return stateMap.get(state);
	}

	public void setupStateMap() {
		if (stateMap != null)
			return;
		stateMap = new HashMap<String, String>();

		stateMap = new HashMap<String, String>();
		stateMap.put("AL", "Alabama");
		stateMap.put("AK", "Alaska");
		stateMap.put("AB", "Alberta");
		stateMap.put("AZ", "Arizona");
		stateMap.put("AR", "Arkansas");
		stateMap.put("BC", "British Columbia");
		stateMap.put("CA", "California");
		stateMap.put("CO", "Colorado");
		stateMap.put("CT", "Connecticut");
		stateMap.put("DE", "Delaware");
		stateMap.put("DC", "District Of Columbia");
		stateMap.put("FL", "Florida");
		stateMap.put("GA", "Georgia");
		stateMap.put("GU", "Guam");
		stateMap.put("HI", "Hawaii");
		stateMap.put("ID", "Idaho");
		stateMap.put("IL", "Illinois");
		stateMap.put("IN", "Indiana");
		stateMap.put("IA", "Iowa");
		stateMap.put("KS", "Kansas");
		stateMap.put("KY", "Kentucky");
		stateMap.put("LA", "Louisiana");
		stateMap.put("ME", "Maine");
		stateMap.put("MB", "Manitoba");
		stateMap.put("MD", "Maryland");
		stateMap.put("MA", "Massachusetts");
		stateMap.put("MI", "Michigan");
		stateMap.put("MN", "Minnesota");
		stateMap.put("MS", "Mississippi");
		stateMap.put("MO", "Missouri");
		stateMap.put("MT", "Montana");
		stateMap.put("NE", "Nebraska");
		stateMap.put("NV", "Nevada");
		stateMap.put("NB", "New Brunswick");
		stateMap.put("NH", "New Hampshire");
		stateMap.put("NJ", "New Jersey");
		stateMap.put("NM", "New Mexico");
		stateMap.put("NY", "New York");
		stateMap.put("NF", "Newfoundland");
		stateMap.put("NC", "North Carolina");
		stateMap.put("ND", "North Dakota");
		stateMap.put("NT", "Northwest Territories");
		stateMap.put("NS", "Nova Scotia");
		stateMap.put("NU", "Nunavut");
		stateMap.put("OH", "Ohio");
		stateMap.put("OK", "Oklahoma");
		stateMap.put("ON", "Ontario");
		stateMap.put("OR", "Oregon");
		stateMap.put("PA", "Pennsylvania");
		stateMap.put("PE", "Prince Edward Island");
		stateMap.put("PR", "Puerto Rico");
		stateMap.put("QC", "Quebec");
		stateMap.put("RI", "Rhode Island");
		stateMap.put("SK", "Saskatchewan");
		stateMap.put("SC", "South Carolina");
		stateMap.put("SD", "South Dakota");
		stateMap.put("TN", "Tennessee");
		stateMap.put("TX", "Texas");
		stateMap.put("UT", "Utah");
		stateMap.put("VT", "Vermont");
		stateMap.put("VI", "Virgin Islands");
		stateMap.put("VA", "Virginia");
		stateMap.put("WA", "Washington");
		stateMap.put("WV", "West Virginia");
		stateMap.put("WI", "Wisconsin");
		stateMap.put("WY", "Wyoming");
		stateMap.put("YT", "Yukon Territory");
	}
}
