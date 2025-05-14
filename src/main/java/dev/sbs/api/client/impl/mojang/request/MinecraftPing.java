package dev.sbs.api.client.impl.mojang.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.impl.mojang.response.MinecraftPingResponse;
import dev.sbs.api.minecraft.text.segment.TextSegment;
import dev.sbs.api.util.io.ByteArrayDataOutput;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class MinecraftPing {

    // https://github.com/lucaazalim/minecraft-server-ping
    public static final byte PACKET_HANDSHAKE = 0x00;
    public static final byte PACKET_STATUSREQUEST = 0x00;
    public static final byte PACKET_PING = 0x01;
    public static final int PROTOCOL_VERSION = 4;
    public static final int STATUS_HANDSHAKE = 1;

    /**
     * Fetches a {@link MinecraftPingResponse} for the supplied hostname.
     * <b>Assumed timeout of 2s and port of 25565.</b>
     *
     * @param hostname A valid hostname
     * @return {@link MinecraftPingResponse}
     */
    public static MinecraftPingResponse getPing(final String hostname) {
        return getPing(hostname, 25565, 2000);
    }

    /**
     * Fetches a {@link MinecraftPingResponse} for the supplied options.
     *
     * @param hostname A valid hostname.
     * @param port Minecraft server port.
     * @param timeout Timeout in milliseconds for the connection to complete.
     * @return {@link MinecraftPingResponse}
     */
    @SneakyThrows
    public static MinecraftPingResponse getPing(@NotNull String hostname, int port, int timeout) {
        @Cleanup Socket socket = new Socket();
        long start = System.currentTimeMillis();
        socket.connect(new InetSocketAddress(hostname, port), timeout);
        long ping = System.currentTimeMillis() - start;

        @Cleanup DataInputStream in = new DataInputStream(socket.getInputStream());
        @Cleanup DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        //> Handshake
        @Cleanup ByteArrayDataOutput handshake = new ByteArrayDataOutput();
        handshake.writeByte(PACKET_HANDSHAKE);
        writeVarInt(handshake, PROTOCOL_VERSION);
        writeVarInt(handshake, hostname.length());
        handshake.writeBytes(hostname);
        handshake.writeShort(port);
        writeVarInt(handshake, STATUS_HANDSHAKE);

        writeVarInt(out, handshake.size());
        out.write(handshake.toByteArray());

        //> Status request
        out.writeByte(0x01); // Size of packet
        out.writeByte(PACKET_STATUSREQUEST);

        //< Status response
        readVarInt(in); // Size
        int id = readVarInt(in);
        if (id == -1) throw new IOException("Server prematurely ended stream.");
        if (id != PACKET_STATUSREQUEST) throw new IOException("Server returned invalid packet.");

        int length = readVarInt(in);
        if (length == -1) throw new IOException("Server prematurely ended stream.");
        if (length == 0) throw new IOException("Server returned invalid packet.");

        byte[] data = new byte[length];
        in.readFully(data);
        String json = new String(data, StandardCharsets.UTF_8);

        //> Ping
        out.writeByte(0x09); // Size of packet
        out.writeByte(PACKET_PING);
        out.writeLong(System.currentTimeMillis());

        //< Ping
        readVarInt(in); // Size
        id = readVarInt(in);
        if (id == -1) throw new IOException("Server prematurely ended stream.");
        if (id != PACKET_PING) throw new IOException("Server returned invalid packet.");

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonElement descriptionJsonElement = jsonObject.get("description");

        if (descriptionJsonElement.isJsonObject()) {
            // For those versions that work with TextComponent MOTDs
            JsonObject descriptionJsonObject = jsonObject.get("description").getAsJsonObject();

            if (descriptionJsonObject.has("extra")) {
                TextSegment textSegment = TextSegment.fromJson(descriptionJsonObject.get("extra").getAsJsonObject());

                if (textSegment != null)
                    descriptionJsonObject.addProperty("text", textSegment.toLegacy());

                jsonObject.add("description", descriptionJsonObject);
            }
        } else {
            // For those versions that work with String MOTDs
            String description = descriptionJsonElement.getAsString();
            JsonObject descriptionJsonObject = new JsonObject();
            descriptionJsonObject.addProperty("text", description);
            jsonObject.add("description", descriptionJsonObject);

        }

        MinecraftPingResponse output = SimplifiedApi.getGson().fromJson(jsonObject, MinecraftPingResponse.class);
        output.setPing(ping);

        return output;
    }

    private static int readVarInt(DataInput dataInput) throws IOException {
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

    private static void writeVarInt(DataOutput out, int paramInt) throws IOException {
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
