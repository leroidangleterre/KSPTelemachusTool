package ksptelemachustool;

/**
 *
 * @author arthu
 */
class KSPUtils {

    /**
     * Keep only two digits after decimal point
     *
     */
    public static String truncate(String value, int nbDecimalDigits) {
        int decimalPointPosition = value.indexOf('.');
        if (decimalPointPosition == -1) {
            // No decimal point, no change needed.
            return value;
        } else {
            // keep characters from beginning to 2 after decimal point.
            return value.substring(0, decimalPointPosition + 1 + nbDecimalDigits);
        }
    }
}
