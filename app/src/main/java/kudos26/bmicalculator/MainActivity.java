package kudos26.bmicalculator;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams;
import static kudos26.bmicalculator.Constants.BMI_NORMAL;
import static kudos26.bmicalculator.Constants.BMI_OVER_WEIGHT;
import static kudos26.bmicalculator.Constants.BMI_UNDERWEIGHT;
import static kudos26.bmicalculator.Constants.CENTIMETER_TO_METER;
import static kudos26.bmicalculator.Constants.ERROR_INVALID_BMI;
import static kudos26.bmicalculator.Constants.FEET_TO_METER;
import static kudos26.bmicalculator.Constants.HEIGHT_DEFAULT_UNIT;
import static kudos26.bmicalculator.Constants.HEIGHT_METER_DEFAULT_VALUE;
import static kudos26.bmicalculator.Constants.HEIGHT_UNIT_CENTIMETER;
import static kudos26.bmicalculator.Constants.HEIGHT_UNIT_FEET;
import static kudos26.bmicalculator.Constants.HEIGHT_UNIT_INCH;
import static kudos26.bmicalculator.Constants.INCH_TO_METER;
import static kudos26.bmicalculator.Constants.INT_FOUR;
import static kudos26.bmicalculator.Constants.INT_ONE;
import static kudos26.bmicalculator.Constants.INT_SIX;
import static kudos26.bmicalculator.Constants.INT_THREE;
import static kudos26.bmicalculator.Constants.INT_ZERO;
import static kudos26.bmicalculator.Constants.KEY_BMI_VALUE;
import static kudos26.bmicalculator.Constants.KEY_FOCUS_HEIGHT;
import static kudos26.bmicalculator.Constants.KEY_HEIGHT_INPUT_VALUE;
import static kudos26.bmicalculator.Constants.KEY_VISIBILITY_RESULT;
import static kudos26.bmicalculator.Constants.KEY_WEIGHT_INPUT_VALUE;
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
import static kudos26.bmicalculator.Constants.WEIGHT_UNIT_POUND;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends AppCompatActivity {

    private TextView mFocusInput;
    private TextView mWeightValueInput;
    private TextView mHeightValueInput;
    private TextView mWeightUnit;
    private TextView mHeightUnit;
    private Boolean mWeightSpinnerFlag;
    private Boolean mHeightSpinnerFlag;
    private View mResult;
    private View mNumPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mWeightSpinnerFlag = false;
        mHeightSpinnerFlag = false;

        mNumPad = findViewById(R.id.num_pad);
        mResult = findViewById(R.id.result_card);
        final ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MainActivity.this.finish();
                return true;
            }
        });

        final LinearLayout weightView = findViewById(R.id.parameter_weight).findViewById(R.id.parameter_support);
        final LinearLayout heightView = findViewById(R.id.parameter_height).findViewById(R.id.parameter_support);

        weightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusWeightInput();
                showNumPad();
            }
        });

        heightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusHeightInput();
                showNumPad();
            }
        });

        setupHeightFunctionality();
        setupWeightFunctionality();
        setupNumPadFunctionality();
        restoreSavedInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mResult.getVisibility() == View.VISIBLE) {
            outState.putInt(KEY_VISIBILITY_RESULT, View.VISIBLE);
            TextView bmiValueTextView = mResult.findViewById(R.id.result_bmi_value);
            outState.putString(KEY_BMI_VALUE, bmiValueTextView.getText().toString());
        } else {
            outState.putInt(KEY_VISIBILITY_RESULT, View.INVISIBLE);
        }
        outState.putString(KEY_WEIGHT_INPUT_VALUE, mWeightValueInput.getText().toString());
        outState.putString(KEY_HEIGHT_INPUT_VALUE, mHeightValueInput.getText().toString());
        outState.putBoolean(KEY_FOCUS_HEIGHT, false);
        if (mFocusInput == mHeightValueInput) {
            outState.putBoolean(KEY_FOCUS_HEIGHT, true);
        }
    }

    private void restoreSavedInstanceState(Bundle inState) {
        if (inState != null) {
            if (inState.getBoolean(KEY_FOCUS_HEIGHT)) {
                focusHeightInput();
            } else {
                focusWeightInput();
            }
            mWeightValueInput.setText(inState.getString(KEY_WEIGHT_INPUT_VALUE));
            mHeightValueInput.setText(inState.getString(KEY_HEIGHT_INPUT_VALUE));
            if (inState.getInt(KEY_VISIBILITY_RESULT) == View.VISIBLE) {
                setBMIString(inState.getString(KEY_BMI_VALUE));
                showResult();
            } else {
                showNumPad();
            }
        }
    }

    private void focusWeightInput() {
        mFocusInput = mWeightValueInput;
        mWeightValueInput.setText(STRING_ZERO);
        mWeightValueInput.setTextColor(getResources().getColor(R.color.colorAccent));
        mHeightValueInput.setTextColor(getResources().getColor(R.color.black));
    }

    private void focusHeightInput() {
        mFocusInput = mHeightValueInput;
        mHeightValueInput.setText(STRING_ZERO);
        mHeightValueInput.setTextColor(getResources().getColor(R.color.colorAccent));
        mWeightValueInput.setTextColor(getResources().getColor(R.color.black));
    }

    private void showNumPad() {
        mNumPad.setVisibility(View.VISIBLE);
        mResult.setVisibility(View.INVISIBLE);
    }

    private void showResult() {
        mResult.setVisibility(View.VISIBLE);
        mNumPad.setVisibility(View.INVISIBLE);
    }

    private void setupWeightFunctionality() {
        final View weightParameter = findViewById(R.id.parameter_weight);
        final TextView parameterType = weightParameter.findViewById(R.id.parameter_type);
        parameterType.setText(PARAMETER_WEIGHT);
        mWeightValueInput = weightParameter.findViewById(R.id.parameter_value);
        mWeightValueInput.setTextColor(getResources().getColor(R.color.colorAccent));
        mWeightValueInput.setText(String.valueOf(WEIGHT_KILOGRAM_DEFAULT_VALUE));
        mWeightUnit = weightParameter.findViewById(R.id.parameter_unit);
        mWeightUnit.setText(WEIGHT_DEFAULT_UNIT);
        final Spinner weightSpinner = weightParameter.findViewById(R.id.spinner_parameter_type);
        final LinearLayout weightSelector = weightParameter.findViewById(R.id.parameter_selector);
        CustomAdapter weightSpinnerAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.weight_units));
        weightSpinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        weightSpinner.setAdapter(weightSpinnerAdapter);
        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mWeightSpinnerFlag) {
                    mWeightUnit.setText(getResources().getStringArray(R.array.weight_units)[position]);
                    focusWeightInput();
                } else {
                    mWeightSpinnerFlag = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        weightSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightSpinner.performClick();
            }
        });
    }

    private void setupHeightFunctionality() {
        final View heightParameter = findViewById(R.id.parameter_height);
        final TextView parameterType = heightParameter.findViewById(R.id.parameter_type);
        parameterType.setText(PARAMETER_HEIGHT);
        mHeightValueInput = heightParameter.findViewById(R.id.parameter_value);
        mHeightValueInput.setText(String.valueOf(HEIGHT_METER_DEFAULT_VALUE));
        mHeightUnit = heightParameter.findViewById(R.id.parameter_unit);
        mHeightUnit.setText(HEIGHT_DEFAULT_UNIT);
        final Spinner heightSpinner = heightParameter.findViewById(R.id.spinner_parameter_type);
        final LinearLayout heightSelector = heightParameter.findViewById(R.id.parameter_selector);
        CustomAdapter heightSpinnerAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.height_units));
        heightSpinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        heightSpinner.setAdapter(heightSpinnerAdapter);
        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mHeightSpinnerFlag) {
                    mHeightUnit.setText(getResources().getStringArray(R.array.height_units)[position]);
                    focusHeightInput();
                } else {
                    mHeightSpinnerFlag = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        heightSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heightSpinner.performClick();
            }
        });
    }

    private void setupNumPadFunctionality() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int min = width < height ? width : height;

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
        final Drawable simpleDrawable = numberButtons.get(0).getBackground();
        final FrameLayout allClearButton = findViewById(R.id.all_clear);
        final FrameLayout decimalButton = findViewById(R.id.decimal);
        final FrameLayout deleteButton = findViewById(R.id.delete);
        final FrameLayout goButton = findViewById(R.id.go);
        LayoutParams params = goButton.getLayoutParams();
        params.height = min / INT_FOUR;
        params.width = min / INT_FOUR;
        goButton.setLayoutParams(params);
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
                    setBMIString(String.valueOf(bmi));
                    showResult();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                return true;
            }
        });
    }

    private float getBMI() {
        float weightValue = Float.valueOf(mWeightValueInput.getText().toString());
        float heightValue = Float.valueOf(mHeightValueInput.getText().toString());
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
            float bmiResult = weightValue / (heightValue * heightValue);
            bmiResult = Math.round(bmiResult * 10) / 10.0f;
            return bmiResult;
        } else return 1;
    }

    private void setBMIString(String bmiString) {
        final View resultCard = findViewById(R.id.result_card);
        final TextView bmiValueTextView = resultCard.findViewById(R.id.result_bmi_value);
        bmiValueTextView.setText(bmiString);
    }
}
