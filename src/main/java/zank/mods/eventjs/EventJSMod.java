package zank.mods.eventjs;

import dev.latvian.mods.rhino.NativeJavaClass;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author ZZZank
 */
@Mod(EventJSMod.MOD_ID)
public class EventJSMod {

    public static final String MOD_ID = "eventjs";
    public static final String NAME = "EventJS";

    static IEventBus MOD_BUS;

    public EventJSMod() {
        MOD_BUS = FMLJavaModLoadingContext.get() == null
            ? null
            : FMLJavaModLoadingContext.get().getModEventBus();
    }

    public static Class<?> ofClass(Object o) {
        if (o instanceof CharSequence) {
            try {
                return Class.forName(o.toString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (o instanceof Class) {
            return (Class<?>) o;
        } else if (o instanceof NativeJavaClass) {
            return ((NativeJavaClass) o).getClassObject();
        }
        throw new IllegalArgumentException(String.format("'%s' is not a CharSequence/Class/NativeJavaClass", o));
    }
}
