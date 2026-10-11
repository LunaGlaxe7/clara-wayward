package lunaglaxe7.thaumtraveller.core;

import java.util.List;

public interface ICraftTemplateMatching {
    boolean matches(List<IMaterialAccessor> input);
}
