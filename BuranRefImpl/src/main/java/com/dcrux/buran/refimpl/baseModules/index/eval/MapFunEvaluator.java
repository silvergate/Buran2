package com.dcrux.buran.refimpl.baseModules.index.eval;

import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.fields.getter.FieldGetPrim;
import com.dcrux.buran.common.indexing.IndexDefinition;
import com.dcrux.buran.common.indexing.mapFunction.MapFunction;
import com.dcrux.buran.common.indexing.mapInput.*;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefExt;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.dcrux.buran.scripting.compiler.CompiledBlock;
import com.dcrux.buran.scripting.iface.Block;
import com.dcrux.buran.scripting.iface.VarName;
import com.dcrux.buran.scripting.runner.Runner;
import com.google.common.base.Optional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 20:08
 */
public class MapFunEvaluator extends Module<BaseModule> {

    public MapFunEvaluator(BaseModule baseModule) {
        super(baseModule);
    }

    private void readFields(ClassIndexName classIndexName, ClassDefinition classDefinition,
            Map<IFieldTarget, Object> inputs, CommonNode node, Map<VarName, IFieldTarget> fields,
            Map<ClassIndexName, Map<VarName, Object>> result) {
        /* Eval every field */
        final Map<VarName, Object> varNameObjectMap = new HashMap<>();
        result.put(classIndexName, varNameObjectMap);
        for (Map.Entry<VarName, IFieldTarget> fieldEntry : fields.entrySet()) {
            final IFieldTarget fieldTarget = fieldEntry.getValue();
            final Object value;
            if (!inputs.containsKey(fieldTarget)) {
                if (fieldTarget instanceof FieldTarget) {
                    final FieldTarget fieldTargetCast = (FieldTarget) fieldTarget;
                    final boolean hasValue = node.hasField(fieldTargetCast.getField());
                    if ((!hasValue) && (fieldTargetCast.isRequired())) {
                           /* Index gen abort */
                        result.remove(classIndexName);
                        //TODO: Falsch, da ein index-input auch kein feld haben kann.
                        break;
                    } else {
                        value = getBase().getFieldsModule()
                                .performUnfieldedGetter(new LiveNode(node.getDocument()),
                                        classDefinition, fieldTargetCast.getField(),
                                        FieldGetPrim.SINGLETON);
                        //value = node.getFieldValue(fieldTargetCast.getField());
                        inputs.put(fieldTarget, value);
                    }
                } else {
                    throw new IllegalArgumentException("Unknown IFieldTarget");
                }
            } else {
                value = inputs.get(fieldTarget);
            }

                /* Add value to set */
            varNameObjectMap.put(fieldEntry.getKey(), value);
        }
    }

    private Map<ClassIndexName, Map<VarName, Object>> fetchNodeInputs(
            ClassDefinition classDefinition, CommonNode node) {
        final ClassIndexDefinition cid = classDefinition.getIndexes();
        final Map<ClassIndexName, Map<VarName, Object>> result = new HashMap<>();

        final Map<IFieldTarget, Object> inputs = new HashMap<IFieldTarget, Object>();
        for (final Map.Entry<ClassIndexName, IndexDefinition> entry : cid.getIndexDefinitionMap()
                .entrySet()) {
            final IndexDefinition def = entry.getValue();
            final IMapInput mapInput = def.getMapInput();
            final Map<VarName, IFieldTarget> fields;
            if (mapInput instanceof NodeMapInput) {
                NodeMapInput nodeMapInput = (NodeMapInput) mapInput;
                fields = nodeMapInput.getFields();
            } else if (mapInput instanceof EdgeMapInput) {
                EdgeMapInput edgeMapInput = (EdgeMapInput) mapInput;
                fields = edgeMapInput.getFields();
            } else {
                throw new IllegalArgumentException("Unknown map input-type");
            }

            readFields(entry.getKey(), classDefinition, inputs, node, fields, result);
        }
        return result;
    }

    public Map<ClassIndexName, Collection<EvaluatedMap>> eval(ClassDefExt cde, CommonNode node) {
        final Map<ClassIndexName, Collection<EvaluatedMap>> results = new HashMap<>();
        final Map<ClassIndexName, Map<VarName, Object>> requiredNodeInputs =
                fetchNodeInputs(cde.getClassDefinition(), node);

        for (final ClassIndexName correctClassIndexName : requiredNodeInputs.keySet()) {
            final IndexDefinition indexDef =
                    cde.getClassDefinition().getIndexes().getIndexDefinitionMap()
                            .get(correctClassIndexName);
            final MapFunction mapFunction = indexDef.getMapFunction();
            final Optional<Block> singleMapFunctionOpt = mapFunction.getSingleMapFunction();
            if (singleMapFunctionOpt.isPresent()) {
                final CompiledBlock compiled =
                        cde.getCombiledMapFunctions().get(correctClassIndexName);
                Runner runner = new Runner();

                /* Set field values */
                for (final Map.Entry<VarName, Object> fields : requiredNodeInputs
                        .get(correctClassIndexName).entrySet()) {
                    System.out.println("Runner gives value " + fields.getValue() + " for var " +
                            fields.getKey());
                    runner.setValue(fields.getKey(), fields.getValue());
                }

                Object[] byteAndValue = (Object[]) runner.run(compiled);
                final EvaluatedMap evaluatedMap =
                        new EvaluatedMap((byte[]) byteAndValue[0], byteAndValue[1]);
                results.put(correctClassIndexName, Collections.singletonList(evaluatedMap));
            }
        }

        return results;
    }
}
