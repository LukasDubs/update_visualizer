package trdps.update_visualizer.network.packet;

import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import trdps.update_visualizer.Update_visualizer;
import trdps.update_visualizer.render.BoxRenderer;

import java.awt.*;

public record ScheduledBlockUpdatePacket(BlockPos pos, Color color) implements Packet<ClientPlayPacketListener> {

    public static final PacketType<ScheduledBlockUpdatePacket> PACKET_TYPE = new PacketType<>(NetworkSide.CLIENTBOUND, Identifier.of(Update_visualizer.MODID, "blockupdate"));

    public static final PacketCodec<PacketByteBuf, ScheduledBlockUpdatePacket> CODEC = Packet.createCodec(ScheduledBlockUpdatePacket::write, ScheduledBlockUpdatePacket::new);

    public ScheduledBlockUpdatePacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), new Color(buf.readInt(), true));
    }


    private void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(color.getRGB());
    }

    @Override
    public PacketType<? extends Packet<ClientPlayPacketListener>> getPacketId() {
        return PACKET_TYPE;
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {
        BoxRenderer.putBox(pos, color);
    }
}
