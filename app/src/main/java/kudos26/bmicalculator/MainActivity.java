package kudos26.bmicalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        {
            View weightParameter = findViewById(R.id.parameter_weight);
            TextView parameterValue = weightParameter.findViewById(R.id.tv_parameter_value);
            parameterValue.setText(String.valueOf(60));
            parameterValue.setTextColor(getResources().getColor(R.color.colorAccent));
            TextView parameterType = weightParameter.findViewById(R.id.tv_parameter_type);
            parameterType.setText("Weight");
            TextView parameterUnit = weightParameter.findViewById(R.id.tv_parameter_unit);
            parameterUnit.setText("Kilogram");
        }

        {
            View heightParameter = findViewById(R.id.parameter_height);
            TextView parameterValue = heightParameter.findViewById(R.id.tv_parameter_value);
            parameterValue.setText(String.valueOf(170));
            TextView parameterType = heightParameter.findViewById(R.id.tv_parameter_type);
            parameterType.setText("Height");
            TextView parameterUnit = heightParameter.findViewById(R.id.tv_parameter_unit);
            parameterUnit.setText("Centimeter");
        }

    }
}
