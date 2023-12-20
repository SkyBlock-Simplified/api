package dev.sbs.api.util.helper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;

/**
 * <p>Provides data compression, decompression, encoding, decoding,<br>
 * variable types and stream creation.</p>
 */
@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataUtil {

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	/**
	 * Decodes Base64 data.
	 * No blanks or line breaks are allowed within the Base64 encoded data.
	 *
	 * @param input a character array containing the Base64 encoded data.
	 * @return An array containing the decoded data.
	 * @throws IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(char[] input) {
		return decode(new String(input));
	}

	/**
	 * Decodes Base64 data.
	 * No blanks or line breaks are allowed within the Base64 encoded data.
	 *
	 * @param in a character array containing the Base64 encoded data.
	 * @return An array containing the decoded data.
	 * @throws IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(String input) {
		return Base64.getDecoder().decode(input);
	}

	/**
	 * Decodes Base64 data.
	 * No blanks or line breaks are allowed within the Base64 encoded data.
	 *
	 * @param in a character array containing the Base64 encoded data.
	 * @return An array containing the decoded data.
	 * @throws IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	public static String decodeToString(String input) {
		return new String(Base64.getDecoder().decode(input), StandardCharsets.UTF_8);
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 *
	 * @param input an array containing the data bytes to be encoded.
	 * @return A byte array containing the encoded data.
	 */
	public static byte[] encode(String input) {
		return encode(input.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 *
	 * @param input an array containing the data bytes to be encoded.
	 * @return A byte array containing the encoded data.
	 */
	public static byte[] encode(byte[] input) {
		return Base64.getEncoder().encode(input);
	}

	/**
	 * Encodes a byte array into Base64 format.
	 * No blanks or line breaks are inserted.
	 *
	 * @param input a string containing the data to be encoded.
	 * @return A string containing the encoded data.
	 */
	public static String encodeToString(String input) {
		return encodeToString(input.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Encodes a byte array into Base64 format.
	 * No blanks or line breaks are inserted.
	 *
	 * @param input an array containing the data bytes to be encoded.
	 * @return A string containing the encoded data.
	 */
	public static String encodeToString(byte[] input) {
		return Base64.getEncoder().encodeToString(input);
	}

	public static @NotNull CompressionType getCompression(@NotNull InputStream inputStream) throws IOException {
		if (!inputStream.markSupported())
			inputStream = new BufferedInputStream(inputStream);

		CompressionType compressionType = CompressionType.NONE;
		inputStream.mark(0);
		int magic = inputStream.read();

		if (magic == 120)
			compressionType = CompressionType.ZLIB;

		if (magic == 31 || inputStream.read() == 139)
			compressionType = CompressionType.GZIP;

		inputStream.reset();
		return compressionType;
	}

	public static @NotNull ByteArrayDataInput newDataInput(byte[] data) {
		return ByteStreams.newDataInput(data);
	}

	public static @NotNull ByteArrayDataInput newDataInput(ByteArrayInputStream inputStream) {
		return ByteStreams.newDataInput(inputStream);
	}

	public static @NotNull ByteArrayDataInput newDataInput(byte[] data, int start) {
		return ByteStreams.newDataInput(data, start);
	}

	public static @NotNull ByteArrayDataOutput newDataOutput() {
		return ByteStreams.newDataOutput();
	}

	public static @NotNull ByteArrayDataOutput newDataOutput(ByteArrayOutputStream outputStream) {
		return ByteStreams.newDataOutput(outputStream);
	}

	public static @NotNull ByteArrayDataOutput newDataOutput(int size) {
		return ByteStreams.newDataOutput(size);
	}

	@SneakyThrows
	public static @NotNull FileWriter newFileWriter(@NotNull String fileName) {
		return new FileWriter(fileName);
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

	/**
	 * Defines the types of compression used in stream data.
	 */
	public enum CompressionType {

		/**
		 * No compression.
		 */
		NONE,

		/**
		 * GZIP compression ({@code GZIPInputStream} and {@code GZIPOutputStream}).
		 */
		GZIP,

		/**
		 * ZLIB compression ({@code InflaterInputStream} and {@code DeflaterOutputStream}).
		 */
		ZLIB

	}

}
