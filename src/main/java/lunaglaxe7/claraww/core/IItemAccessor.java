package lunaglaxe7.claraww.core;

public interface IItemAccessor {

    Object getItem();

    public static IItemAccessor NONE = () -> null;
}
