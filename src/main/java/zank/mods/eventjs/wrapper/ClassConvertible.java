package zank.mods.eventjs.wrapper;

import dev.latvian.mods.rhino.NativeJavaClass;

import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public interface ClassConvertible extends Supplier<Class<?>> {

    static Class<?> of(Object o) {
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
        return null;
    }
}
