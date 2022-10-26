package org.example.fieldTypesAndFormats;

public enum NodeCoordType {
    TWOD_COORDS(2),
    THREED_COORDS(3),
    NO_COORDS(-1);

    private final int length;

    private NodeCoordType(int length) {
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }
}
