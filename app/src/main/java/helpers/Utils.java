package helpers;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by grigory on 17/01/18.
 */

public class Utils {
    public static String encodeFileToBase64Binary(Uri fileName, Context ctx) throws IOException {

        InputStream is = ctx.getContentResolver().openInputStream(fileName);
        byte[] bytes = fileToByteArray(is);
        byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
        String encodedString = new String(encoded);

        return encodedString;
    }

    public static byte[] fileToByteArray(InputStream is) throws IOException {
//        InputStream is = new FileInputStream(file);

        long length = is.available();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file ");
        }

        is.close();
        return bytes;
    }
}
