package org.identifiers.cloud.ws.resolver.models;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-26 10:48
 * ---
 *
 * This class implements the resolution strategy when it comes to different situations, e.g. choose a resource among
 * different options regarding a compact ID
 *
 * DEVNOTE - This should be an interface and different strategies be implementations of that interface, if things go
 * south in terms of granularity, there could be specializations where we have splitted all the possible strategies for
 * all the possible situations as individual pieces. But this is not required right now, just to let you know, future
 * developer.
 */
public class ResolverResourceDecider {
}
