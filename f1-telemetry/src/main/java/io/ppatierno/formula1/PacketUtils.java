/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public class PacketUtils {
    
    /**
     * Get the String instance from the related bytes in the input buffer
     * The string can be null terminated or filling the buffer until the end
     * 
     * @param buffer buffer containing the string bytes
     * @param maxLength max length of the string
     * @return String instace
     */
    public static String readString(ByteBuf buffer, int maxLength) {
        int result = buffer.bytesBefore(maxLength, (byte) 0);
        // if the string is not null terminated, just read all the characters
        result = result == -1 ? maxLength : result;
        String s = buffer.readBytes(result).toString(Charset.forName("UTF-8"));
        buffer.skipBytes(maxLength - result);
        return s;
    }

    /**
     * Convert a normalized vector represented as 16-bit signed value to float
     * 
     * @param value normalized vector represented as 16-bit signed value
     * @return normalized vector as float
     */
    public static float normalizedVectorToFloat(short value) {
        return value / 32767.0f;
    }
}