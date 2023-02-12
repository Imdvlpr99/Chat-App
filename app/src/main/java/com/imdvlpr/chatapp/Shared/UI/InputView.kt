package com.imdvlpr.chatapp.Shared.UI

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.text.*
import android.text.method.DigitsKeyListener
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Extension.setVisible
import com.imdvlpr.chatapp.Shared.Extension.toVisible
import com.imdvlpr.chatapp.databinding.LayoutInputViewBinding

class InputView : ConstraintLayout {

    /**
     * Variables
     */
    //State
    private lateinit var mContext: Context
    private lateinit var binding: LayoutInputViewBinding

    enum class TYPE { TEXT, PASSWORD, PASSWORD_NUMBER, NUMBER, OFFICE_PHONE_NUMBER, PHONE_NUMBER, NUMBER_DECIMAL, EMAIL, DISABLE, CURRENCY ,MULTI_LINE, NUMBER_4_DIGIT, TEXT_NO_SYMBOL, ALPHANUMERIC,  CARD_NUMBER, MASKING_NUMBER, CURRENCY_DECIMAL, UNIT, ACCOUNT_NUMBER, VOUCHER, CREDIT_CARD}
    enum class ACTION { NEXT, DONE }
    private var inputType: TYPE = TYPE.TEXT
    private var listener: InputListener? = null
    private var isDeleteMode: Boolean = false
    private var currentCurrency: String = ""
    private var limitCounter: String = ""
    private var listSpinner: ArrayList<String> = ArrayList()
    private var isFieldError: Boolean = false
    private var isNoBackground = false

    /**
     * Constructors
     */
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /**
     * Initialization
     */
    private fun init(context: Context, attributeSet: AttributeSet?) {
        mContext = context

        //Inflate view
        binding = LayoutInputViewBinding.bind(LayoutInflater.from(mContext).inflate(R.layout.layout_input_view, this, true))

        binding.txtInput.addTextChangedListener(textWatcher)
        binding.cbPassword.setOnCheckedChangeListener { _, isChecked ->
            binding.txtInput.transformationMethod =
                if (isChecked) PasswordTransformationMethod()
                else HideReturnsTransformationMethod.getInstance()
            binding.txtInput.setSelection(binding.txtInput.text.toString().length)
        }
        binding.cbPassword.isChecked = true
        binding.txtInput.setOnFocusChangeListener { _, hasFocus ->
            listener?.onEditTextFocusChange(
                hasFocus
            )
        }
    }


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (inputType == TYPE.NUMBER_4_DIGIT) {
                if (start == 0 && before == 0 && count >= 4) {
                    val format = s?.replace("....".toRegex(), "$0 ")
                    binding.txtInput.setText(format)
                }
            }
        }
        override fun afterTextChanged(s: Editable?) {
            when(inputType){
                TYPE.TEXT_NO_SYMBOL ->{
                    val blockCharacterSet = "qwertyuiopasdfghjklzxcvbnm 1234567890 QWERTYUIOPASDFGHJKLZXCVBNM"
                    val filter = InputFilter { source, start, end, _, _, _ ->
                        for (i in start until end) {
                            if (!Character.isLetter(source[i]) && !Character.isSpaceChar(source[i]) &&
                                !blockCharacterSet.contains("" + source)) {
                                return@InputFilter ""
                            }
                        }
                        return@InputFilter null
                    }
                    binding.txtInput.filters = arrayOf(filter)
                }
                TYPE.NUMBER_4_DIGIT ->{
                    val space = ' '
                    if (!s.isNullOrEmpty() && s.length % 5 == 0) {
                        val c: Char = s[(s.length - 1)]
                        if (space == c) {
                            s.delete(s.length - 1, s.length)
                        }
                        if (Character.isDigit(c) && TextUtils.split(s.toString(), java.lang.String.valueOf(space)).size <= 3) {
                            s.insert(
                                s.length - 1,
                                java.lang.String.valueOf(space)
                            )
                        }
                    }
                }
                else -> {}
            }
            // listener
            listener?.afterTextChanged()
        }
    }

    fun setTitle(title: String) {
        binding.lblInput.text = title
    }

    fun setTitleVisible(visible: Boolean) {
        binding.lblInput.setVisible(visible)
    }

    fun setBackground(drawable: Int) {
        binding.constraintLayout.background = ContextCompat.getDrawable(context, drawable)
        isNoBackground = true
    }

    fun setHint(hint: String) {
        binding.txtInput.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        binding.txtInput.hint = getHintTypeface(hint)
    }

    fun setSuffix(suffix: String) {
        if (suffix.isNotEmpty()) {
            binding.lblSuffix.text = suffix
            binding.lblSuffix.toVisible()
        }
    }

    fun setDisableCopyPaste() {
        binding.txtInput.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        }
        binding.txtInput.customInsertionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        }

        binding.txtInput.isLongClickable = false
        binding.txtInput.setTextIsSelectable(false)
        binding.txtInput.highlightColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.txtInput.setTextSelectHandle(ContextCompat.getDrawable(context, R.drawable.text_select_handle) as Drawable)
        }
    }


    fun setInfo(info: String, infoSize: Float = 14f) {
        binding.lblAlert.text = info
        binding.lblAlert.setVisible(info.isNotEmpty())
        binding.lblAlert.setTextSize(TypedValue.COMPLEX_UNIT_SP, infoSize)
    }

    fun setCounter(limit: String) {
        limitCounter = limit
        binding.lblAlert.setVisible(limit.isNotEmpty())
        binding.lblAlert.text = String.format(context.getString(R.string.count), "0", limit)
        binding.lblAlert.gravity = Gravity.END
    }

    fun setMaxLength(length: Int) {
        binding.txtInput.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(length))
    }

    private fun getHintTypeface(hint: String): SpannableString {
        val typeFontBook = ResourcesCompat.getFont(context, R.font.poppins_regular) as Typeface
        val typefaceSpan = CustomTypefaceSpan(typeFontBook)
        val spannableString = SpannableString(hint)
        spannableString.setSpan(typefaceSpan, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun setText(input: String, color: Int = R.color.white) {
        binding.txtInput.setText(input)
        binding.txtInput.setTextColor(ContextCompat.getColor(mContext, color))
        binding.txtInput.setSelection(input.length)
    }

    fun setText(input: String) {
        binding.txtInput.setText(input)
    }

    fun setInputType(type: TYPE, needTransparant: Boolean = false) {
        inputType = type
        binding.txtInput.inputType = when(type) {
            TYPE.TEXT -> InputType.TYPE_CLASS_TEXT
            TYPE.PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            TYPE.PASSWORD_NUMBER -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            TYPE.NUMBER -> InputType.TYPE_CLASS_NUMBER
            TYPE.NUMBER_DECIMAL -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            TYPE.EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            TYPE.DISABLE -> InputType.TYPE_CLASS_TEXT
            TYPE.CURRENCY -> InputType.TYPE_CLASS_NUMBER
            TYPE.MULTI_LINE -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            TYPE.NUMBER_4_DIGIT -> InputType.TYPE_CLASS_PHONE
            TYPE.TEXT_NO_SYMBOL -> InputType.TYPE_CLASS_TEXT
            TYPE.ALPHANUMERIC -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            TYPE.CARD_NUMBER, TYPE.MASKING_NUMBER, TYPE.ACCOUNT_NUMBER -> InputType.TYPE_CLASS_NUMBER
            else-> InputType.TYPE_CLASS_TEXT
        }
        if (type == TYPE.NUMBER_4_DIGIT) {
            binding.txtInput.keyListener = DigitsKeyListener.getInstance("0123456789 ")

        }
        binding.txtInput.setVisible(type != TYPE.DISABLE)
        binding.cbPassword.setVisible(inputType == TYPE.PASSWORD || inputType == TYPE.PASSWORD_NUMBER)
    }

    fun setImeOption(action: ACTION) {
        binding.txtInput.imeOptions = when(action) {
            ACTION.NEXT -> EditorInfo.IME_ACTION_NEXT
            ACTION.DONE -> EditorInfo.IME_ACTION_DONE
        }
    }

    fun setVisibleIcon(visible: Boolean, drawable: Int? = null, forceSetFill: Boolean = false) {
        binding.imageView.setVisible(visible)
        if (drawable != null) {
            val iconInput = ContextCompat.getDrawable(mContext, drawable) as Drawable
            binding.imageView.setImageDrawable(iconInput)
            binding.imageView.drawable.setFill(forceSetFill)
        }
    }

    private fun Drawable.setFill(isNotEmpty: Boolean) {
        val color = ContextCompat.getColor(context, if (isNotEmpty) R.color.white else R.color.white)
        this.changeDrawableColor(color)
    }

    fun setListener(inputListener: InputListener) {
        listener = inputListener
    }

    fun getText(): String {
        return binding.txtInput.text.toString()
    }

    fun getError(): String {
        return if (binding.txtInput.error == null) "" else binding.txtInput.error.toString()
    }

    fun setError(error: String? = null) {
        binding.txtInput.error = error
    }

    fun setFieldError(isFieldError: Boolean) {
        when(isFieldError) {
            true -> {
                val colorRed = ContextCompat.getColor(context, R.color.colorRed)
                binding.lblInput.setTextColor(colorRed)
                binding.txtInput.setTextColor(colorRed)
                binding.lblAlert.setTextColor(colorRed)
                binding.imageView.drawable.changeDrawableColor(colorRed)
                binding.cbPassword.buttonDrawable?.changeDrawableColor(colorRed)
                binding.constraintLayout.background = ContextCompat.getDrawable(context, R.drawable.background_input_false)
            }
            false -> {
                binding.lblInput.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.txtInput.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.lblAlert.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.imageView.drawable.changeDrawableColor(ContextCompat.getColor(context, R.color.white))
                binding.cbPassword.buttonDrawable?.changeDrawableColor(ContextCompat.getColor(context, R.color.white))
                binding.constraintLayout.background = ContextCompat.getDrawable(context, R.drawable.background_input)
            }
        }
        this.isFieldError = isFieldError
    }

    fun getIsFieldError(): Boolean {
        return isFieldError
    }

    fun setDisabled(isDisable: Boolean){
        binding.txtInput.isEnabled = isDisable
    }
    private fun Drawable.changeDrawableColor(color: Int) {
        when (this) {
            is ShapeDrawable -> this.paint.color = color // cast to 'ShapeDrawable'
            is GradientDrawable -> // cast to 'GradientDrawable'
                this.setColor(color)
            is ColorDrawable -> // alpha value may need to be set again after this call
                this.color = color
            else -> this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    interface InputListener {
        fun afterTextChanged() {}
        fun onInputIconClick() {}
        fun onInputClick() {}
        fun onSuffixClick() {}
        fun onSuffixChanged() {}
        fun onEditTextFocusChange(hasFocus: Boolean) {}
    }

    fun getEditText(): EditText {
        return binding.txtInput
    }

    fun getImeOption() = binding.txtInput.imeOptions

    fun getEditorInfo(): EditorInfo {
        return EditorInfo().apply {
            binding.txtInput.let { et ->
                imeOptions = et.imeOptions
                actionId = et.imeActionId
                inputType = et.inputType
            }
        }
    }

    fun requestEdittextFocus(): Boolean {
        return binding.txtInput.requestFocus()
    }

    fun clearEdittextFocus() {
        binding.txtInput.clearFocus()
    }

    fun setPasswordEyeIconVisibility(isVisible: Boolean) {
        binding.cbPassword.setVisible(isVisible)
    }
}