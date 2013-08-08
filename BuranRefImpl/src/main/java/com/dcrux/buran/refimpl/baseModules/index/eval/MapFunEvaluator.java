package com.dcrux.buran.refimpl.baseModules.index.eval;

import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.indexing.IndexDefinition;
import com.dcrux.buran.common.indexing.mapFunction.MapFunction;
import com.dcrux.buran.common.indexing.mapFunction.TextFunction;
import com.dcrux.buran.common.indexing.mapInput.FieldTarget;
import com.dcrux.buran.common.indexing.mapInput.IFieldTarget;
import com.dcrux.buran.common.indexing.mapInput.IMapInput;
import com.dcrux.buran.common.indexing.mapInput.NodeMapInput;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefExt;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.text.processors.IEmmitter;
import com.dcrux.buran.scripting.compiler.CompiledBlock;
import com.dcrux.buran.scripting.iface.Code;
import com.dcrux.buran.scripting.iface.VarName;
import com.dcrux.buran.scripting.runner.Runner;
import com.google.common.base.Optional;

import java.util.*;

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
                        /*value = getBase().getFieldsModule()
                                .performUnfieldedGetter(new LiveNode(node.getDocument()),
                                        fieldTargetCast,
                                        FieldGetPrim.SINGLETON);*/

                        //TODO: Removed
                        value = null;

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
            } else {
                throw new IllegalArgumentException("Unknown map input-type");
            }

            readFields(entry.getKey(), classDefinition, inputs, node, fields, result);
        }
        return result;
    }

    private byte[] combine(byte[] one, byte[] two) {
        final byte[] result = new byte[one.length + two.length];
        System.arraycopy(one, 0, result, 0, one.length);
        System.arraycopy(two, 0, result, one.length, two.length);
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
            final Optional<Code> singleMapFunctionOpt = mapFunction.getSingleMapFunction();
            final Optional<TextFunction> textFunctionOpt = mapFunction.getTextFunction();

            EvaluatedMap evaluatedFromCode = null;
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
                evaluatedFromCode = evaluatedMap;
            }

            Set<byte[]> evaluatedFromText = null;
            if (textFunctionOpt.isPresent()) {
                evaluatedFromText = new HashSet<>();
                TextFunction textFunction = textFunctionOpt.get();
                /*Object value = getBase().getFieldsModule()
                        .performUnfieldedGetter(new LiveNode(node.getDocument()),
                                cde.getClassDefinition(), textFunction.getTarget(),
                                textFunction.getDataGetter());*/
                //TODO: Removed
                Object value = null;
                String valueStr = (String) value;
                final Set<byte[]> evaluatedFromTextFinal = evaluatedFromText;
                if (valueStr != null) {
                    getBase().getTextModule().process(valueStr, new IEmmitter.ICallback() {
                        @Override
                        public void emit(byte[] data) {
                            evaluatedFromTextFinal.add(data);
                        }
                    });
                }
            }

            /* Combine */
            if (evaluatedFromText == null) {
                results.put(correctClassIndexName, Collections.singletonList(evaluatedFromCode));
            } else if (evaluatedFromCode != null) {
                /* Text and code */
                final List<EvaluatedMap> combined = new ArrayList<>();
                for (byte[] fromText : evaluatedFromText) {
                    byte[] combinedByte = combine(evaluatedFromCode.getKey(), fromText);
                    combined.add(new EvaluatedMap(combinedByte, evaluatedFromCode.getValue()));
                }
                results.put(correctClassIndexName, combined);
            } else {
                /* From text only */
                final List<EvaluatedMap> combined = new ArrayList<>();
                for (byte[] fromText : evaluatedFromText) {
                    combined.add(new EvaluatedMap(fromText, 0));
                }
                results.put(correctClassIndexName, combined);
            }
        }

        return results;
    }
}
