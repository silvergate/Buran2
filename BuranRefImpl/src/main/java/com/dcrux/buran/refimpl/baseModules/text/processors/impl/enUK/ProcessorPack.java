package com.dcrux.buran.refimpl.baseModules.text.processors.impl.enUK;

import com.dcrux.buran.refimpl.baseModules.text.processors.*;
import com.dcrux.buran.refimpl.baseModules.text.processors.impl.StdEmitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 21:49
 */
public class ProcessorPack implements IProcessorPack {

    private final IEmmitter emmitter = new StdEmitter();
    private final IFilter filter = new Filter();
    private final INormalizer normalizer = new Normalizer();
    private final ITokenizer tokenizer = new Tokenizer();

    @Override
    public void process(String input, final IEmmitter.ICallback callback) {
        final IEmmitter.ICallback emitCallback = new IEmmitter.ICallback() {
            @Override
            public void emit(byte[] data) {
                System.out.println("EMIT: " + Arrays.toString(data));
                callback.emit(data);
            }
        };
        emmitter.begin();
        tokenizer.tokenize(input, new ITokenizer.ICallback() {
            @Override
            public void nextToken(String token, boolean lastToken) {
                final String normalized = normalizer.normalize(token);
                boolean use = filter.use(normalized);
                if (!use) {
                    return;
                }
                System.out.println("NORMALIZED: " + normalized);
                emmitter.nextToken(normalized, emitCallback, lastToken);
            }

            @Override
            public void nextCdata(int typeInt, String cdata) {
                //TODO
                System.out.println("CDATA: " + cdata);
            }
        });
    }

    @Override
    public KeyRange generateKey(String query) {
        final List<String> tokens = new ArrayList<>();
        tokenizer.tokenize(query, new ITokenizer.ICallback() {
            @Override
            public void nextToken(String token, boolean lastToken) {
                final String normalized = normalizer.normalize(token);
                boolean use = filter.use(normalized);
                if (!use) {
                    return;
                }
                tokens.add(normalized);
            }

            @Override
            public void nextCdata(int typeInt, String cdata) {
                //TODO
            }
        });

        if (tokens.size() == 1) {
            return emmitter.singleToken(tokens.get(0));
        } else if (tokens.size() == 0) {
            return null;
        } else {
            return emmitter.multiToken(tokens);
        }
    }
}
