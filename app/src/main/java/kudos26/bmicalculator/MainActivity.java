package kudos26.bmicalculator;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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

    private TextView focusInput;
    private TextView weightValue;
    private TextView heightValue;
    private TextView weightUnit;
    private TextView heightUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MainActivity.this.finish();
                return true;
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final View numPad = findViewById(R.id.num_pad);
        final View resultCard = findViewById(R.id.result_card);

        setupWeightFunctionality();
        setupHeightFunctionality();
        View weightParameter = findViewById(R.id.parameter_weight);
        View heightParameter = findViewById(R.id.parameter_height);
        final LinearLayout weightParameterSupport = weightParameter.findViewById(R.id.parameter_support);
        final LinearLayout heightParameterSupport = heightParameter.findViewById(R.id.parameter_support);
        final TextView weightParameterValue = weightParameter.findViewById(R.id.parameter_value);
        final TextView heightParameterValue = heightParameter.findViewById(R.id.parameter_value);

        focusInput = weightParameterValue;

        weightParameterSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusInput != weightParameterValue) {
                    focusInput = weightParameterValue;
                    weightParameterValue.setTextColor(getResources().getColor(R.color.colorAccent));
                    heightParameterValue.setTextColor(getResources().getColor(R.color.black));
                }
                numPad.setVisibility(View.VISIBLE);
                resultCard.setVisibility(View.INVISIBLE);
            }
        });

        heightParameterSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusInput != heightParameterValue) {
                    focusInput = heightParameterValue;
                    heightParameterValue.setTextColor(getResources().getColor(R.color.colorAccent));
                    weightParameterValue.setTextColor(getResources().getColor(R.color.black));
                }
                numPad.setVisibility(View.VISIBLE);
                resultCard.setVisibility(View.INVISIBLE);
            }
        });

        setupNumPad();
    }

    private void setupWeightFunctionality() {
        final View weightParameter = findViewById(R.id.parameter_weight);
        final TextView parameterType = weightParameter.findViewById(R.id.parameter_type);
        parameterType.setText(PARAMETER_WEIGHT);
        weightValue = weightParameter.findViewById(R.id.parameter_value);
        weightValue.setTextColor(getResources().getColor(R.color.colorAccent));
        weightValue.setText(String.valueOf(PARAMETER_WEIGHT_KILOGRAM_DEFAULT_VALUE));
        weightUnit = weightParameter.findViewById(R.id.parameter_unit);
        weightUnit.setText(PARAMETER_WEIGHT_DEFAULT_UNIT);
        final Spinner weightSpinner = weightParameter.findViewById(R.id.spinner_parameter_type);
        final LinearLayout weightSelector = weightParameter.findViewById(R.id.parameter_selector);
        CustomAdapter weightSpinnerAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.weight_units));
        weightSpinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        weightSpinner.setAdapter(weightSpinnerAdapter);
        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                weightUnit.setText(getResources().getStringArray(R.array.weight_units)[position]);
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
        heightValue = heightParameter.findViewById(R.id.parameter_value);
        heightValue.setText(String.valueOf(PARAMETER_HEIGHT_METER_DEFAULT_VALUE));
        heightUnit = heightParameter.findViewById(R.id.parameter_unit);
        heightUnit.setText(PARAMETER_HEIGHT_DEFAULT_UNIT);
        final Spinner heightSpinner = heightParameter.findViewById(R.id.spinner_parameter_type);
        final LinearLayout heightSelector = heightParameter.findViewById(R.id.parameter_selector);
        CustomAdapter heightSpinnerAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.height_units));
        heightSpinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        heightSpinner.setAdapter(heightSpinnerAdapter);
        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                heightUnit.setText(getResources().getStringArray(R.array.height_units)[position]);
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

        final FrameLayout allClearButton = findViewById(R.id.all_clear);
        final FrameLayout decimalButton = findViewById(R.id.decimal);
        final FrameLayout deleteButton = findViewById(R.id.delete);
        final FrameLayout goButton = findViewById(R.id.go);

        final Drawable simpleDrawable = deleteButton.getBackground();

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
                        if (focusInput != null) {
                            String input = focusInput.getText().toString();
                            int length = input.length();
                            if (length == INT_ONE && input.matches(STRING_ZERO)) {
                                focusInput.setText(stringNumber);
                            } /*else if (input.contains(STRING_DECIMAL)) {
                                int decimal = input.split(STRING_DECIMAL)[1].length();
                                if (decimal == INT_ONE) {
                                    focusInput.append(stringNumber);
                                }
                            }*/ else if (input.contains(STRING_DECIMAL) && length > INT_ONE && length < INT_SIX) {
                                focusInput.append(stringNumber);
                            } else if (length > INT_ZERO && length < INT_THREE) {
                                focusInput.append(stringNumber);
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
                    String input = focusInput.getText().toString();
                    if (input.contains(STRING_DECIMAL)) {
                        return true;
                    } else {
                        focusInput.append(STRING_DECIMAL);
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
                    if (focusInput != null) {
                        String input = focusInput.getText().toString();
                        int length = input.length();
                        if (length == INT_ONE) {
                            focusInput.setText(STRING_ZERO);
                        } else {
                            focusInput.setText(input.substring(INT_ZERO, length - 1));
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
                    if (focusInput != null) {
                        focusInput.setText(STRING_ZERO);
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
                    resultCard.setVisibility(View.VISIBLE);
                    final TextView bmiValueTextView = resultCard.findViewById(R.id.result_bmi_value);
                    String bmiString = String.valueOf(bmiFloat);
                    bmiValueTextView.setText(bmiString);
                    final View numPad = findViewById(R.id.num_pad);
                    numPad.setVisibility(View.INVISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                return true;
            }
        });
    }

    private float getBMI() {
        float weightValue = Float.valueOf(this.weightValue.getText().toString());
        float heightValue = Float.valueOf(this.heightValue.getText().toString());
        String weightUnit = this.weightUnit.getText().toString();
        String heightUnit = this.heightUnit.getText().toString();
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
