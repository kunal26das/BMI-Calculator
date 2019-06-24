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

        View focus = weightParameterValue;
    }
}
