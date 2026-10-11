package lunaglaxe7.thaumtraveller.core;

public interface IItemAccessor {
    Object getItem();

    public static IItemAccessor NONE = () -> null;
}
