package com.dcrux.buran.refimpl.baseModules.text.processors.impl;

import com.dcrux.buran.refimpl.baseModules.text.processors.IEmmitter;
import com.dcrux.buran.refimpl.baseModules.text.processors.KeyRange;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 19:57
 */
public class StdEmitter implements IEmmitter {

    private static enum Bound {
        upper,
        lower,
        none
    }

    private final static int[] PRIMES = {127, 103, 89, 73, 47, 31, 17, 17, 17, 17, 5, 5, 5};
    private final static long[] FACTORS =
            {10179239712690875l, 98827570026125l, 1110422135125l, 15211262125l, 323643875l,
                    10440125l, 614125l, 36125l, 2125l, 125l, 25l, 5l, 0l};

    private long toHash(String token) {
        return toHash(token, 0, PRIMES.length - 1, Bound.none);
    }

    private long toHash(String token, int inFrom, int inTo, Bound bound) {
        final byte[] bytes;
        try {
            bytes = token.getBytes("UTF-8");
        } catch (UnsupportedEncodingException use) {
            throw new IllegalStateException();
        }
        long num = Long.MIN_VALUE;

        for (int i = 0; (i + inFrom <= inTo); i++) {
            boolean outsideString = (i >= bytes.length);
            if (outsideString) {
                if (bound == Bound.none) {
                    break;
                }
            }

            final long factor = FACTORS[i + inFrom];
            final int value;
            if (outsideString) {
                if (bound == Bound.lower) {
                    value = 0;
                } else {
                    value = PRIMES[i + inFrom];
                }
            } else {
                value = bytes[i] % PRIMES[i + inFrom];
            }
            num = num + (factor * value);
        }

        return num;
    }

    private final static long[] COMB_FACTORS = {47143689453125l, 1305015625l, 36125l, 0l};

    private long toHash(List<String> elements, Bound bound) {
        long number = Long.MIN_VALUE;

        for (int normalIndex = 0; normalIndex < COMB_FACTORS.length; normalIndex++) {
            /* No mor elements? */
            boolean outOfElements = normalIndex >= elements.size();

            if (outOfElements && bound == Bound.none) {
                System.out.println("End because end of elements");
                /* End now */
                break;
            }

            final long hash;
            if (outOfElements) {
                final String token = "";
                hash = toHash(token, 8, 12, bound);

                System.out.println("Out of elements");
            } else {
                final String token = elements.get(normalIndex);
                hash = toHash(token, 8, 12, bound);

                System.out.println(MessageFormat.format("Token: {0}, Hash {1}", token, hash));
            }

            final long factor = COMB_FACTORS[normalIndex];

            number = number + hash * factor;
        }

        return number;
    }

    private final List<String> queue = new LinkedList<>();

    @Override
    public void begin() {
        this.queue.clear();
    }

    private byte[] toByte(long value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(value);
        return byteBuffer.array();
    }

    @Override
    public void nextToken(String token, ICallback callback, boolean lastToken) {
        this.queue.add(token);
        long hash = toHash(token);
        callback.emit(toByte(hash));

        if (lastToken) {
            flushQueue(callback);
        } else {
            processQueue(callback, false);
        }
    }

    private void processQueue(ICallback callback, boolean ignoreLength) {
        if ((this.queue.size() >= COMB_FACTORS.length) || ignoreLength) {
            final long hash = toHash(this.queue, Bound.none);
            callback.emit(toByte(hash));
            /* Remove oldest element */
            if (!this.queue.isEmpty()) {
                this.queue.remove(0);
            }
        }
    }

    private void flushQueue(ICallback callback) {
        while (this.queue.size() > 1) {
            processQueue(callback, true);
        }
    }

    @Override
    public KeyRange singleToken(String token) {

        long lower = toHash(token, 0, PRIMES.length - 1, Bound.lower);
        long upper = toHash(token, 0, PRIMES.length - 1, Bound.upper);

        return new KeyRange(toByte(lower), toByte(upper));
    }

    @Override
    public KeyRange multiToken(List<String> tokens) {
        long lower = toHash(tokens, Bound.lower);
        long upper = toHash(tokens, Bound.upper);
        return new KeyRange(toByte(lower), toByte(upper));
    }

}
