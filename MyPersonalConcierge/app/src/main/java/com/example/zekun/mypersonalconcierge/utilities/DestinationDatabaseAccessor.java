package com.example.zekun.mypersonalconcierge.utilities;

import com.example.zekun.mypersonalconcierge.models.Destination;

import java.util.List;
import java.util.ArrayList;

/**
 * This class provides a way to access the Destination objects. It is abstract because it does
 * not specify how they are retrieved. They could come from a remote database or an internal
 * database or they could just be hardcoded.
 */
public abstract class DestinationDatabaseAccessor {

    public abstract int getNumDestinations();

    public abstract Destination getDestination(int destinationId);

    public List<Destination> getAllDestinations() {
        List<Destination> allDestinations = new ArrayList<Destination>();
        int numDestinations = getNumDestinations();

        for (int i = 0; i < numDestinations; ++i) {
            allDestinations.add(getDestination(i));
        }

        return allDestinations;
    }
}