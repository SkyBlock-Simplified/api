package dev.sbs.api.util.io;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An extension of {@code DataOutput} for writing to in-memory byte arrays; its methods offer
 * identical functionality but do not throw {@link IOException}.
 */
public interface ByteArrayDataOutput extends DataOutput {

    @Override
    void write(int b);

    @Override
    void write(byte @NotNull [] b);

    @Override
    void write(byte @NotNull [] b, int off, int len);

    @Override
    void writeBoolean(boolean v);

    @Override
    void writeByte(int v);

    @Override
    void writeShort(int v);

    @Override
    void writeChar(int v);

    @Override
    void writeInt(int v);

    @Override
    void writeLong(long v);

    @Override
    void writeFloat(float v);

    @Override
    void writeDouble(double v);

    @Override
    void writeChars(@NotNull String s);

    @Override
    void writeUTF(@NotNull String s);

    /**
     * @deprecated This method is dangerous as it discards the high byte of every character. For
     * UTF-8, use {@code write(s.getBytes(StandardCharsets.UTF_8))}.
     */
    @Deprecated
    @Override
    void writeBytes(@NotNull String s);

    /**
     * Returns the contents that have been written to this instance, as a byte array.
     */
    byte[] toByteArray();

    static @NotNull ByteArrayDataOutput of() {
        return of(new ByteArrayOutputStream());
    }

    static @NotNull ByteArrayDataOutput of(int size) {
        return of(new ByteArrayOutputStream(size));
    }

    static @NotNull ByteArrayDataOutput of(@NotNull ByteArrayOutputStream byteArrayOutputStream) {
        return new ByteArrayDataOutput.Impl(byteArrayOutputStream);
    }

    class Impl implements ByteArrayDataOutput {

        private final @NotNull DataOutput output;
        private final @NotNull ByteArrayOutputStream byteArrayOutputStream;

        Impl(@NotNull ByteArrayOutputStream byteArrayOutputStream) {
            this.byteArrayOutputStream = byteArrayOutputStream;
            this.output = new DataOutputStream(byteArrayOutputStream);
        }

        @SneakyThrows
        @Override
        public void write(int b) {
            this.output.write(b);
        }

        @SneakyThrows
        @Override
        public void write(byte @NotNull [] b) {
            this.output.write(b);
        }

        @SneakyThrows
        @Override
        public void write(byte @NotNull [] b, int off, int len) {
            this.output.write(b, off, len);
        }

        @SneakyThrows
        @Override
        public void writeBoolean(boolean v) {
            this.output.writeBoolean(v);
        }

        @SneakyThrows
        @Override
        public void writeByte(int v) {
            this.output.writeByte(v);
        }

        @SneakyThrows
        @Override
        public void writeBytes(@NotNull String s) {
            this.output.writeBytes(s);
        }

        @SneakyThrows
        @Override
        public void writeChar(int v) {
            this.output.writeChar(v);
        }

        @SneakyThrows
        @Override
        public void writeChars(@NotNull String s) {
            this.output.writeChars(s);
        }

        @SneakyThrows
        @Override
        public void writeDouble(double v) {
            this.output.writeDouble(v);
        }

        @SneakyThrows
        @Override
        public void writeFloat(float v) {
            this.output.writeFloat(v);
        }

        @SneakyThrows
        @Override
        public void writeInt(int v) {
            this.output.writeInt(v);
        }

        @SneakyThrows
        @Override
        public void writeLong(long v) {
            this.output.writeLong(v);
        }

        @SneakyThrows
        @Override
        public void writeShort(int v) {
            this.output.writeShort(v);
        }

        @SneakyThrows
        @Override
        public void writeUTF(@NotNull String s) {
            this.output.writeUTF(s);
        }

        @Override
        public byte[] toByteArray() {
            return this.byteArrayOutputStream.toByteArray();
        }
    }

}