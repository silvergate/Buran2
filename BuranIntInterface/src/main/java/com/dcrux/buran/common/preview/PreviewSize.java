package com.dcrux.buran.common.preview;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.08.13 Time: 10:06
 */
public enum PreviewSize {
    xtraSmall(32, 20),
    small(64, 40),
    medium(128, 80),
    large(256, 160),
    larger(512, 320),
    xtraLarge(1024, 640),
    huge(2048, 1280);

    private PreviewSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    int width;
    int height;
}
