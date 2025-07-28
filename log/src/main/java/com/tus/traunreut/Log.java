package com.tus.traunreut;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Log {
    public final static Marker NETWORK = MarkerFactory.getMarker("NETWORK");
    public final static Marker DATABASE = MarkerFactory.getMarker("DATABASE");
    public final static Marker INTERNAL = MarkerFactory.getMarker("INTERNAL");

    public final static Logger DB_LOG = LoggerFactory.getLogger("DATABASE");
    public final static Logger NETWORK_LOG = LoggerFactory.getLogger("NETWORK");
    public final static Logger INTERNAL_LOG = LoggerFactory.getLogger("INTERNAL");
}
