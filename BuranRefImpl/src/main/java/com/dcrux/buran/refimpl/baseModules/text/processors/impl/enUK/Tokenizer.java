package com.dcrux.buran.refimpl.baseModules.text.processors.impl.enUK;

import com.dcrux.buran.refimpl.baseModules.text.processors.impl.StdTokenizer;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 13:06
 */
public class Tokenizer extends StdTokenizer {

    private int maxTokenLen = Integer.MAX_VALUE;
    private int maxInputLen = Integer.MAX_VALUE;
    private int MIN_TOKEN_LENGTH = 3;
    private int MIN_TOKEN_LENGTH_SPECIAL = 2;

    @Override
    public void tokenize(String input, ICallback callback) {
        int begin = -1;
        StdTokenizer.CDataBeginInfo cDataBeginInfo = null;
        String token = null;
        for (int i = 0; i < input.length(); i++) {
            boolean closeTokenIfPossible = false;
            final char chr = input.charAt(i);

            /* CData processing */
            if (cDataBeginInfo == null) {
                cDataBeginInfo = super.isCdataInit(input, i);
                if ((cDataBeginInfo != null)) {
                /* Begin new Cdata section */
                    i = cDataBeginInfo.getCdataBeginPos();

                /* End normal token processing*/
                    begin = -1;
                }
            } else {
                final boolean cdataEnd = super.isCdataEnd(input, i);
                if (cdataEnd) {
                    /* Cdata ends here */
                    String cData = input.substring(cDataBeginInfo.getCdataBeginPos(), i);
                    callback.nextCdata(cDataBeginInfo.getTypeInt(), cData);
                    cDataBeginInfo = null;
                }
            }

            /* Do normal token processing */
            if (cDataBeginInfo == null) {
                boolean letterOrDigit = Character.isLetterOrDigit(chr);
                if (letterOrDigit) {
                    if (begin == -1) {
                        begin = i;
                    }
                } else {
                    closeTokenIfPossible = true;
                }

                /* Close if last character */
                final boolean isLastCharacter = (i + 1 == input.length());
                if (isLastCharacter) {
                    closeTokenIfPossible = true;
                }

                if (closeTokenIfPossible) {
                    if (begin != -1) {
                /* New token (end) */
                        int length = (i - begin);
                        final boolean process;
                        if (length < MIN_TOKEN_LENGTH) {
                            if (length >= MIN_TOKEN_LENGTH_SPECIAL) {
                        /* OK if previous char is uppercase or a number */
                                if (i > 0) {
                                    final char previousChar = input.charAt(i - 1);
                                    if (Character.isUpperCase(previousChar) ||
                                            Character.isDigit(previousChar)) {
                                        process = true;
                                    } else {
                                        process = false;
                                    }
                                } else {
                                    process = false;
                                }
                            } else {
                                process = false;
                            }
                        } else {
                            process = true;
                        }

                        if (process) {
                            final int end;
                            if (isLastCharacter) {
                                end = i + 1;
                            } else {
                                end = i;
                            }
                            final String tokenIn = input.substring(begin, end);
                            if (token != null) {
                                callback.nextToken(token, false);
                            }
                            token = tokenIn;
                        }
                        begin = -1;
                    }
                }

            }
        }

        /* Send last token */
        if (token != null) {
            callback.nextToken(token, true);
        }
    }
}
