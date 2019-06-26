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
import static kudos26.bmicalculator.Constants.INCH_TO_METER;
import static kudos26.bmicalculator.Constants.INT_FOUR;
import static kudos26.bmicalculator.Constants.INT_ONE;
import static kudos26.bmicalculator.Constants.INT_SIX;
import static kudos26.bmicalculator.Constants.INT_THREE;
import static kudos26.bmicalculator.Constants.INT_ZERO;
import static kudos26.bmicalculator.Constants.KEY_BMI_VALUE;
import static kudos26.bmicalculator.Constants.KEY_NUM_PAD_VISIBILITY;
import static kudos26.bmicalculator.Constants.KEY_PARAMETER_FOCUS;
import static kudos26.bmicalculator.Constants.KEY_RESULT_CARD_VISIBILITY;
import static kudos26.bmicalculator.Constants.PARAMETER_HEIGHT;
import static kudos26.bmicalculator.Constants.PARAMETER_HEIGHT_DEFAULT_UNIT;
import static kudos26.bmicalculator.Constants.PARAMETER_HEIGHT_METER_DEFAULT_VALUE;
import static kudos26.bmicalculator.Constants.PARAMETER_HEIGHT_UNIT_CENTIMETER;
import static kudos26.bmicalculator.Constants.PARAMETER_HEIGHT_UNIT_FEET;
import static kudos26.bmicalculator.Constants.PARAMETER_HEIGHT_UNIT_INCH;
import static kudos26.bmicalculator.Constants.PARAMETER_WEIGHT;
import static kudos26.bmicalculator.Constants.PARAMETER_WEIGHT_DEFAULT_UNIT;
import static kudos26.bmicalculator.Constants.PARAMETER_WEIGHT_KILOGRAM_DEFAULT_VALUE;
import static kudos26.bmicalculator.Constants.PARAMETER_WEIGHT_UNIT_POUND;
import static kudos26.bmicalculator.Constants.POUND_TO_KILOGRAM;
import static kudos26.bmicalculator.Constants.RANGE_NORMAL;
import static kudos26.bmicalculator.Constants.RANGE_OVER_WEIGHT;
import static kudos26.bmicalculator.Constants.RANGE_UNDERWEIGHT;
import static kudos26.bmicalculator.Constants.STRING_DECIMAL;
import static kudos26.bmicalculator.Constants.STRING_ZERO;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends AppCompatActivity {

    private TextView mFocusInput;
    private TextView mWeightValueInput;
    private TextView mHeightValueInput;
    private TextView mWeightUnit;
    private TextView mHeightUnit;
    private View mResultCard;
    private View mNumPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mNumPad = findViewById(R.id.num_pad);
        mResultCard = findViewById(R.id.result_card);
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
        setupNumPad();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_BMI_VALUE, mResultCard.findViewById(R.id.result_bmi_value).toString());
        outState.putInt(KEY_NUM_PAD_VISIBILITY, mNumPad.getVisibility());
        outState.putInt(KEY_RESULT_CARD_VISIBILITY, mResultCard.getVisibility());
        if (mFocusInput == mHeightValueInput) {
            outState.putString(KEY_PARAMETER_FOCUS, PARAMETER_HEIGHT);
        } else {
            outState.putString(KEY_PARAMETER_FOCUS, PARAMETER_WEIGHT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        focusWeightInput();
    }

    private void focusWeightInput() {
        mFocusInput = mWeightValueInput;
        mWeightValueInput.setTextColor(getResources().getColor(R.color.colorAccent));
        mHeightValueInput.setTextColor(getResources().getColor(R.color.black));
    }

    private void focusHeightInput() {
        mFocusInput = mHeightValueInput;
        mHeightValueInput.setTextColor(getResources().getColor(R.color.colorAccent));
        mWeightValueInput.setTextColor(getResources().getColor(R.color.black));
    }

    private void showNumPad() {
        mNumPad.setVisibility(View.VISIBLE);
        mResultCard.setVisibility(View.INVISIBLE);
    }

    private void showResultCard() {
        mResultCard.setVisibility(View.VISIBLE);
        mNumPad.setVisibility(View.INVISIBLE);
    }

    private void setupWeightFunctionality() {
        final View weightParameter = findViewById(R.id.parameter_weight);
        final TextView parameterType = weightParameter.findViewById(R.id.parameter_type);
        parameterType.setText(PARAMETER_WEIGHT);
        mWeightValueInput = weightParameter.findViewById(R.id.parameter_value);
        mWeightValueInput.setTextColor(getResources().getColor(R.color.colorAccent));
        mWeightValueInput.setText(String.valueOf(PARAMETER_WEIGHT_KILOGRAM_DEFAULT_VALUE));
        mWeightUnit = weightParameter.findViewById(R.id.parameter_unit);
        mWeightUnit.setText(PARAMETER_WEIGHT_DEFAULT_UNIT);
        final Spinner weightSpinner = weightParameter.findViewById(R.id.spinner_parameter_type);
        final LinearLayout weightSelector = weightParameter.findViewById(R.id.parameter_selector);
        CustomAdapter weightSpinnerAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.weight_units));
        weightSpinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        weightSpinner.setAdapter(weightSpinnerAdapter);
        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mWeightUnit.setText(getResources().getStringArray(R.array.weight_units)[position]);
                //focusWeightInput();
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
        mHeightValueInput.setText(String.valueOf(PARAMETER_HEIGHT_METER_DEFAULT_VALUE));
        mHeightUnit = heightParameter.findViewById(R.id.parameter_unit);
        mHeightUnit.setText(PARAMETER_HEIGHT_DEFAULT_UNIT);
        final Spinner heightSpinner = heightParameter.findViewById(R.id.spinner_parameter_type);
        final LinearLayout heightSelector = heightParameter.findViewById(R.id.parameter_selector);
        CustomAdapter heightSpinnerAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.height_units));
        heightSpinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        heightSpinner.setAdapter(heightSpinnerAdapter);
        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mHeightUnit.setText(getResources().getStringArray(R.array.height_units)[position]);
                //focusHeightInput();
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

    private void setupNumPad() {
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
                    float bmiFloat = getBMI();
                    final View resultCard = findViewById(R.id.result_card);
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    final TextView bmiTypeTextView = resultCard.findViewById(R.id.result_bmi_type);
                    if (bmiFloat >= RANGE_UNDERWEIGHT[0] && bmiFloat < RANGE_UNDERWEIGHT[1]) {
                        bmiTypeTextView.setText(BMI_UNDERWEIGHT);
                        bmiTypeTextView.setTextColor(getResources().getColor(R.color.under));
                    } else if (bmiFloat >= RANGE_NORMAL[0] && bmiFloat <= RANGE_NORMAL[1]) {
                        bmiTypeTextView.setText(BMI_NORMAL);
                        bmiTypeTextView.setTextColor(getResources().getColor(R.color.normal));
                    } else if (bmiFloat > RANGE_OVER_WEIGHT[0] && bmiFloat <= RANGE_OVER_WEIGHT[1]) {
                        bmiTypeTextView.setText(BMI_OVER_WEIGHT);
                        bmiTypeTextView.setTextColor(getResources().getColor(R.color.over));
                    } else {
                        Toast.makeText(view.getContext(), ERROR_INVALID_BMI, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    final TextView bmiValueTextView = resultCard.findViewById(R.id.result_bmi_value);
                    String bmiString = String.valueOf(bmiFloat);
                    bmiValueTextView.setText(bmiString);
                    showResultCard();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                return true;
            }
        });
    }

    private float getBMI() {
        float weightValue = Float.valueOf(this.mWeightValueInput.getText().toString());
        float heightValue = Float.valueOf(this.mHeightValueInput.getText().toString());
        String weightUnit = this.mWeightUnit.getText().toString();
        String heightUnit = this.mHeightUnit.getText().toString();
        if (weightUnit.equals(PARAMETER_WEIGHT_UNIT_POUND)) {
            weightValue *= POUND_TO_KILOGRAM;
        }
        switch (heightUnit) {
            case PARAMETER_HEIGHT_UNIT_CENTIMETER: {
                heightValue *= CENTIMETER_TO_METER;
                break;
            }
            case PARAMETER_HEIGHT_UNIT_FEET: {
                heightValue *= FEET_TO_METER;
                break;
            }
            case PARAMETER_HEIGHT_UNIT_INCH: {
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
}
