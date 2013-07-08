package com.dcrux.buran.indexing.mapFunction;

import com.dcrux.buran.indexing.mapInput.IFieldTarget;

import java.io.Serializable;
import java.util.EnumSet;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 01:18
 */
public class TextFunction implements Serializable {

    public static final int MAX_TOKEN_LENGTH_LIMIT = 48;

    public static enum MatchType {
        exact,
        lowFuzziness,
        mediumFuzziness,
        highFuzziness
    }

    public static enum ProximityType {
        following,
        near,
        indocument
    }

    private IFieldTarget target;
    private int inputStrLimit = Integer.MAX_VALUE;
    private int numOfTokenLimit = Integer.MAX_VALUE;
    private int tokenLengthLimit = MAX_TOKEN_LENGTH_LIMIT;
    private boolean removeStopwords;
    private EnumSet<MatchType> supportedMatchTypes;
    private EnumSet<ProximityType> supportedProximityTypes;
}
