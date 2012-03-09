/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.modules.titanium.geolocation.android;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;

import android.location.Location;


/**
 * LocationRuleProxy represents a location rule that can be used for filtering location updates.  
 * The properties contained in the rule that can used for filtering are:
 * <ul>
 * 	<li>provider - the name of the location service that this rule will match</li>
 * 	<li>accuracy - the accuracy value compared against the accuracy value of a location.  
 * 		if the location accuracy value is less than (lower is better) the accuracy value 
 * 		for the rule then the comparison will pass</li>
 * 	<li>time - the time value compared against the time value of a location.  if the 
 * 		location time value is greater (higher is better) than the time value for the 
 * 		rule then the comparison will pass</li>
 * </ul>
 */
@Kroll.proxy(propertyAccessors = {
	TiC.PROPERTY_PROVIDER,
	TiC.PROPERTY_ACCURACY,
	TiC.PROPERTY_TIME
})
public class LocationRuleProxy extends KrollProxy
{
	/**
	 * Constructor.  Used primarily when creating a location rule via 
	 * Ti.Geolocation.Android.createLocationRule
	 * 
	 * @param creationArgs			creation arguments for the location provider
	 */
	public LocationRuleProxy(Object[] creationArgs)
	{
		super();

		handleCreationArgs(null, creationArgs);
	}

	/**
	 * Constructor.  Used primarily when creating a location provider via 
	 * internal platform code.
	 * 
	 * @param provider			location service that the provider should be associated with
	 * @param accuracy			the accuracy value that will be compared against the accuracy 
	 * 							value of a location
	 * @param time				the time value that will be compared against the time 
	 * 							value of a location
	 */
	public LocationRuleProxy(String provider, Double accuracy, Double time)
	{
		super();

		setProperty(TiC.PROPERTY_PROVIDER, provider);
		setProperty(TiC.PROPERTY_ACCURACY, accuracy);
		setProperty(TiC.PROPERTY_TIME, time);
	}

	/**
	 * Compares the two specified locations and checks if the new location passes the 
	 * checks specified in this rule
	 * 
	 * @param currentLocation			current location that is compared against the specified
	 * 									new location
	 * @param newLocation				new location that is compared against the specified
	 * 									current location
	 * @return							<code>true</code> if the new location passes the checks 
	 * 									for this rule when compared against the current location, 
	 * 									<code>false</code> if not
	 */
	public boolean check(Location currentLocation, Location newLocation)
	{
		boolean passed = true;

		String provider = TiConvert.toString(properties.get(TiC.PROPERTY_PROVIDER));
		if(provider != null) {
			if(!(provider.equals(newLocation.getProvider()))) {
				passed = false;
			}
		}

		Object rawAccuracy = properties.get(TiC.PROPERTY_ACCURACY);
		if(rawAccuracy != null) {
			double accuracyValue = TiConvert.toDouble(rawAccuracy);
			if(accuracyValue < newLocation.getAccuracy()) {
				passed = false;
			}
		}

		Object rawTime = properties.get(TiC.PROPERTY_TIME);
		if(rawTime != null) {
			double timeValue = TiConvert.toDouble(rawTime);

			// make sure the update breaks the time threshold on the diff of the
			// current and new location
			if(timeValue < (newLocation.getTime() - currentLocation.getTime())) {
				passed = false;
			}
		}

		return passed;
	}
}

