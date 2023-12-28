package ksptelemachustool;

import java.util.ArrayList;

/**
 * This class stores all the values for the altitude and speed,
 * the data can be requested from other classes for display purposes.
 *
 * @author arthu
 */
public class KSPHistory {

    private ArrayList<Double> altitudes;
    private ArrayList<Double> speeds;

    public KSPHistory() {

        altitudes = new ArrayList<>();
        speeds = new ArrayList<>();
    }

    protected void update(double newAltitude, double newAirspeed) {
        altitudes.add(newAltitude);
        speeds.add(newAirspeed);
    }

    protected int getNbElements() {
        return altitudes.size();
    }

    double getSpeed(int i) {
        return speeds.get(i);
    }

    double getAlt(int i) {
        return altitudes.get(i);
    }
}
