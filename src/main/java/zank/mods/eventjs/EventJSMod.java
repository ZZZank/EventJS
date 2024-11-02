package zank.mods.eventjs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * @author ZZZank
 */
@Mod(EventJSMod.MOD_ID)
public class EventJSMod {

    public static final String MOD_ID = "eventjs";
    public static final String NAME = "EventJS";

    static IEventBus MOD_BUS;

    public EventJSMod(IEventBus bus) {
        MOD_BUS = bus;
    }

    public static IEventBus selectBus(Class<? extends Event> eventType) {
        return IModBusEvent.class.isAssignableFrom(eventType)
            ? MOD_BUS
            : MinecraftForge.EVENT_BUS;
    }
}
