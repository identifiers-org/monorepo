package org.identifiers.cloud.hq.ws.registry.models.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: idorg-hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.helpers
 * Timestamp: 2020-11-10 12:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * Helper for working with common tasks around MIR IDs
 */
public class MirIdHelper {
    public static final String MIR_ID_REGEX = "^MIR:\\d+$";

    /**
     * This method will check whether a given MIR ID is valid.
     *
     * @param mirId MIR ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String mirId) {
        Pattern pattern = Pattern.compile(MIR_ID_REGEX);
        Matcher matcher = pattern.matcher(mirId);
        return matcher.matches();
    }
}
