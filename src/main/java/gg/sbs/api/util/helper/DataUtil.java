package gg.sbs.api.util.helper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gg.sbs.api.util.CompressionType;
import lombok.Cleanup;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/**
 * <p>Provides data compression, decompression, encoding, decoding,<br>
 * variable types and stream creation.</p>
 */
@SuppressWarnings("all")
public final class DataUtil {

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	// Mapping table from 6-bit nibbles to Base64 characters.
	private final static char[] map1 = new char[64];
	static {
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++)
			map1[i++] = c;
		for (char c = 'a'; c <= 'z'; c++)
			map1[i++] = c;
		for (char c = '0'; c <= '9'; c++)
			map1[i++] = c;
		map1[i++] = '+';
		map1[i++] = '/';
	}

	// Mapping table from Base64 characters to 6-bit nibbles.
	private final static byte[] map2 = new byte[128];
	static {
		Arrays.fill(map2, (byte) -1);
		for (int i = 0; i < 64; i++)
			map2[map1[i]] = (byte) i;
	}

	private DataUtil() { }

	public static String compress(String data) throws IOException {
		return compress(data.getBytes());
	}

	public static String compress(String data, int level) throws IOException {
		return compress(data.getBytes(), level);
	}

	public static String compress(byte[] data) throws IOException {
		return compress(data, 1);
	}

	public static String compress(byte[] data, int level) throws IOException {
		if (data.length == 0)
			return "";

		byte[] results;
		@Cleanup ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		@Cleanup DeflaterOutputStream deflater = new DeflaterOutputStream(buffer, new Deflater(level));
		deflater.write(data);
		results = buffer.toByteArray();
		return new String(encode(results));
	}

	/**
	 * Decodes Base64 data. No blanks or line breaks are allowed within the
	 * Base64 encoded data.
	 *
	 * @param in a character array containing the Base64 encoded data.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(char[] in) {
		int iLen = in.length;

		if (iLen % 4 != 0)
			throw new RuntimeException("Length of Base64 encoded input string is not a multiple of 4.");

		while (iLen > 0 && in[iLen - 1] == '=')
			iLen--;

		int oLen = (iLen * 3) / 4;
		byte[] out = new byte[oLen];
		int ip = 0;
		int op = 0;

		while (ip < iLen) {
			int i0 = in[ip++];
			int i1 = in[ip++];
			int i2 = ip < iLen ? in[ip++] : 'A';
			int i3 = ip < iLen ? in[ip++] : 'A';

			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
				throw new RuntimeException("Illegal character in Base64 encoded data.");

			int b0 = map2[i0];
			int b1 = map2[i1];
			int b2 = map2[i2];
			int b3 = map2[i3];

			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
				throw new RuntimeException("Illegal character in Base64 encoded data.");

			int o0 = (b0 << 2) | (b1 >>> 4);
			int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
			int o2 = ((b2 & 3) << 6) | b3;
			out[op++] = (byte) o0;

			if (op < oLen)
				out[op++] = (byte) o1;

			if (op < oLen)
				out[op++] = (byte) o2;
		}

		return out;
	}

	public static byte[] decompress(String data) throws IOException {
		if (StringUtil.isEmpty(data))
			return new byte[] {};

		byte[] bytes = decode(data.toCharArray());
		byte[] results;
		@Cleanup ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		@Cleanup InflaterOutputStream inflater = new InflaterOutputStream(buffer);
		inflater.write(bytes);
		results = buffer.toByteArray();

		return results;
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 *
	 * @param in an array containing the data bytes to be encoded.
	 * @return A character array with the Base64 encoded data.
	 */
	public static char[] encode(byte[] in) {
		int iLen = in.length;
		int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
		int oLen = ((iLen + 2) / 3) * 4; // output length including padding
		char[] out = new char[oLen];
		int ip = 0;
		int op = 0;

		while (ip < iLen) {
			int i0 = in[ip++] & 0xff;
			int i1 = ip < iLen ? in[ip++] & 0xff : 0;
			int i2 = ip < iLen ? in[ip++] & 0xff : 0;
			int o0 = i0 >>> 2;
			int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			int o3 = i2 & 0x3F;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			op++;
			out[op] = op < oDataLen ? map1[o3] : '=';
			op++;
		}

		return out;
	}

	public static CompressionType getCompression(InputStream inputStream) throws IOException {
		if (!inputStream.markSupported())
			inputStream = new BufferedInputStream(inputStream);

		inputStream.mark(0);

		if (inputStream.read() == 120)
			return CompressionType.ZLIB;

		inputStream.reset();
		if (inputStream.read() == 31)
			return CompressionType.GZIP;

		return CompressionType.NONE;
	}

	public static ByteArrayDataInput newDataInput(byte[] data) {
		return ByteStreams.newDataInput(data);
	}

	public static ByteArrayDataInput newDataInput(ByteArrayInputStream inputStream) {
		return ByteStreams.newDataInput(inputStream);
	}

	public static ByteArrayDataInput newDataInput(byte[] data, int start) {
		return ByteStreams.newDataInput(data, start);
	}

	public static ByteArrayDataOutput newDataOutput() {
		return ByteStreams.newDataOutput();
	}

	public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream outputStream) {
		return ByteStreams.newDataOutput(outputStream);
	}

	public static ByteArrayDataOutput newDataOutput(int size) {
		return ByteStreams.newDataOutput(size);
	}

	public static int readVarInt(DataInputStream inputStream) throws IOException {
		int i = 0;
		int j = 0;

		while (true) {
			int k = inputStream.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128) break;
		}

		return i;
	}

	public static int readVarInt(ByteArrayDataInput dataInput) {
		int i = 0;
		int j = 0;

		while (true) {
			int k = dataInput.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128) break;
		}

		return i;
	}

	/**
	 * Gets human readable ascii of a hexadecimal string.
	 *
	 * @param hex to convert
	 * @return converted hexadecimal string into ascii
	 */
	public static String toAsciiString(String hex) {
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < hex.length(); i += 2) {
			String str = hex.substring(i, i + 2);
			output.append((char)Integer.parseInt(str, 16));
		}

		return output.toString();
	}

	/**
	 * Gets a byte array of converted objects.
	 *
	 * @param data to convert
	 * @return converted objects in byte array
	 */
	public static byte[] toByteArray(Object... data) {
		return toByteArray(Arrays.asList(data));
	}

	/**
	 * Gets a byte array of converted objects.
	 *
	 * @param data to convert
	 * @return converted objects in byte array
	 */
	public static byte[] toByteArray(Collection<?> data) {
		ByteArrayDataOutput output = DataUtil.newDataOutput();

		for (Object obj : data) {
			if (obj instanceof byte[])
				output.write((byte[])obj);
			else if (obj instanceof Byte)
				output.writeByte((int)obj);
			else if (obj instanceof Boolean)
				output.writeBoolean((boolean)obj);
			else if (obj instanceof Character)
				output.writeChar((char)obj);
			else if (obj instanceof Short)
				output.writeShort((short)obj);
			else if (obj instanceof Integer)
				output.writeInt((int)obj);
			else if (obj instanceof Long)
				output.writeLong((long)obj);
			else if (obj instanceof Double)
				output.writeDouble((double)obj);
			else if (obj instanceof Float)
				output.writeFloat((float)obj);
			else if (obj instanceof String)
				output.writeUTF((String)obj);
			else if (obj instanceof UUID)
				output.writeUTF(obj.toString());
		}

		return output.toByteArray();
	}

	/**
	 * Gets the hexadecimal string of a byte array.
	 *
	 * @param bytes to convert
	 * @return converted byte array as hexadecimal string
	 */
	public static String toHexString(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];

		for (int i = 0; i < bytes.length; i++) {
			int v = bytes[i] & 0xFF;
			hexChars[i * 2] = HEX_ARRAY[v >>> 4];
			hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}

		return new String(hexChars);
	}

	public static void writeByteArray(DataOutputStream out, byte[] data) throws IOException {
		writeVarInt(out, data.length);
		out.write(data);
	}

	public static void writeByteArray(ByteArrayDataOutput out, byte[] data) {
		writeVarInt(out, data.length);
		out.write(data);
	}

	public static void writeString(DataOutputStream out, String string) throws IOException {
		writeVarInt(out, string.length());
		out.write(string.getBytes(StandardCharsets.UTF_8));
	}

	public static void writeString(ByteArrayDataOutput out, String string) {
		writeVarInt(out, string.length());
		out.write(string.getBytes(StandardCharsets.UTF_8));
	}

	public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}

			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}

	public static void writeVarInt(ByteArrayDataOutput out, int paramInt) {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}

			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}

}