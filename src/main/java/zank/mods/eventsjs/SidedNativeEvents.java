package zank.mods.eventsjs;

import dev.latvian.kubejs.forge.KubeJSForgeEventHandlerWrapper;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.HideFromJS;
import lombok.val;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import zank.mods.eventsjs.wrapper.ClassConvertible;
import zank.mods.eventsjs.wrapper.WrappedGenericEventHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * @author ZZZank
 */
public final class SidedNativeEvents {
    public static final SidedNativeEvents STARTUP = new SidedNativeEvents(ScriptType.STARTUP);
    public static final SidedNativeEvents SERVER = new SidedNativeEvents(ScriptType.SERVER);
    public static final SidedNativeEvents CLIENT = new SidedNativeEvents(ScriptType.CLIENT);

    private static final EnumMap<ScriptType, SidedNativeEvents> BY_TYPE = new EnumMap<>(ScriptType.class);

    public static SidedNativeEvents byType(@Nonnull ScriptType type) {
        return BY_TYPE.get(type);
    }

    private final List<Object> handlers = new ArrayList<>();
    public final ScriptType type;

    private SidedNativeEvents(ScriptType type) {
        this.type = type;
        BY_TYPE.put(type, this);
    }

    @HideFromJS
    public void unload() {
        for (val handler : handlers) {
            MinecraftForge.EVENT_BUS.unregister(handler);
        }
        handlers.clear();
    }

    public void onEvent(ClassConvertible type, KubeJSForgeEventHandlerWrapper handler) {
        onEvent(EventPriority.NORMAL, false, type, handler);
    }

    public void onEvent(
        EventPriority priority,
        boolean receiveCancelled,
        ClassConvertible type,
        KubeJSForgeEventHandlerWrapper handler
    ) {
        val eventType = type.get();
        if (!Event.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException(String.format("Event class must be a subclass of '%s'", Event.class));
        }
        handlers.add(handler);
        MinecraftForge.EVENT_BUS.addListener(
            priority,
            receiveCancelled,
            (Class<Event>) eventType,
            handler
        );
    }

    public void onGenericEvent(
        ClassConvertible genericClassFilter,
        EventPriority priority,
        boolean receiveCancelled,
        ClassConvertible type,
        WrappedGenericEventHandler handler
    ) {
        val eventType = (Class<GenericEvent>) type.get();
        if (!GenericEvent.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException(String.format("Event class must be a subclass of '%s'", GenericEvent.class));
        }
        handlers.add(handler);
        MinecraftForge.EVENT_BUS.addGenericListener(
            (Class<Object>) genericClassFilter.get(),
            priority,
            receiveCancelled,
            eventType,
            handler
        );
    }

    public void onGenericEvent(
        ClassConvertible genericClassFilter,
        ClassConvertible type,
        WrappedGenericEventHandler handler
    ) {
        onGenericEvent(genericClassFilter, EventPriority.NORMAL, false, type, handler);
    }
}
