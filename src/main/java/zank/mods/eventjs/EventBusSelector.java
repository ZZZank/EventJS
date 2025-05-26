package zank.mods.eventjs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * @author ZZZank
 */
public enum EventBusSelector {
    AUTO,
    FORGE,
    MOD;

    public IEventBus selectBus(Class<? extends Event> eventType) {
        switch (this) {
            case MOD:
                return EventJSMod.MOD_BUS;
            case FORGE:
                return MinecraftForge.EVENT_BUS;
            case AUTO:
                return IModBusEvent.class.isAssignableFrom(eventType)
                    ? EventJSMod.MOD_BUS
                    : MinecraftForge.EVENT_BUS;
            default:
                throw new IllegalStateException("unknown bus selector: " + this);
        }
    }
}
