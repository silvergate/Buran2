package com.dcrux.buran.common.indexing.mapFunction;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.FieldGetStr;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.types.StringType;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 01:18
 */
public class TextFunction implements Serializable {

    public static final int MAX_TOKEN_LENGTH_LIMIT = 48;
    public static final int MAX_INPUT_STRING_LENGTH_LIMIT = StringType.MAXLEN_LIMIT;

    @Deprecated
    public static enum FuzzinessType {
        lowFuzziness,
        mediumFuzziness,
        highFuzziness
    }

    private TextFunction(FieldIndex target, IUnfieldedDataGetter<?> dataGetter, int inputStrLimit) {
        this.target = target;
        this.dataGetter = dataGetter;
        this.inputStrLimit = inputStrLimit;
    }

    public static TextFunction c(FieldIndex target) {
        return new TextFunction(target, FieldGetStr.SINGLETON, StringType.MAXLEN_LIMIT);
    }

    /*public static enum ProximityType {
        following,
        near,
        indocument // This operation is optional
    } */

    private FieldIndex target;
    private IUnfieldedDataGetter<?> dataGetter;

    private int inputStrLimit = MAX_INPUT_STRING_LENGTH_LIMIT;
    //private int numOfTokenLimit = Integer.MAX_VALUE;
    //private int tokenLengthLimit = MAX_TOKEN_LENGTH_LIMIT;
    //private boolean removeStopwords;
    //private EnumSet<FuzzinessType> supportedFuzzinessTypes;
    //private EnumSet<ProximityType> supportedProximityTypes;

    public FieldIndex getTarget() {
        return target;
    }

    public void setTarget(FieldIndex target) {
        this.target = target;
    }

    public int getInputStrLimit() {
        return inputStrLimit;
    }

    public void setInputStrLimit(int inputStrLimit) {
        this.inputStrLimit = inputStrLimit;
    }

    public IUnfieldedDataGetter<?> getDataGetter() {
        return dataGetter;
    }

    /*
    public int getNumOfTokenLimit() {
        return numOfTokenLimit;
    }

    public void setNumOfTokenLimit(int numOfTokenLimit) {
        this.numOfTokenLimit = numOfTokenLimit;
    }

    public int getTokenLengthLimit() {
        return tokenLengthLimit;
    }

    public void setTokenLengthLimit(int tokenLengthLimit) {
        this.tokenLengthLimit = tokenLengthLimit;
    }

    public boolean isRemoveStopwords() {
        return removeStopwords;
    }

    public void setRemoveStopwords(boolean removeStopwords) {
        this.removeStopwords = removeStopwords;
    }

    public EnumSet<FuzzinessType> getSupportedFuzzinessTypes() {
        return supportedFuzzinessTypes;
    }

    public void setSupportedFuzzinessTypes(EnumSet<FuzzinessType> supportedFuzzinessTypes) {
        this.supportedFuzzinessTypes = supportedFuzzinessTypes;
    }   */

    /*public EnumSet<ProximityType> getSupportedProximityTypes() {
        return supportedProximityTypes;
    }

    public void setSupportedProximityTypes(EnumSet<ProximityType> supportedProximityTypes) {
        this.supportedProximityTypes = supportedProximityTypes;
    } */
}
