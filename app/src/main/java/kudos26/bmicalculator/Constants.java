package kudos26.bmicalculator;

public class Constants {

    public static final int INT_ZERO = 0;
    public static final int INT_ONE = 1;
    public static final int INT_TWO = 2;
    public static final int INT_THREE = 3;
    public static final int INT_FOUR = 4;
    public static final int INT_FIVE = 5;
    public static final int INT_SIX = 6;
    public static final int INT_SEVEN = 7;
    public static final int INT_EIGHT = 8;
    public static final int INT_NINE = 9;

    public static final String STRING_ZERO = "0";
    public static final String STRING_ONE = "1";
    public static final String STRING_TWO = "2";
    public static final String STRING_THREE = "3";
    public static final String STRING_FOUR = "4";
    public static final String STRING_FIVE = "5";
    public static final String STRING_SIX = "6";
    public static final String STRING_SEVEN = "7";
    public static final String STRING_EIGHT = "8";
    public static final String STRING_NINE = "9";
    public static final String STRING_DECIMAL = ".";

    public static final String PARAMETER_WEIGHT = "Weight";
    public static final String PARAMETER_HEIGHT = "Height";

    public static final String PARAMETER_WEIGHT_UNIT_KILOGRAM = "Kilogram";
    public static final String PARAMETER_WEIGHT_UNIT_POUND = "Pound";

    public static final String PARAMETER_HEIGHT_UNIT_CENTIMETER = "Centimeter";
    public static final String PARAMETER_HEIGHT_UNIT_METER = "Meter";
    public static final String PARAMETER_HEIGHT_UNIT_FEET = "Feet";
    public static final String PARAMETER_HEIGHT_UNIT_INCH = "Inch";

    public static final String PARAMETER_WEIGHT_DEFAULT_UNIT = PARAMETER_WEIGHT_UNIT_KILOGRAM;
    public static final String PARAMETER_HEIGHT_DEFAULT_UNIT = PARAMETER_HEIGHT_UNIT_METER;

    public static final int PARAMETER_WEIGHT_KILOGRAM_DEFAULT_VALUE = 60;
    public static final float PARAMETER_HEIGHT_METER_DEFAULT_VALUE = 1.7f;

    public static final float POUND_TO_KILOGRAM = 0.453592f;
    public static final float CENTIMETER_TO_METER = 0.01f;
    public static final float FEET_TO_METER = 0.3048f;
    public static final float INCH_TO_METER = 0.0254f;

    public static final float[] RANGE_UNDERWEIGHT = {16.0f, 18.5f};
    public static final float[] RANGE_NORMAL = {18.5f, 25.0f};
    public static final float[] RANGE_OVER_WEIGHT = {25.0f, 40.0f};

    public static final String BMI_UNDERWEIGHT = "Underweight";
    public static final String BMI_NORMAL = "Normal";
    public static final String BMI_OVER_WEIGHT = "Over weight";

    public static final String ERROR_INVALID_BMI = "Invalid BMI! Please check your input";

    public static final String KEY_BMI_VALUE = "BMI";
    public static final String KEY_NUM_PAD_VISIBILITY = "Num Pad Visibility";
    public static final String KEY_RESULT_CARD_VISIBILITY = "Result Card Visibility";
    public static final String KEY_PARAMETER_FOCUS = "Parameter Focus";
}
