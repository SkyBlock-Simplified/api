package gg.sbs.api.data.sql;

import java.util.Map;

public class SqlModel {
    public static int getEffectOrZero(EffectFunction f, String s) {
        Double effect = (Double) f.returns().get(s);
        if (effect == null) return 0;
        else return effect.intValue();
    }

    public interface EffectFunction {
        Map<String, Object> returns();
    }
}
