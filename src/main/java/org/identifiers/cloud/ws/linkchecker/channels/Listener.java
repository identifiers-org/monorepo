package org.identifiers.cloud.ws.linkchecker.channels;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels
 * Timestamp: 2018-08-02 14:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface Listener<V> {
    void process(V value);
}
