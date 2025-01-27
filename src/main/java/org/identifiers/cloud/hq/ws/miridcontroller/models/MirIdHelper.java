package org.identifiers.cloud.hq.ws.miridcontroller.models;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.models
 * Timestamp: 2019-02-26 11:44
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This helper was created with the initial intention of handling MIR ID formatting
 */
public class MirIdHelper {

    /**
     * Given a number representation of a MIR ID, get the pretty representation, a.k.a. official MIR ID representation,
     * i.e. 345800 -> MIR:00345800
     * @param mirId to format
     * @return a formatted representation of the given MIR ID
     */
    public static String prettyPrintMirId(long mirId) {
        return String.format("MIR:%08d", mirId);
    }

    public static long parseMirId(String mirId) throws MirIdHelperException {
        try {
            return Long.parseLong(mirId.split(":")[1]);
        } catch (NumberFormatException e) {
            throw new MirIdHelperException(String.format("Error parsing MIR ID from '%s', due to '%s'", mirId, e.getMessage()));
        }
    }
}
