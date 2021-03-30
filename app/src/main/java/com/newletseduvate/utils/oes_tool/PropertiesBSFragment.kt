package com.newletseduvate.utils.oes_tool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.newletseduvate.R
import com.newletseduvate.databinding.FragmentBottomBrushBinding

class PropertiesBSFragment : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener {
    private var binding: FragmentBottomBrushBinding? = null
    lateinit var viewmodel: AddTextViewModel
    private var mProperties: Properties? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        retainInstance = true
        viewmodel = ViewModelProvider(this).get(AddTextViewModel::class.java)
    }

    interface Properties {
        fun onColorChanged(colorCode: Int)
        fun onOpacityChanged(opacity: Int)
        fun onBrushSizeChanged(brushSize: Int)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_bottom_brush,
                container,
                false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.bottombrushmodel = viewmodel
        binding!!.setVariable(BR.bottombrushmodel, viewmodel)
        binding!!.lifecycleOwner = this
        binding!!.executePendingBindings()
        viewmodel.colordata.observe(viewLifecycleOwner, Observer { string ->
            val colorPickerAdapter = ColorPickerAdapter(requireActivity(), string)
            //This listener will change the text color when clicked on any color from picker
            //This listener will change the text color when clicked on any color from picker
            colorPickerAdapter.setOnColorPickerClickListener(object :
                    ColorPickerAdapter.OnColorPickerClickListener {
                override fun onColorPickerClickListener(colorCode: Int) {
                    if (mProperties != null) {
                        dismiss()
                        mProperties!!.onColorChanged(colorCode)
                    }
                }
            })
            binding!!.rvColors.adapter = colorPickerAdapter
        })
        binding!!.sbOpacity.setOnSeekBarChangeListener(this)
        binding!!.sbSize.setOnSeekBarChangeListener(this)

    }

    fun setPropertiesChangeListener(properties: Properties) {
        mProperties = properties
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        when (seekBar.id) {
            R.id.sbOpacity -> if (mProperties != null) {
                mProperties!!.onOpacityChanged(i)
            }
            R.id.sbSize -> if (mProperties != null) {
                mProperties!!.onBrushSizeChanged(i)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    override fun onDestroyView() {
        super.onDestroyView()
        if (binding != null) {
            binding = null
        }
    }
}