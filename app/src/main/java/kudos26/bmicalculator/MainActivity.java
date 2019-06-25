package kudos26.bmicalculator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams;

public class MainActivity extends AppCompatActivity {

    private TextView focusInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final View numpad = findViewById(R.id.numpad);

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
                    numpad.setVisibility(View.VISIBLE);
                }
            }
        });

        heightParameterSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusInput != heightParameterValue) {
                    focusInput = heightParameterValue;
                    heightParameterValue.setTextColor(getResources().getColor(R.color.colorAccent));
                    weightParameterValue.setTextColor(getResources().getColor(R.color.black));
                    numpad.setVisibility(View.VISIBLE);
                }
            }
        });

        setupNumpad();
    }

    private void setupWeightFunctionality() {
        final View weightParameter = findViewById(R.id.parameter_weight);
        final TextView weightParameterValue = weightParameter.findViewById(R.id.parameter_value);
        final TextView weightParameterType = weightParameter.findViewById(R.id.parameter_type);
        final TextView weightParameterUnit = weightParameter.findViewById(R.id.tv_parameter_unit);
        final Spinner weightSpinner = weightParameter.findViewById(R.id.spinner_parameter_type);
        final LinearLayout weightParameterSelector = weightParameter.findViewById(R.id.parameter_selector);
        weightParameterValue.setTextColor(getResources().getColor(R.color.colorAccent));
        weightParameterValue.setText(String.valueOf(60));
        weightParameterUnit.setText("Kilogram");
        weightParameterType.setText("Weight");
        CustomAdapter weightSpinnerAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.weight_units));
        weightSpinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        weightSpinner.setAdapter(weightSpinnerAdapter);
        weightParameterSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightSpinner.performClick();
            }
        });
    }

    private void setupHeightFunctionality() {
        final View heightParameter = findViewById(R.id.parameter_height);
        final TextView heightParameterValue = heightParameter.findViewById(R.id.parameter_value);
        final TextView heightParameterType = heightParameter.findViewById(R.id.parameter_type);
        final TextView heightParameterUnit = heightParameter.findViewById(R.id.tv_parameter_unit);
        final Spinner heightSpinner = heightParameter.findViewById(R.id.spinner_parameter_type);
        final LinearLayout heightParameterSelector = heightParameter.findViewById(R.id.parameter_selector);
        heightParameterValue.setText(String.valueOf(170));
        heightParameterUnit.setText("Centimeter");
        heightParameterType.setText("Height");
        CustomAdapter heightSpinnerAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.height_units));
        heightSpinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        heightSpinner.setAdapter(heightSpinnerAdapter);
        heightParameterSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heightSpinner.performClick();
            }
        });
    }

    private void setupNumpad() {
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
        params.height = min / 4;
        params.width = min / 4;
        goButton.setLayoutParams(params);

        for (int i = 0; i < numberButtons.size(); i++) {
            final int value = i;
            View button = numberButtons.get(i);
            params = button.getLayoutParams();
            params.height = min / 4;
            params.width = min / 4;
            button.setLayoutParams(params);
            //final Drawable normalDrawable = button.getBackground();
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        if (focusInput != null) {
                            String input = focusInput.getText().toString();
                            int length = input.length();
                            if (input.contains(".")) {

                            } else {
                                switch (length) {
                                    case 1: {
                                        if (input.matches("0")) {
                                            focusInput.setText(String.valueOf(value));
                                        } else {
                                            focusInput.append(String.valueOf(value));
                                        }
                                        break;
                                    }
                                    case 2: {
                                        focusInput.append(String.valueOf(value));
                                        break;
                                    }
                                }
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
                        if (input.contains(".")) {

                        } else {
                            switch (length) {
                                case 1: {
                                    focusInput.setText("0");
                                    break;
                                }
                                case 2: {
                                    focusInput.setText(input.substring(0, 1));
                                    break;
                                }
                                case 3: {
                                    focusInput.setText(input.substring(0, 2));
                                    break;
                                }
                            }
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
                        focusInput.setText("0");
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
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                return true;
            }
        });
    }

    private void calculateBMI() {

    }
}
