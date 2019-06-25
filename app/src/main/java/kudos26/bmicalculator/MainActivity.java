package kudos26.bmicalculator;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        View weightParameter = findViewById(R.id.parameter_weight);
        TextView weightParameterValue = weightParameter.findViewById(R.id.tv_parameter_value);
        TextView weightParameterType = weightParameter.findViewById(R.id.tv_parameter_type);
        TextView weightParameterUnit = weightParameter.findViewById(R.id.tv_parameter_unit);
        weightParameterValue.setTextColor(getResources().getColor(R.color.colorAccent));
        weightParameterValue.setText(String.valueOf(60));
        weightParameterUnit.setText("Kilogram");
        weightParameterType.setText("Weight");

        View heightParameter = findViewById(R.id.parameter_height);
        TextView heightParameterValue = heightParameter.findViewById(R.id.tv_parameter_value);
        TextView heightParameterType = heightParameter.findViewById(R.id.tv_parameter_type);
        TextView heightParameterUnit = heightParameter.findViewById(R.id.tv_parameter_unit);
        heightParameterValue.setText(String.valueOf(170));
        heightParameterUnit.setText("Centimeter");
        heightParameterType.setText("Height");

        //View focus = weightParameterValue;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int min = width < height ? width : height;

        List<FrameLayout> buttons = new ArrayList<>();
        buttons.add((FrameLayout) findViewById(R.id.number_zero));
        buttons.add((FrameLayout) findViewById(R.id.number_one));
        buttons.add((FrameLayout) findViewById(R.id.number_two));
        buttons.add((FrameLayout) findViewById(R.id.number_three));
        buttons.add((FrameLayout) findViewById(R.id.number_four));
        buttons.add((FrameLayout) findViewById(R.id.number_five));
        buttons.add((FrameLayout) findViewById(R.id.number_six));
        buttons.add((FrameLayout) findViewById(R.id.number_seven));
        buttons.add((FrameLayout) findViewById(R.id.number_eight));
        buttons.add((FrameLayout) findViewById(R.id.number_nine));
        buttons.add((FrameLayout) findViewById(R.id.all_clear));
        buttons.add((FrameLayout) findViewById(R.id.decimal));
        buttons.add((FrameLayout) findViewById(R.id.delete));
        buttons.add((FrameLayout) findViewById(R.id.go));

        for (FrameLayout button : buttons) {
            LayoutParams params = button.getLayoutParams();
            params.height = min / 4;
            params.width = min / 4;
            button.setLayoutParams(params);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
