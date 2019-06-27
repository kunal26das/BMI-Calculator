package kudos26.bmicalculator;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams;
import static kudos26.bmicalculator.Constants.BMI_NORMAL;
import static kudos26.bmicalculator.Constants.BMI_OVER_WEIGHT;
import static kudos26.bmicalculator.Constants.BMI_UNDERWEIGHT;
import static kudos26.bmicalculator.Constants.CENTIMETER_TO_FEET;
import static kudos26.bmicalculator.Constants.CENTIMETER_TO_INCH;
import static kudos26.bmicalculator.Constants.CENTIMETER_TO_METER;
import static kudos26.bmicalculator.Constants.ERROR_INVALID_BMI;
import static kudos26.bmicalculator.Constants.FEET_TO_CENTIMETER;
import static kudos26.bmicalculator.Constants.FEET_TO_INCH;
import static kudos26.bmicalculator.Constants.FEET_TO_METER;
import static kudos26.bmicalculator.Constants.HEIGHT_DEFAULT_UNIT;
import static kudos26.bmicalculator.Constants.HEIGHT_METER_DEFAULT_VALUE;
import static kudos26.bmicalculator.Constants.HEIGHT_UNIT_CENTIMETER;
import static kudos26.bmicalculator.Constants.HEIGHT_UNIT_FEET;
import static kudos26.bmicalculator.Constants.HEIGHT_UNIT_INCH;
import static kudos26.bmicalculator.Constants.HEIGHT_UNIT_METER;
import static kudos26.bmicalculator.Constants.INCH_TO_CENTIMETER;
import static kudos26.bmicalculator.Constants.INCH_TO_FEET;
import static kudos26.bmicalculator.Constants.INCH_TO_METER;
import static kudos26.bmicalculator.Constants.INT_FOUR;
import static kudos26.bmicalculator.Constants.INT_ONE;
import static kudos26.bmicalculator.Constants.INT_SIX;
import static kudos26.bmicalculator.Constants.INT_THREE;
import static kudos26.bmicalculator.Constants.INT_ZERO;
import static kudos26.bmicalculator.Constants.KEY_BMI_VALUE;
import static kudos26.bmicalculator.Constants.KEY_FOCUS_HEIGHT;
import static kudos26.bmicalculator.Constants.KEY_HEIGHT_UNIT;
import static kudos26.bmicalculator.Constants.KEY_HEIGHT_VALUE;
import static kudos26.bmicalculator.Constants.KEY_VISIBILITY_RESULT;
import static kudos26.bmicalculator.Constants.KEY_WEIGHT_UNIT;
import static kudos26.bmicalculator.Constants.KEY_WEIGHT_VALUE;
import static kudos26.bmicalculator.Constants.KILOGRAM_TO_POUND;
import static kudos26.bmicalculator.Constants.METER_TO_CENTIMETER;
import static kudos26.bmicalculator.Constants.METER_TO_FEET;
import static kudos26.bmicalculator.Constants.METER_TO_INCH;
import static kudos26.bmicalculator.Constants.PARAMETER_HEIGHT;
import static kudos26.bmicalculator.Constants.PARAMETER_WEIGHT;
import static kudos26.bmicalculator.Constants.POUND_TO_KILOGRAM;
import static kudos26.bmicalculator.Constants.RANGE_NORMAL;
import static kudos26.bmicalculator.Constants.RANGE_OVER_WEIGHT;
import static kudos26.bmicalculator.Constants.RANGE_UNDERWEIGHT;
import static kudos26.bmicalculator.Constants.STRING_DECIMAL;
import static kudos26.bmicalculator.Constants.STRING_ZERO;
import static kudos26.bmicalculator.Constants.WEIGHT_DEFAULT_UNIT;
import static kudos26.bmicalculator.Constants.WEIGHT_KILOGRAM_DEFAULT_VALUE;
import static kudos26.bmicalculator.Constants.WEIGHT_UNIT_KILOGRAM;
import static kudos26.bmicalculator.Constants.WEIGHT_UNIT_POUND;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends AppCompatActivity {

    private View mResult;
    private View mNumPad;
    private TextView mFocusInput;
    private TextView mWeightUnit;
    private TextView mHeightUnit;
    private TextView mWeightValue;
    private TextView mHeightValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mNumPad = findViewById(R.id.num_pad);
        mResult = findViewById(R.id.result_card);
        final ImageView backButton = findViewById(R.id.back_button);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MainActivity.this.finish();
                return true;
            }
        });
        setupHeightFunctionality();
        setupWeightFunctionality();
        setupNumPadFunctionality();
        restoreSavedInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mResult.getVisibility() == View.VISIBLE) {
            outState.putInt(KEY_VISIBILITY_RESULT, View.VISIBLE);
            TextView bmiValueTextView = mResult.findViewById(R.id.result_bmi_value);
            outState.putString(KEY_BMI_VALUE, bmiValueTextView.getText().toString());
        } else {
            outState.putInt(KEY_VISIBILITY_RESULT, View.INVISIBLE);
        }

        outState.putString(KEY_WEIGHT_UNIT, mWeightUnit.getText().toString());
        outState.putString(KEY_HEIGHT_UNIT, mHeightUnit.getText().toString());
        outState.putString(KEY_WEIGHT_VALUE, mWeightValue.getText().toString());
        outState.putString(KEY_HEIGHT_VALUE, mHeightValue.getText().toString());

        outState.putBoolean(KEY_FOCUS_HEIGHT, false);
        if (mFocusInput == mHeightValue) {
            outState.putBoolean(KEY_FOCUS_HEIGHT, true);
        }
    }

    private void restoreSavedInstanceState(Bundle inState) {
        if (inState != null) {
            if (inState.getBoolean(KEY_FOCUS_HEIGHT)) {
                focusHeightInput(0);
            } else {
                focusWeightInput(0);
            }

            mWeightUnit.setText(inState.getString(KEY_WEIGHT_UNIT));
            mHeightUnit.setText(inState.getString(KEY_HEIGHT_UNIT));
            mWeightValue.setText(inState.getString(KEY_WEIGHT_VALUE));
            mHeightValue.setText(inState.getString(KEY_HEIGHT_VALUE));

            if (inState.getInt(KEY_VISIBILITY_RESULT) == View.VISIBLE) {
                showBMI(inState.getString(KEY_BMI_VALUE));
            } else {
                showNumPad();
            }
        }
    }

    private void focusWeightInput(float weight) {
        mFocusInput = mWeightValue;
        if (weight == 0) {
            mWeightValue.setText(STRING_ZERO);
        } else {
            mWeightValue.setText(String.valueOf(roundFloat(weight)));
        }
        mWeightValue.setTextColor(getResources().getColor(R.color.colorAccent));
        mHeightValue.setTextColor(getResources().getColor(R.color.black));
        showNumPad();
    }

    private void focusHeightInput(float height) {
        mFocusInput = mHeightValue;
        if (height == 0) {
            mHeightValue.setText(STRING_ZERO);
        } else {
            mHeightValue.setText(String.valueOf(roundFloat(height)));
        }
        mHeightValue.setTextColor(getResources().getColor(R.color.colorAccent));
        mWeightValue.setTextColor(getResources().getColor(R.color.black));
        showNumPad();
    }

    private void showNumPad() {
        mNumPad.setVisibility(View.VISIBLE);
        mResult.setVisibility(View.INVISIBLE);
    }

    private void setupWeightFunctionality() {
        final View weightParameter = findViewById(R.id.parameter_weight);
        final TextView parameterType = weightParameter.findViewById(R.id.parameter_type);
        final LinearLayout weightInfo = weightParameter.findViewById(R.id.parameter_info);
        final LinearLayout weightUnitSelector = weightParameter.findViewById(R.id.parameter_unit_selector);
        mWeightValue = weightParameter.findViewById(R.id.parameter_value);
        mWeightUnit = weightParameter.findViewById(R.id.parameter_unit);

        parameterType.setText(PARAMETER_WEIGHT);
        mWeightUnit.setText(WEIGHT_DEFAULT_UNIT);
        mWeightValue.setText(String.valueOf(WEIGHT_KILOGRAM_DEFAULT_VALUE));
        mWeightValue.setTextColor(getResources().getColor(R.color.colorAccent));

        final AlertDialog weightUnitSelectorDialog = new AlertDialog.Builder(this)
                .setItems(getResources().getStringArray(R.array.weight_units), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        String currentUnit = mWeightUnit.getText().toString();
                        float currentValue = Float.valueOf(mWeightValue.getText().toString());
                        String selectedUnit = getResources().getStringArray(R.array.weight_units)[position];
                        if (!currentUnit.equals(selectedUnit)) {
                            switch (currentUnit) {
                                case WEIGHT_UNIT_KILOGRAM: {
                                    focusWeightInput(currentValue * KILOGRAM_TO_POUND);
                                    break;
                                }
                                case WEIGHT_UNIT_POUND: {
                                    focusWeightInput(currentValue * POUND_TO_KILOGRAM);
                                    break;
                                }
                            }
                            mWeightUnit.setText(selectedUnit);
                        } else {
                            focusWeightInput(currentValue);
                        }
                        showNumPad();
                    }
                }).create();
        weightUnitSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightUnitSelectorDialog.show();
            }
        });
        weightInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusWeightInput(0);
            }
        });
    }

    private void setupHeightFunctionality() {
        final View heightParameter = findViewById(R.id.parameter_height);
        final TextView parameterType = heightParameter.findViewById(R.id.parameter_type);
        final LinearLayout heightView = heightParameter.findViewById(R.id.parameter_info);
        final LinearLayout heightUnitSelector = heightParameter.findViewById(R.id.parameter_unit_selector);
        mHeightValue = heightParameter.findViewById(R.id.parameter_value);
        mHeightUnit = heightParameter.findViewById(R.id.parameter_unit);

        parameterType.setText(PARAMETER_HEIGHT);
        mHeightUnit.setText(HEIGHT_DEFAULT_UNIT);
        mHeightValue.setText(String.valueOf(HEIGHT_METER_DEFAULT_VALUE));

        final AlertDialog heightUnitSelectorDialog = new AlertDialog.Builder(this)
                .setItems(getResources().getStringArray(R.array.height_units), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        String selectedUnit = getResources().getStringArray(R.array.height_units)[position];
                        String currentUnit = mHeightUnit.getText().toString();
                        Float currentValue = Float.valueOf(mHeightValue.getText().toString());
                        if (!currentUnit.equals(selectedUnit)) {
                            switch (currentUnit) {
                                case HEIGHT_UNIT_METER: {
                                    switch (selectedUnit) {
                                        case HEIGHT_UNIT_CENTIMETER: {
                                            focusHeightInput(currentValue * METER_TO_CENTIMETER);
                                            break;
                                        }
                                        case HEIGHT_UNIT_FEET: {
                                            focusHeightInput(currentValue * METER_TO_FEET);
                                            break;
                                        }
                                        case HEIGHT_UNIT_INCH: {
                                            focusHeightInput(currentValue * METER_TO_INCH);
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case HEIGHT_UNIT_CENTIMETER: {
                                    switch (selectedUnit) {
                                        case HEIGHT_UNIT_METER: {
                                            focusHeightInput(currentValue * CENTIMETER_TO_METER);
                                            break;
                                        }
                                        case HEIGHT_UNIT_FEET: {
                                            focusHeightInput(currentValue * CENTIMETER_TO_FEET);
                                            break;
                                        }
                                        case HEIGHT_UNIT_INCH: {
                                            focusHeightInput(currentValue * CENTIMETER_TO_INCH);
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case HEIGHT_UNIT_FEET: {
                                    switch (selectedUnit) {
                                        case HEIGHT_UNIT_METER: {
                                            focusHeightInput(currentValue * FEET_TO_METER);
                                            break;
                                        }
                                        case HEIGHT_UNIT_CENTIMETER: {
                                            focusHeightInput(currentValue * FEET_TO_CENTIMETER);
                                            break;
                                        }
                                        case HEIGHT_UNIT_INCH: {
                                            focusHeightInput(currentValue * FEET_TO_INCH);
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case HEIGHT_UNIT_INCH: {
                                    switch (selectedUnit) {
                                        case HEIGHT_UNIT_METER: {
                                            focusHeightInput(currentValue * INCH_TO_METER);
                                            break;
                                        }
                                        case HEIGHT_UNIT_CENTIMETER: {
                                            focusHeightInput(currentValue * INCH_TO_CENTIMETER);
                                            break;
                                        }
                                        case HEIGHT_UNIT_FEET: {
                                            focusHeightInput(currentValue * INCH_TO_FEET);
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            mHeightUnit.setText(selectedUnit);
                        } else {
                            focusHeightInput(currentValue);
                        }
                        showNumPad();
                    }
                }).create();
        heightUnitSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heightUnitSelectorDialog.show();
            }
        });
        heightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusHeightInput(0);
            }
        });
    }

    private void setupNumPadFunctionality() {
        final List<FrameLayout> numberButtons = new ArrayList<>();
        numberButtons.add((FrameLayout) findViewById(R.id.number_zero));
        numberButtons.add((FrameLayout) findViewById(R.id.number_one));
        numberButtons.add((FrameLayout) findViewById(R.id.number_two));
        numberButtons.add((FrameLayout) findViewById(R.id.number_three));
        numberButtons.add((FrameLayout) findViewById(R.id.number_four));
        numberButtons.add((FrameLayout) findViewById(R.id.number_five));
        numberButtons.add((FrameLayout) findViewById(R.id.number_six));
        numberButtons.add((FrameLayout) findViewById(R.id.number_seven));
        numberButtons.add((FrameLayout) findViewById(R.id.number_eight));
        numberButtons.add((FrameLayout) findViewById(R.id.number_nine));
        final FrameLayout allClearButton = findViewById(R.id.all_clear);
        final FrameLayout decimalButton = findViewById(R.id.decimal);
        final FrameLayout deleteButton = findViewById(R.id.delete);
        final FrameLayout goButton = findViewById(R.id.go);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int min = width < height ? width : height;
        LayoutParams params = goButton.getLayoutParams();
        params.width = min / INT_FOUR;
        goButton.setLayoutParams(params);

        final Drawable simpleDrawable = numberButtons.get(0).getBackground();
        for (int i = INT_ZERO; i < numberButtons.size(); i++) {
            final String stringNumber = String.valueOf(i);
            View button = numberButtons.get(i);
            params = button.getLayoutParams();
            params.height = min / INT_FOUR;
            params.width = min / INT_FOUR;
            button.setLayoutParams(params);
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        if (mFocusInput != null) {
                            String input = mFocusInput.getText().toString();
                            int length = input.length();
                            if (length == INT_ONE && input.matches(STRING_ZERO)) {
                                mFocusInput.setText(stringNumber);
                            } else if (input.contains(STRING_DECIMAL) && length > INT_ONE && length < INT_SIX) {
                                mFocusInput.append(stringNumber);
                            } else if (length > INT_ZERO && length < INT_THREE) {
                                mFocusInput.append(stringNumber);
                            }
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        view.setBackground(simpleDrawable);
                    }
                    return true;
                }
            });
        }

        decimalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    String input = mFocusInput.getText().toString();
                    if (input.contains(STRING_DECIMAL)) {
                        return true;
                    } else {
                        mFocusInput.append(STRING_DECIMAL);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackground(simpleDrawable);
                }
                return true;
            }
        });

        deleteButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    if (mFocusInput != null) {
                        String input = mFocusInput.getText().toString();
                        int length = input.length();
                        if (length == INT_ONE) {
                            mFocusInput.setText(STRING_ZERO);
                        } else {
                            mFocusInput.setText(input.substring(INT_ZERO, length - 1));
                        }
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackground(simpleDrawable);
                }
                return true;
            }
        });

        allClearButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    if (mFocusInput != null) {
                        mFocusInput.setText(STRING_ZERO);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackground(simpleDrawable);
                }
                return true;
            }
        });

        goButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    float bmi = getBMI();
                    final View resultCard = findViewById(R.id.result_card);
                    final TextView bmiTypeTextView = resultCard.findViewById(R.id.result_bmi_type);
                    if (bmi >= RANGE_UNDERWEIGHT[0] && bmi < RANGE_UNDERWEIGHT[1]) {
                        bmiTypeTextView.setText(BMI_UNDERWEIGHT);
                        bmiTypeTextView.setTextColor(getResources().getColor(R.color.under));
                    } else if (bmi >= RANGE_NORMAL[0] && bmi <= RANGE_NORMAL[1]) {
                        bmiTypeTextView.setText(BMI_NORMAL);
                        bmiTypeTextView.setTextColor(getResources().getColor(R.color.normal));
                    } else if (bmi > RANGE_OVER_WEIGHT[0] && bmi <= RANGE_OVER_WEIGHT[1]) {
                        bmiTypeTextView.setText(BMI_OVER_WEIGHT);
                        bmiTypeTextView.setTextColor(getResources().getColor(R.color.over));
                    } else {
                        Toast.makeText(view.getContext(), ERROR_INVALID_BMI, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    showBMI(String.valueOf(bmi));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                return true;
            }
        });
    }

    private float getBMI() {
        float weightValue = Float.valueOf(mWeightValue.getText().toString());
        float heightValue = Float.valueOf(mHeightValue.getText().toString());
        String weightUnit = mWeightUnit.getText().toString();
        String heightUnit = mHeightUnit.getText().toString();
        if (weightUnit.equals(WEIGHT_UNIT_POUND)) {
            weightValue *= POUND_TO_KILOGRAM;
        }
        switch (heightUnit) {
            case HEIGHT_UNIT_CENTIMETER: {
                heightValue *= CENTIMETER_TO_METER;
                break;
            }
            case HEIGHT_UNIT_FEET: {
                heightValue *= FEET_TO_METER;
                break;
            }
            case HEIGHT_UNIT_INCH: {
                heightValue *= INCH_TO_METER;
                break;
            }
        }
        if (heightValue > 0) {
            return roundFloat(weightValue / (heightValue * heightValue));
        } else {
            return 0;
        }
    }

    private void showBMI(String bmiString) {
        final TextView bmiValueTextView = mResult.findViewById(R.id.result_bmi_value);
        bmiValueTextView.setText(bmiString);
        mResult.setVisibility(View.VISIBLE);
        mNumPad.setVisibility(View.INVISIBLE);
    }

    private float roundFloat(float value) {
        return Math.round(value * 10) / 10.0f;
    }
}
