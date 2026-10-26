package net.flamgop.auth.minecraft;

import java.util.HexFormat;
import java.util.UUID;

final class ParsingHelper {
    private ParsingHelper() {}

    public static UUID uuidFromString(String str) {
        if (str == null || str.length() != 32) {
            throw new IllegalArgumentException("Invalid UUID length");
        }

        long msb = HexFormat.fromHexDigitsToLong(str, 0, 16);
        long lsb = HexFormat.fromHexDigitsToLong(str, 16, 32);

        return new UUID(msb, lsb);
    }
}
