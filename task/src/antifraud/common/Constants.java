package antifraud.common;

import java.util.Map;
import java.util.Set;

public class Constants {


    public static final long INITIAL_ALLOWED_THRESHOLD = 200L;
    public static final long INITIAL_PROHIBITED_LIMIT = 1500L;

    public static final Map<String, RoleEnum> ENUM_MAP = Map.of(
            "MERCHANT", RoleEnum.ROLE_MERCHANT,
            "SUPPORT", RoleEnum.ROLE_SUPPORT
    );


    /*
    Code	Description
    EAP	East Asia and Pacific
    ECA	Europe and Central Asia
    HIC	High-Income countries
    LAC	Latin America and the Caribbean
    MENA	The Middle East and North Africa
    SA	South Asia
    SSA	Sub-Saharan Africa
     */

    public static final Set<String> REGION_SET = Set.of
            ("EAP", "ECA", "HIC", "LAC", "MENA", "SA", "SSA");

    public static final String INFO_AMOUNT = "amount";
    public static final String INFO_FORBIDDEN_CARD = "card-number";
    public static final String INFO_FORBIDDEN_IP = "ip";
    public static final String INFO_IP_CORRELATION = "ip-correlation";
    public static final String INFO_REGION_CORRELATION = "region-correlation";
    public static final String INFO_ALLOWED = "none";


}
