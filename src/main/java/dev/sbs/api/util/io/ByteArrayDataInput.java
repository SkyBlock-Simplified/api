package dev.sbs.api.util.io;

import dev.sbs.api.util.helper.Preconditions;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * An extension of {@code DataInput} for reading from in-memory byte arrays; its methods offer
 * identical functionality but do not throw {@link IOException}.
 *
 * <p><b>Warning:</b> The caller is responsible for not attempting to read past the end of the
 * array. If any method encounters the end of the array prematurely, it throws {@link
 * IllegalStateException} to signify <i>programmer error</i>. This behavior is a technical violation
 * of the supertype's contract, which specifies a checked exception.
 */
public interface ByteArrayDataInput extends DataInput {

    @Override
    void readFully(byte @NotNull [] b);

    @Override
    void readFully(byte @NotNull [] b, int off, int len);

    // not guaranteed to skip n bytes so result should NOT be ignored
    // use ByteStreams.skipFully or one of the read methods instead
    @Override
    int skipBytes(int n);

    @Override
    boolean readBoolean();

    @Override
    byte readByte();

    @Override
    int readUnsignedByte();

    @Override
    short readShort();

    @Override
    int readUnsignedShort();

    @Override
    char readChar();

    @Override
    int readInt();

    @Override
    long readLong();

    @Override
    float readFloat();

    @Override
    double readDouble();

    @Override
    @NotNull String readLine();

    @Override
    @NotNull String readUTF();

    static @NotNull ByteArrayDataInput of(byte[] bytes) {
        return of(new ByteArrayInputStream(bytes));
    }

    static @NotNull ByteArrayDataInput of(byte[] bytes, int start) {
        Preconditions.checkPositionIndex(start, bytes.length);
        return of(new ByteArrayInputStream(bytes, start, bytes.length - start));
    }

    static @NotNull ByteArrayDataInput of(@NotNull ByteArrayInputStream byteArrayInputStream) {
        return new Impl(byteArrayInputStream);
    }

    class Impl implements ByteArrayDataInput {

        final DataInput input;

        Impl(ByteArrayInputStream byteArrayInputStream) {
            this.input = new DataInputStream(byteArrayInputStream);
        }

        @SneakyThrows
        @Override
        public void readFully(byte @NotNull [] b) {
            input.readFully(b);
        }

        @SneakyThrows
        @Override
        public void readFully(byte @NotNull [] b, int off, int len) {
            input.readFully(b, off, len);
        }

        @SneakyThrows
        @Override
        public int skipBytes(int n) {
            return input.skipBytes(n);
        }

        @SneakyThrows
        @Override
        public boolean readBoolean() {
            return input.readBoolean();
        }

        @SneakyThrows
        @Override
        public byte readByte() {
            return input.readByte();
        }

        @SneakyThrows
        @Override
        public int readUnsignedByte() {
            return input.readUnsignedByte();
        }

        @SneakyThrows
        @Override
        public short readShort() {
            return input.readShort();
        }

        @SneakyThrows
        @Override
        public int readUnsignedShort() {
            return input.readUnsignedShort();
        }

        @SneakyThrows
        @Override
        public char readChar() {
            return input.readChar();
        }

        @SneakyThrows
        @Override
        public int readInt() {
            return input.readInt();
        }

        @SneakyThrows
        @Override
        public long readLong() {
            return input.readLong();
        }

        @SneakyThrows
        @Override
        public float readFloat() {
            return input.readFloat();
        }

        @SneakyThrows
        @Override
        public double readDouble() {
            return input.readDouble();
        }

        @SneakyThrows
        @Override
        public @NotNull String readLine() {
            return input.readLine();
        }

        @SneakyThrows
        @Override
        public @NotNull String readUTF() {
            return input.readUTF();
        }

    }

}
