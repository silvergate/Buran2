package com.dcrux.buran.common.labels.setter;

import com.dcrux.buran.common.labels.ILabelName;
import com.dcrux.buran.common.labels.ILabelSet;
import com.dcrux.buran.common.labels.ILabelTargetInc;
import com.dcrux.buran.common.labels.LabelIndex;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:52
 */
public class SetLabel implements ILabelSet {
    private final ILabelName name;
    private final Map<LabelIndex, ILabelTargetInc> targets = new HashMap<>();

    public SetLabel(ILabelName name) {
        this.name = name;
    }

    public SetLabel add(LabelIndex index, ILabelTargetInc target) {
        this.targets.put(index, target);
        return this;
    }

    public ILabelName getName() {
        return name;
    }

    public Map<LabelIndex, ILabelTargetInc> getTargets() {
        return targets;
    }
}
