package lunaglaxe7.thaumtraveller.core;

import java.util.List;

public class DefaultTemplateMatching{
    public static final NaturalMatching defaultNaturalMatching = new NaturalMatching();
    public static final MagicalMatching defaultMagicalMatching = new MagicalMatching();

    public static class NaturalMatching implements ICraftTemplateMatching{

        @Override
        public boolean matches(List<IMaterialAccessor> input) {
            String material = null;
            int count = 0;
            String template = "";
            for (IMaterialAccessor o : input){
                if (count > 0) return false;
                String n = o.getMaterialName();
                if (!MaterialCates.isNaturalCapMaterial(n)){
                    return false;
                }
                if (material == null) {
                    material = n;
                }
                if (!n.equalsIgnoreCase("null")) {
                    if (!n.equalsIgnoreCase(material)){
                        count ++;
                    }
                    template = template.concat(String.valueOf(count));
                }else {
                    template = template.concat(" ");
                }
            }
            return template.equalsIgnoreCase("0000 0   ") || template.equalsIgnoreCase("   0000 0");
        }
    }

    public static class MagicalMatching implements ICraftTemplateMatching{
        @Override
        public boolean matches(List<IMaterialAccessor> input) {
            String material = null;
            int count = 0;
            String template = "";
            for (IMaterialAccessor o : input){
                if (count > 1) return false;
                String n = o.getMaterialName();
                if (!MaterialCates.isMagicalCapMaterial(n)){
                    return false;
                }
                if (material == null) {
                    material = n;
                }
                if (!n.equalsIgnoreCase("null")) {
                    if (!n.equalsIgnoreCase(material)){
                        count ++;
                    }
                    template = template.concat(String.valueOf(count));
                }else {
                    template = template.concat(" ");
                }
            }
            return template.equalsIgnoreCase("000010   ") || template.equalsIgnoreCase("   000010");
        }
    }
}
