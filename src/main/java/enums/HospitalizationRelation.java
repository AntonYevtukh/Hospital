package enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum HospitalizationRelation {
    SINGLE, INITIAL, INTERMEDIATE, DISCHARGE;

    private static Map<Integer, HospitalizationRelation> ordinalMap;
    static {
        ordinalMap = Arrays.stream(HospitalizationRelation.values()).collect(Collectors.toMap(
                (HospitalizationRelation hospitalizationRelation) -> hospitalizationRelation.ordinal(),
                Function.identity()
        ));
    }

    public static HospitalizationRelation getValueByOrdinal(int ordinal) {
        return ordinalMap.get(ordinal);
    }

    public int getOrdinal() {
        return ordinal();
    }
}
