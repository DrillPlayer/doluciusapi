package net.minecraft.network.protocol.game;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;

public class PacketPlayOutNBTQuery implements Packet<PacketListenerPlayOut> {

    private int a;
    @Nullable
    private NBTTagCompound b;

    public PacketPlayOutNBTQuery() {}

    public PacketPlayOutNBTQuery(int i, @Nullable NBTTagCompound nbttagcompound) {
        this.a = i;
        this.b = nbttagcompound;
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.i();
        this.b = packetdataserializer.l();
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.d(this.a);
        packetdataserializer.a(this.b);
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    @Override
    public boolean a() {
        return true;
    }
}
