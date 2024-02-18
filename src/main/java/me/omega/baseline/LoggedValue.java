package me.omega.baseline;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class LoggedValue<T> {

    public final LoggedClass loggedClass;
    private final String name;
    public final Log log;
    public final Supplier<T> getter;

    public LoggedValue parent;

    public LoggedValue(LoggedClass loggedClass, String name, Log log, Supplier<T> getter) {
        this.loggedClass = loggedClass;
        this.name = name;
        this.log = log;
        this.getter = getter;
    }

    public T getValue() {
        return getter.get();
    }

    public String getName() {
        if (Objects.equals(log.name(), "")) return name;
        return log.name();
    }

    public String getClassName() {
        return loggedClass.toString();
    }

    public String getParentString(Function<List<LoggedValue<?>>, String> transformer) {
        if (parent == null) return "Root";
        return transformer.apply(getParentList());
    }

    public String getParentString() {
        return getParentString(list -> {
            var builder = new StringBuilder();
            builder.append("Root -> ");
            for (int i = list.size() - 1; i >= 0; i--) {
                var value = list.get(i);
                builder.append(value.getName());
                if (i != 0) builder.append(" -> ");
            }
            return builder.toString();
        });
    }

    public List<LoggedValue<?>> getParentList() {
        var list = new ArrayList<LoggedValue<?>>();
        var current = parent;
        while (current != null) {
            list.add(current);
            current = current.parent;
        }
        return list;
    }

    public void setParent(LoggedValue parent) {
        this.parent = parent;
    }

}
