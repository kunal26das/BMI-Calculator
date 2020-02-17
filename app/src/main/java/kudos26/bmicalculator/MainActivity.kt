package kudos26.bmicalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {

    private var mResult: View? = null
    private var mNumPad: View? = null
    private var mFocusInput: TextView? = null
    private var mWeightUnit: TextView? = null
    private var mHeightUnit: TextView? = null
    private var mWeightValue: TextView? = null
    private var mHeightValue: TextView? = null

    private val bmi: Float
        get() {
            var weightValue = java.lang.Float.valueOf(mWeightValue!!.text.toString())
            var heightValue = java.lang.Float.valueOf(mHeightValue!!.text.toString())
            val weightUnit = mWeightUnit!!.text.toString()
            val heightUnit = mHeightUnit!!.text.toString()
            if (weightUnit == WEIGHT_UNIT_POUND) {
                weightValue *= POUND_TO_KILOGRAM
            }
            when (heightUnit) {
                HEIGHT_UNIT_CENTIMETER -> {
                    heightValue *= CENTIMETER_TO_METER
                }
                HEIGHT_UNIT_FEET -> {
                    heightValue *= FEET_TO_METER
                }
                HEIGHT_UNIT_INCH -> {
                    heightValue *= INCH_TO_METER
                }
            }
            return if (heightValue > 0) {
                roundFloat(weightValue / (heightValue * heightValue))
            } else {
                0f
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        mNumPad = findViewById(R.id.num_pad)
        mResult = findViewById(R.id.result_card)
        val backButton = findViewById<ImageView>(R.id.back_button)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        backButton.setOnTouchListener { view, motionEvent ->
            this@MainActivity.finish()
            true
        }
        setupHeightFunctionality()
        setupWeightFunctionality()
        setupNumPadFunctionality()
        restoreSavedInstanceState(savedInstanceState)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mResult!!.visibility == View.VISIBLE) {
            outState.putInt(KEY_VISIBILITY_RESULT, View.VISIBLE)
            val bmiValueTextView = mResult!!.findViewById<TextView>(R.id.result_bmi_value)
            outState.putString(KEY_BMI_VALUE, bmiValueTextView.text.toString())
        } else {
            outState.putInt(KEY_VISIBILITY_RESULT, View.INVISIBLE)
        }

        outState.putString(KEY_WEIGHT_UNIT, mWeightUnit!!.text.toString())
        outState.putString(KEY_HEIGHT_UNIT, mHeightUnit!!.text.toString())
        outState.putString(KEY_WEIGHT_VALUE, mWeightValue!!.text.toString())
        outState.putString(KEY_HEIGHT_VALUE, mHeightValue!!.text.toString())

        outState.putBoolean(KEY_FOCUS_HEIGHT, false)
        if (mFocusInput === mHeightValue) {
            outState.putBoolean(KEY_FOCUS_HEIGHT, true)
        }
    }

    private fun restoreSavedInstanceState(inState: Bundle?) {
        if (inState != null) {
            if (inState.getBoolean(KEY_FOCUS_HEIGHT)) {
                focusHeightInput(0f)
            } else {
                focusWeightInput(0f)
            }

            mWeightUnit!!.text = inState.getString(KEY_WEIGHT_UNIT)
            mHeightUnit!!.text = inState.getString(KEY_HEIGHT_UNIT)
            mWeightValue!!.text = inState.getString(KEY_WEIGHT_VALUE)
            mHeightValue!!.text = inState.getString(KEY_HEIGHT_VALUE)

            if (inState.getInt(KEY_VISIBILITY_RESULT) == View.VISIBLE) {
                showBMI(inState.getString(KEY_BMI_VALUE))
            } else {
                showNumPad()
            }
        }
    }

    private fun focusWeightInput(weight: Float) {
        mFocusInput = mWeightValue
        if (weight == 0f) {
            mWeightValue!!.text = STRING_ZERO
        } else {
            mWeightValue!!.text = roundFloat(weight).toString()
        }
        mWeightValue!!.setTextColor(resources.getColor(R.color.colorAccent))
        mHeightValue!!.setTextColor(resources.getColor(R.color.black))
        showNumPad()
    }

    private fun focusHeightInput(height: Float) {
        mFocusInput = mHeightValue
        if (height == 0f) {
            mHeightValue!!.text = STRING_ZERO
        } else {
            mHeightValue!!.text = roundFloat(height).toString()
        }
        mHeightValue!!.setTextColor(resources.getColor(R.color.colorAccent))
        mWeightValue!!.setTextColor(resources.getColor(R.color.black))
        showNumPad()
    }

    private fun showNumPad() {
        mNumPad!!.visibility = View.VISIBLE
        mResult!!.visibility = View.INVISIBLE
    }

    private fun setupWeightFunctionality() {
        val weightParameter = findViewById<View>(R.id.parameter_weight)
        val parameterType = weightParameter.findViewById<TextView>(R.id.parameter_type)
        val weightInfo = weightParameter.findViewById<LinearLayout>(R.id.parameter_info)
        val weightUnitSelector = weightParameter.findViewById<LinearLayout>(R.id.parameter_unit_selector)
        mWeightValue = weightParameter.findViewById(R.id.parameter_value)
        mWeightUnit = weightParameter.findViewById(R.id.parameter_unit)

        parameterType.text = PARAMETER_WEIGHT
        mWeightUnit!!.text = WEIGHT_DEFAULT_UNIT
        mWeightValue!!.text = WEIGHT_KILOGRAM_DEFAULT_VALUE.toString()
        mWeightValue!!.setTextColor(resources.getColor(R.color.colorAccent))

        val weightUnitSelectorDialog = AlertDialog.Builder(this)
                .setItems(resources.getStringArray(R.array.weight_units)) { dialog, position ->
                    val currentUnit = mWeightUnit!!.text.toString()
                    val currentValue = java.lang.Float.valueOf(mWeightValue!!.text.toString())
                    val selectedUnit = resources.getStringArray(R.array.weight_units)[position]
                    when {
                        currentUnit != selectedUnit -> {
                            when (currentUnit) {
                                WEIGHT_UNIT_KILOGRAM -> {
                                    focusWeightInput(currentValue * KILOGRAM_TO_POUND)
                                }
                                WEIGHT_UNIT_POUND -> {
                                    focusWeightInput(currentValue * POUND_TO_KILOGRAM)
                                }
                            }
                            mWeightUnit!!.text = selectedUnit
                        }
                        else -> focusWeightInput(currentValue)
                    }
                    showNumPad()
                }.create()
        weightUnitSelector.setOnClickListener { weightUnitSelectorDialog.show() }
        weightInfo.setOnClickListener { focusWeightInput(0f) }
    }

    private fun setupHeightFunctionality() {
        val heightParameter = findViewById<View>(R.id.parameter_height)
        val parameterType = heightParameter.findViewById<TextView>(R.id.parameter_type)
        val heightView = heightParameter.findViewById<LinearLayout>(R.id.parameter_info)
        val heightUnitSelector = heightParameter.findViewById<LinearLayout>(R.id.parameter_unit_selector)
        mHeightValue = heightParameter.findViewById(R.id.parameter_value)
        mHeightUnit = heightParameter.findViewById(R.id.parameter_unit)

        parameterType.text = PARAMETER_HEIGHT
        mHeightUnit!!.text = HEIGHT_DEFAULT_UNIT
        mHeightValue!!.text = HEIGHT_METER_DEFAULT_VALUE.toString()

        val heightUnitSelectorDialog = AlertDialog.Builder(this)
                .setItems(resources.getStringArray(R.array.height_units)) { dialog, position ->
                    val selectedUnit = resources.getStringArray(R.array.height_units)[position]
                    val currentUnit = mHeightUnit!!.text.toString()
                    val currentValue = java.lang.Float.valueOf(mHeightValue!!.text.toString())
                    if (currentUnit != selectedUnit) {
                        when (currentUnit) {
                            HEIGHT_UNIT_METER -> {
                                when (selectedUnit) {
                                    HEIGHT_UNIT_CENTIMETER -> {
                                        focusHeightInput(currentValue * METER_TO_CENTIMETER)
                                    }
                                    HEIGHT_UNIT_FEET -> {
                                        focusHeightInput(currentValue * METER_TO_FEET)
                                    }
                                    HEIGHT_UNIT_INCH -> {
                                        focusHeightInput(currentValue * METER_TO_INCH)
                                    }
                                }
                            }
                            HEIGHT_UNIT_CENTIMETER -> {
                                when (selectedUnit) {
                                    HEIGHT_UNIT_METER -> {
                                        focusHeightInput(currentValue * CENTIMETER_TO_METER)
                                    }
                                    HEIGHT_UNIT_FEET -> {
                                        focusHeightInput(currentValue * CENTIMETER_TO_FEET)
                                    }
                                    HEIGHT_UNIT_INCH -> {
                                        focusHeightInput(currentValue * CENTIMETER_TO_INCH)
                                    }
                                }
                            }
                            HEIGHT_UNIT_FEET -> {
                                when (selectedUnit) {
                                    HEIGHT_UNIT_METER -> {
                                        focusHeightInput(currentValue * FEET_TO_METER)
                                    }
                                    HEIGHT_UNIT_CENTIMETER -> {
                                        focusHeightInput(currentValue * FEET_TO_CENTIMETER)
                                    }
                                    HEIGHT_UNIT_INCH -> {
                                        focusHeightInput(currentValue * FEET_TO_INCH)
                                    }
                                }
                            }
                            HEIGHT_UNIT_INCH -> {
                                when (selectedUnit) {
                                    HEIGHT_UNIT_METER -> {
                                        focusHeightInput(currentValue * INCH_TO_METER)
                                    }
                                    HEIGHT_UNIT_CENTIMETER -> {
                                        focusHeightInput(currentValue * INCH_TO_CENTIMETER)
                                    }
                                    HEIGHT_UNIT_FEET -> {
                                        focusHeightInput(currentValue * INCH_TO_FEET)
                                    }
                                }
                            }
                        }
                        mHeightUnit!!.text = selectedUnit
                    } else {
                        focusHeightInput(currentValue)
                    }
                    showNumPad()
                }.create()
        heightUnitSelector.setOnClickListener { heightUnitSelectorDialog.show() }
        heightView.setOnClickListener { focusHeightInput(0f) }
    }

    private fun setupNumPadFunctionality() {
        val numberButtons = ArrayList<FrameLayout>()
        numberButtons.add(findViewById<View>(R.id.number_zero) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_one) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_two) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_three) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_four) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_five) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_six) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_seven) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_eight) as FrameLayout)
        numberButtons.add(findViewById<View>(R.id.number_nine) as FrameLayout)
        val allClearButton = findViewById<FrameLayout>(R.id.all_clear)
        val decimalButton = findViewById<FrameLayout>(R.id.decimal)
        val deleteButton = findViewById<FrameLayout>(R.id.delete)
        val goButton = findViewById<FrameLayout>(R.id.go)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val min = if (width < height) width else height
        var params = goButton.layoutParams
        params.width = min / INT_FOUR
        goButton.layoutParams = params

        val simpleDrawable = numberButtons[0].background
        for (i in INT_ZERO until numberButtons.size) {
            val stringNumber = i.toString()
            val button = numberButtons[i]
            params = button.layoutParams
            params.height = min / INT_FOUR
            params.width = min / INT_FOUR
            button.layoutParams = params
            button.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    if (mFocusInput != null) {
                        val input = mFocusInput!!.text.toString()
                        val length = input.length
                        if (length == INT_ONE && input.matches(STRING_ZERO.toRegex())) {
                            mFocusInput!!.text = stringNumber
                        } else if (input.contains(STRING_DECIMAL) && length > INT_ONE && length < INT_SIX) {
                            mFocusInput!!.append(stringNumber)
                        } else if (length in (INT_ZERO + 1) until INT_THREE) {
                            mFocusInput!!.append(stringNumber)
                        }
                    }
                } else if (event.action == MotionEvent.ACTION_UP) {
                    view.background = simpleDrawable
                }
                true
            }
        }

        decimalButton.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                val input = mFocusInput!!.text.toString()
                if (input.contains(STRING_DECIMAL)) {
                    return@OnTouchListener true
                } else {
                    mFocusInput!!.append(STRING_DECIMAL)
                }
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                view.background = simpleDrawable
            }
            true
        })

        deleteButton.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                if (mFocusInput != null) {
                    val input = mFocusInput!!.text.toString()
                    val length = input.length
                    if (length == INT_ONE) {
                        mFocusInput!!.text = STRING_ZERO
                    } else {
                        mFocusInput!!.text = input.substring(INT_ZERO, length - 1)
                    }
                }
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                view.background = simpleDrawable
            }
            true
        }

        allClearButton.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                if (mFocusInput != null) {
                    mFocusInput!!.text = STRING_ZERO
                }
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                view.background = simpleDrawable
            }
            true
        }

        goButton.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val bmi = bmi
                val resultCard = findViewById<View>(R.id.result_card)
                val bmiTypeTextView = resultCard.findViewById<TextView>(R.id.result_bmi_type)
                if (bmi >= RANGE_UNDERWEIGHT[0] && bmi < RANGE_UNDERWEIGHT[1]) {
                    bmiTypeTextView.text = BMI_UNDERWEIGHT
                    bmiTypeTextView.setTextColor(resources.getColor(R.color.under))
                } else if (bmi >= RANGE_NORMAL[0] && bmi <= RANGE_NORMAL[1]) {
                    bmiTypeTextView.text = BMI_NORMAL
                    bmiTypeTextView.setTextColor(resources.getColor(R.color.normal))
                } else if (bmi > RANGE_OVER_WEIGHT[0] && bmi <= RANGE_OVER_WEIGHT[1]) {
                    bmiTypeTextView.text = BMI_OVER_WEIGHT
                    bmiTypeTextView.setTextColor(resources.getColor(R.color.over))
                } else {
                    Toast.makeText(view.context, ERROR_INVALID_BMI, Toast.LENGTH_SHORT).show()
                    return@OnTouchListener true
                }
                showBMI(bmi.toString())
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                view.setBackgroundColor(resources.getColor(R.color.colorAccent))
            }
            true
        })
    }

    private fun showBMI(bmiString: String?) {
        val bmiValueTextView = mResult!!.findViewById<TextView>(R.id.result_bmi_value)
        bmiValueTextView.text = bmiString
        mResult!!.visibility = View.VISIBLE
        mNumPad!!.visibility = View.INVISIBLE
    }

    private fun roundFloat(value: Float): Float {
        return (value * 10).roundToInt() / 10.0f
    }

    companion object {

        private const val INT_ZERO = 0
        private const val INT_ONE = 1
        private const val INT_TWO = 2
        private const val INT_THREE = 3
        private const val INT_FOUR = 4
        private const val INT_FIVE = 5
        private const val INT_SIX = 6
        private const val INT_SEVEN = 7
        private const val INT_EIGHT = 8
        private const val INT_NINE = 9

        private const val STRING_ZERO = "0"
        private const val STRING_DECIMAL = "."

        private const val PARAMETER_WEIGHT = "Weight"
        private const val PARAMETER_HEIGHT = "Height"

        private const val WEIGHT_UNIT_KILOGRAM = "Kilogram"
        private const val WEIGHT_UNIT_POUND = "Pound"

        private const val HEIGHT_UNIT_CENTIMETER = "Centimeter"
        private const val HEIGHT_UNIT_METER = "Meter"
        private const val HEIGHT_UNIT_FEET = "Feet"
        private const val HEIGHT_UNIT_INCH = "Inch"

        private const val WEIGHT_DEFAULT_UNIT = WEIGHT_UNIT_KILOGRAM
        private const val HEIGHT_DEFAULT_UNIT = HEIGHT_UNIT_METER

        private const val WEIGHT_KILOGRAM_DEFAULT_VALUE = 60
        private const val HEIGHT_METER_DEFAULT_VALUE = 1.7f

        private const val KILOGRAM_TO_POUND = 2.20462f
        private const val POUND_TO_KILOGRAM = 0.453592f

        private const val METER_TO_CENTIMETER = 100
        private const val METER_TO_FEET = 3.28084f
        private const val METER_TO_INCH = 39.3701f

        private const val CENTIMETER_TO_METER = 0.01f
        private const val CENTIMETER_TO_FEET = 0.0328084f
        private const val CENTIMETER_TO_INCH = 0.393701f

        private const val FEET_TO_METER = 0.3048f
        private const val FEET_TO_CENTIMETER = 30.48f
        private const val FEET_TO_INCH = 12

        private const val INCH_TO_METER = 0.0254f
        private const val INCH_TO_CENTIMETER = 2.54f
        private const val INCH_TO_FEET = 0.0833333f

        private val RANGE_UNDERWEIGHT = floatArrayOf(16.0f, 18.5f)
        private val RANGE_NORMAL = floatArrayOf(18.5f, 25.0f)
        private val RANGE_OVER_WEIGHT = floatArrayOf(25.0f, 40.0f)

        private const val BMI_UNDERWEIGHT = "Underweight"
        private const val BMI_NORMAL = "Normal"
        private const val BMI_OVER_WEIGHT = "Over weight"

        private const val ERROR_INVALID_BMI = "Invalid BMI! Please check your input"

        private const val KEY_BMI_VALUE = "BMI"
        private const val KEY_WEIGHT_UNIT = "Weight Unit"
        private const val KEY_HEIGHT_UNIT = "Height Unit"
        private const val KEY_WEIGHT_VALUE = "Weight Value"
        private const val KEY_HEIGHT_VALUE = "Height Value"
        private const val KEY_FOCUS_WEIGHT = "Focus Weight"
        private const val KEY_FOCUS_HEIGHT = "Focus Height"
        private const val KEY_VISIBILITY_NUMPAD = "NumPad Visibility"
        private const val KEY_VISIBILITY_RESULT = "Result Visibility"
    }
}
