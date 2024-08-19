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

public record ScheduledBlockUpdatePacket(UpdateData[] data) implements Packet<ClientPlayPacketListener> {

    public static final PacketType<ScheduledBlockUpdatePacket> PACKET_TYPE = new PacketType<>(NetworkSide.CLIENTBOUND, Identifier.of(Update_visualizer.MODID, "blockupdate"));

    public static final PacketCodec<PacketByteBuf, ScheduledBlockUpdatePacket> CODEC = Packet.createCodec(ScheduledBlockUpdatePacket::write, ScheduledBlockUpdatePacket::new);

    public ScheduledBlockUpdatePacket(PacketByteBuf buf) {
        this(readData(buf));
    }

    private static UpdateData[] readData(PacketByteBuf buf) {
        int len = buf.readInt();
        UpdateData[] dt = new UpdateData[len];
        for(int i = 0; i < len; i++) {
            dt[i] = new UpdateData(buf.readBlockPos(), new Color(buf.readInt()));
        }
        return dt;
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(data.length);
        for (UpdateData dta : data) {
            buf.writeBlockPos(dta.pos);
            buf.writeInt(dta.c.getRGB());
        }
    }

    @Override
    public PacketType<? extends Packet<ClientPlayPacketListener>> getPacketId() {
        return PACKET_TYPE;
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {
        for (UpdateData datum : data) {
            BoxRenderer.putBox(datum.pos, datum.c);
        }
    }

    public record UpdateData(BlockPos pos, Color c) {}
}
