package com.shenlandt.wh.internals;

import java.io.ByteArrayInputStream;

/**
 * image abstraction
 */
public abstract class PureImage {
    public ByteArrayInputStream Data;

    public abstract boolean Save(String filePath);
    
    public abstract boolean Save(String filePath, String format);

    public boolean IsParent;
    public long Ix;
    public long Xoff;
    public long Yoff;

    protected String getFormat(ByteArrayInputStream stream) {
        String type = null;
        switch (stream.read()) {
        case 0x89: // PNG: 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
            type = "png";
            break;
        case 0xFF: // JPG: 0xFF, 0xD8, 0xFF
            type = "jpg";
            break;
        case 0x47: // GIF: 0x47, 0x49, 0x46
            type = "gif";
            break;
        case 0x42: // BMP: 0x42, 0x4D
            type = "bmp";
            break;
        case 0x49: // TIFF: 0x49, 0x49 || 0x4D, 0x4D
        case 0x4D:
            type = "tif";
            break;
        default:
            System.out.println("GMapImageProxy: 未知图片格式: " + type);
            break;
        }
        return type;
    }
}