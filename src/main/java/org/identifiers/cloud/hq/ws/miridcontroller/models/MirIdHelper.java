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
     * @param mirId
     * @return
     */
    public static String prettyPrintMirId(long mirId) {
        return String.format("MIR:%08d", mirId);
    }
    // TODO
}
