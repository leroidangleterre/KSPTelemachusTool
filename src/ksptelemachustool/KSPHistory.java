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
    private ArrayList<Double> groundSpeeds;
    private ArrayList<Double> orbitalSpeeds;

    public KSPHistory() {

        altitudes = new ArrayList<>();
        groundSpeeds = new ArrayList<>();
        orbitalSpeeds = new ArrayList<>();
    }

    protected void update(double newAltitude, double newAirspeed, double newOrbitalSpeed) {
        altitudes.add(newAltitude);
        groundSpeeds.add(newAirspeed);
        orbitalSpeeds.add(newOrbitalSpeed);
    }

    protected int getNbElements() {
        return altitudes.size();
    }

    double getGroundSpeed(int i) {
        return groundSpeeds.get(i);
    }

    double getOrbitalSpeed(int i) {
        return orbitalSpeeds.get(i);
    }

    double getAlt(int i) {
        return altitudes.get(i);
    }

    void reset() {
        altitudes.clear();
        groundSpeeds.clear();
    }
}
