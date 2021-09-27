package gg.sbs.api.data.sql;

import java.util.Map;

public class SqlModel {
    public static double getEffectOrZero(EffectFunction f, String s) {
        try{
            Double effect = (Double) f.returns().get(s);
            if (effect == null) return 0.;
            return effect;
        } catch (Exception e) {
            return 0.; // NaN
        }
    }

    public interface EffectFunction {
        Map<String, Object> returns();
    }
}
