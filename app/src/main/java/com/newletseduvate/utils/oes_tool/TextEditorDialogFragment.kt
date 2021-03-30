package com.newletseduvate.utils.oes_tool

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.newletseduvate.R
import com.newletseduvate.databinding.OesToolAddTextBinding

class TextEditorDialogFragment : DialogFragment(), View.OnClickListener {
    private var mTextEditor: TextEditor? = null
    private var binding: OesToolAddTextBinding? = null
    lateinit var viewmodel: AddTextViewModel
    private lateinit var mInputMethodManager: InputMethodManager
    private var mColorCode = 0

    companion object {
        val TAG = TextEditorDialogFragment::class.java.simpleName
        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val EXTRA_COLOR_CODE = "extra_color_code"
    }

    interface TextEditor {
        fun onDone(inputText: String?, colorCode: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        retainInstance = true
        viewmodel = ViewModelProvider(this).get(AddTextViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.oes_tool_add_text,
                container,
                false
        )
        return binding!!.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding!!.addtextmodel = viewmodel
        binding!!.setVariable(BR.addtextmodel, viewmodel)
        binding!!.lifecycleOwner = this
        binding!!.executePendingBindings()
        viewmodel.colordata.observe(viewLifecycleOwner, Observer { string ->
            val colorPickerAdapter = ColorPickerAdapter(requireActivity(), string)
            //This listener will change the text color when clicked on any color from picker
            //This listener will change the text color when clicked on any color from picker
            colorPickerAdapter.setOnColorPickerClickListener(object :
                    ColorPickerAdapter.OnColorPickerClickListener {
                override fun onColorPickerClickListener(colorCode: Int) {
                    mColorCode = colorCode
                    binding!!.addTextEditText.setTextColor(colorCode)
                }
            })
            binding!!.addTextColorPickerRecyclerView.adapter = colorPickerAdapter
        })
        binding!!.addTextEditText.textSize = 17f
        binding!!.addTextEditText.setText(arguments!!.getString(EXTRA_INPUT_TEXT))
        mColorCode = arguments!!.getInt(EXTRA_COLOR_CODE)
        binding!!.addTextEditText.setTextColor(mColorCode)
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        binding!!.addTextDoneTv.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        //Make dialog full screen with transparent background
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun setOnTextEditorListener(textEditor: TextEditor) {
        mTextEditor = textEditor
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.add_text_done_tv -> {
                mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                dismiss()
                val inputText: String = binding!!.addTextEditText.text.toString()
                if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                    mTextEditor!!.onDone(inputText, mColorCode)
                }
            }
            else -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (binding != null) {
            binding = null
        }
    }
}