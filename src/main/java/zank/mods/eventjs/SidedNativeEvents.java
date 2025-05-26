package zank.mods.eventjs;

import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.HideFromJS;
import lombok.val;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import zank.mods.eventjs.wrapper.ClassConvertible;

import javax.annotation.Nonnull;
import java.util.*;
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

    private final List<PackedHandler<?>> packedHandlers = new ArrayList<>();
    public final ScriptType type;

    private SidedNativeEvents(ScriptType type) {
        this.type = type;
        BY_TYPE.put(type, this);
    }

    @HideFromJS
    public void unload() {
        for (val packed : packedHandlers) {
            packed.bus.unregister(packed);
        }
        packedHandlers.clear();
    }

    public <T extends Event> void onEvent(Class<T> type, Consumer<T> handler) {
        onEvent(EventPriority.NORMAL, false, type, handler);
    }

    public <T extends Event> void onEvent(
        EventPriority priority,
        boolean receiveCancelled,
        Class<T> eventType,
        Consumer<T> handler
    ) {
        if (!Event.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException(String.format("Event class must be a subclass of '%s'", Event.class));
        }
        onEventTyped(priority, receiveCancelled, eventType, handler);
    }

    public <T extends Event> void onEventTyped(
        EventPriority priority,
        boolean receiveCancelled,
        Class<T> eventType,
        Consumer<T> handler
    ) {
        val packed = new PackedHandler<>(EventJSMod.selectBus(eventType), handler);
        packedHandlers.add(packed);
        packed.bus.addListener(priority, receiveCancelled, eventType, packed);
    }

    public <F, T extends GenericEvent<? extends F>> void onGenericEvent(
        final Class<F> genericClassFilter,
        final Class<T> eventType,
        final Consumer<T> handler
    ) {
        onGenericEvent(genericClassFilter, EventPriority.NORMAL, false, eventType, handler);
    }

    public <F, T extends GenericEvent<? extends F>> void onGenericEvent(
        final Class<F> genericClassFilter,
        final EventPriority priority,
        final boolean receiveCancelled,
        final Class<T> eventType,
        final Consumer<T> handler
    ) {
        if (!GenericEvent.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException(String.format("Event class must be a subclass of '%s'", GenericEvent.class));
        }
        onGenericEventTyped(genericClassFilter, priority, receiveCancelled, eventType, handler);
    }

    public <T extends GenericEvent<? extends F>, F> void onGenericEventTyped(
        Class<F> genericClassFilter,
        EventPriority priority,
        boolean receiveCancelled,
        Class<T> eventType,
        Consumer<T> handler
    ) {
        val packed = new PackedHandler<>(EventJSMod.selectBus(eventType), handler);
        packedHandlers.add(packed);
        packed.bus.addGenericListener(genericClassFilter, priority, receiveCancelled, eventType, packed);
    }

    private final class PackedHandler<T> implements Consumer<T> {
        private final IEventBus bus;
        private final Consumer<T> inner;

        public PackedHandler(IEventBus bus, Consumer<T> inner) {
            this.bus = Objects.requireNonNull(bus);
            this.inner = Objects.requireNonNull(inner);
        }

        @Override
        public void accept(T event) {
            try {
                inner.accept(event);
            } catch (Exception e) {
                SidedNativeEvents.this.type.console.error("Error when handling native event", e);
            }
        }
    }

    public int getHandlerCount() {
        return packedHandlers.size();
    }
}
