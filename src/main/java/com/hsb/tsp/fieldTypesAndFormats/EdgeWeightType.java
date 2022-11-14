package com.hsb.tsp.fieldTypesAndFormats;

public enum EdgeWeightType {
    EXPLICIT,
    EUC_2D,
    EUC_3D,
    MAX_2D,
    MAX_3D,
    MAN_2D,
    MAN_3D,
    CEIL_2D,
    GEO,
    ATT;




    public NodeCoordType getNodeCoordType() {
        switch (this) {
            case EUC_2D:
            case MAX_2D:
            case MAN_2D:
            case CEIL_2D:
            case GEO:
            case ATT:
                return NodeCoordType.TWOD_COORDS;
            case EUC_3D:
            case MAX_3D:
            case MAN_3D:
                return NodeCoordType.THREED_COORDS;
            default:
                throw new IllegalArgumentException("no node coordinate type defined for " + this);
        }
    }
}
