/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.microsphere.io;

import io.microsphere.util.BaseUtils;
import io.microsphere.util.SystemUtils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.microsphere.util.ArrayUtils.EMPTY_BYTE_ARRAY;
import static io.microsphere.util.StringUtils.isBlank;
import static io.microsphere.util.SystemUtils.FILE_ENCODING;
import static java.lang.Integer.getInteger;
import static java.util.Objects.requireNonNull;

/**
 * The utilities class for I/O
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see Files
 * @see Paths
 * @since 1.0.0
 */
public abstract class IOUtils extends BaseUtils {

    /**
     * The buffer size for I/O
     */
    public static final int BUFFER_SIZE = getInteger("microsphere.io.buffer.size", 4096);

    /**
     * Copy the contents of the given InputStream into a new byte array.
     * <p>Leaves the stream open when done.
     *
     * @param in the stream to copy from (may be {@code null} or empty)
     * @return the new byte array that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static byte[] toByteArray(InputStream in) throws IOException {
        if (in == null) {
            return EMPTY_BYTE_ARRAY;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }

    /**
     * Copy the contents of the given InputStream into a new {@link String}.
     * <p>Leaves the stream open when done.
     *
     * @param in       the stream to copy from (may be {@code null} or empty)
     * @param encoding the encoding to use, if it's <code>null</code>, take the {@link SystemUtils#FILE_ENCODING} as default
     * @return the new byte array that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static String toString(InputStream in, String encoding) throws IOException {
        byte[] bytes = toByteArray(in);
        String charset = isBlank(encoding) ? FILE_ENCODING : encoding;
        return EMPTY_BYTE_ARRAY.equals(bytes) ? null : new String(bytes, charset);
    }

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * <p>Leaves both streams open when done.
     *
     * @param in  the InputStream to copy from
     * @param out the OutputStream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        requireNonNull(in, "No InputStream specified");
        requireNonNull(out, "No OutputStream specified");

        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * Closes a URLConnection.
     *
     * @param conn the connection to close.
     */
    public static void close(URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    /**
     * Unconditionally close a <code>Closeable</code>.
     * <p>
     * Equivalent to {@link Closeable#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     * <p>
     * Example code:
     * <pre>
     *   Closeable closeable = null;
     *   try {
     *       closeable = new FileReader("foo.txt");
     *       // process closeable
     *       closeable.close();
     *   } catch (Exception e) {
     *       // error handling
     *   } finally {
     *       IOUtils.closeQuietly(closeable);
     *   }
     * </pre>
     *
     * @param closeable the object to close, may be null or already closed
     */
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {
            // ignore
        }
    }
}
