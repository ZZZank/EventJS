package zank.mods.eventjs.wrapper;

import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
@FunctionalInterface
public interface WrappedEventHandler extends Consumer<Event> {
}
