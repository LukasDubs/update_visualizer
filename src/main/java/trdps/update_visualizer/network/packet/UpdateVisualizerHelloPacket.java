package trdps.update_visualizer.network.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record UpdateVisualizerHelloPacket(int version) implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, UpdateVisualizerHelloPacket> CODEC = CustomPayload.codecOf(UpdateVisualizerHelloPacket::write, UpdateVisualizerHelloPacket::new);
    public static final Identifier IDENTIFIER = Identifier.ofVanilla("update_visualizer/hello");
    public static final Id<UpdateVisualizerHelloPacket> ID = new CustomPayload.Id<>(IDENTIFIER);

    public UpdateVisualizerHelloPacket(PacketByteBuf buf) {
        this(buf.readInt());
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(version);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
