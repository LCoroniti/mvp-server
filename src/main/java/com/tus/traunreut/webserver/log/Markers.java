package com.tus.traunreut.webserver.log;


import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Markers {
    public final static Marker NETWORK = MarkerFactory.getMarker("NETWORK");
    public final static Marker DATABASE = MarkerFactory.getMarker("DATABASE");
}
