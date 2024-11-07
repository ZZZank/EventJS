package zank.mods.eventjs;

import dev.latvian.mods.kubejs.script.ScriptType;
import zank.mods.eventjs.wrapper.WrappedEventHandler;
import dev.latvian.mods.rhino.util.HideFromJS;
import lombok.val;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import zank.mods.eventjs.wrapper.ClassConvertible;
import zank.mods.eventjs.wrapper.WrappedGenericEventHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public final class SidedNativeEvents {
    private static final EnumMap<ScriptType, SidedNativeEvents> BY_TYPE = new EnumMap<>(ScriptType.class);

    public static final SidedNativeEvents STARTUP = new SidedNativeEvents(ScriptType.STARTUP);
    public static final SidedNativeEvents SERVER = new SidedNativeEvents(ScriptType.SERVER);
    public static final SidedNativeEvents CLIENT = new SidedNativeEvents(ScriptType.CLIENT);

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

    public void onEvent(final ClassConvertible type, final WrappedEventHandler handler) {
        onEvent(EventPriority.NORMAL, false, type, handler);
    }

    public void onEvent(
        final EventPriority priority,
        final boolean receiveCancelled,
        final ClassConvertible type,
        final WrappedEventHandler handler
    ) {
        val eventType = (Class<Event>) type.get();
        if (!Event.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException(String.format("Event class must be a subclass of '%s'", Event.class));
        }
        onEventTyped(priority, receiveCancelled, eventType, handler);
    }

    <T extends Event> void onEventTyped(
        EventPriority priority,
        boolean receiveCancelled,
        Class<T> eventType,
        Consumer<T> handler
    ) {
        final Consumer<T> safed = event -> {
            try {
                handler.accept(event);
            } catch (Exception e) {
                this.type.console.error("Error when handling native event", e);
            }
        };
        handlers.add(safed);
        EventJSMod.selectBus(eventType).addListener(priority, receiveCancelled, eventType, safed);
    }

    public void onGenericEvent(
        final ClassConvertible genericClassFilter,
        final ClassConvertible type,
        final WrappedGenericEventHandler handler
    ) {
        onGenericEvent(genericClassFilter, EventPriority.NORMAL, false, type, handler);
    }

    public void onGenericEvent(
        final ClassConvertible genericClassFilter,
        final EventPriority priority,
        final boolean receiveCancelled,
        final ClassConvertible type,
        final WrappedGenericEventHandler handler
    ) {
        val eventType = (Class<GenericEvent>) type.get();
        if (!GenericEvent.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException(String.format("Event class must be a subclass of '%s'", GenericEvent.class));
        }
        onGenericEventTyped(genericClassFilter.get(), priority, receiveCancelled, eventType, handler);
    }

    <T extends GenericEvent<? extends F>, F> void onGenericEventTyped(
        Class<F> genericClassFilter,
        EventPriority priority,
        boolean receiveCancelled,
        Class<T> eventType,
        Consumer<T> handler
    ) {
        final Consumer<T> safed = event -> {
            try {
                handler.accept(event);
            } catch (Exception e) {
                this.type.console.error("Error when handling native generic event", e);
            }
        };
        handlers.add(safed);
        EventJSMod.selectBus(eventType).addGenericListener(
            genericClassFilter,
            priority,
            receiveCancelled,
            eventType,
            safed
        );
    }
}
