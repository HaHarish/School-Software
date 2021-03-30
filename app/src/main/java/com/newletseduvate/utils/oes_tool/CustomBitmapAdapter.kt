package com.newletseduvate.utils.oes_tool

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.databinding.OesToolCustomBitmapListBinding
import com.newletseduvate.utils.extensions.snackBar
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import ja.burhanrashid52.photoeditor.ViewType
import java.io.File

internal class CustomBitmapAdapter(
    val viewModel: NewOesToolViewModel,
    val viewLifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<CustomBitmapAdapter.MyViewHolder>() {
    lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(holder: MyViewHolder, string: String?, selected: Boolean, position: Int)
    }

    class MyViewHolder(
        val binding: OesToolCustomBitmapListBinding,
        val viewModel: NewOesToolViewModel,
        val viewLifecycleOwner: LifecycleOwner
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.photoEditorView.source.scaleType = ImageView.ScaleType.FIT_XY
        }

        val mPropertiesBSFragment: PropertiesBSFragment = PropertiesBSFragment()
        private var onItemSelected: EditingToolsAdapter.OnItemSelected =
            object : EditingToolsAdapter.OnItemSelected {
                @SuppressLint("MissingPermission")
                override fun onToolSelected(toolType: ToolType?) {
                    when (toolType) {
                        ToolType.BRUSH -> {
                            viewModel.isBrushEnabled.value = true
                            mPhotoEditor.setBrushDrawingMode(true)
                            mPropertiesBSFragment.show(
                                (binding.root.context as FragmentActivity).supportFragmentManager,
                                mPropertiesBSFragment.tag
                            )
                        }
                        ToolType.TEXT -> {
                            val bundle = Bundle()
                            bundle.putString("extra_input_text", "")
                            bundle.putInt("extra_color_code", R.color.white)
                            val textEditorDialogFragment = TextEditorDialogFragment()
                            textEditorDialogFragment.arguments = bundle
                            textEditorDialogFragment.setOnTextEditorListener(object :
                                TextEditorDialogFragment.TextEditor {
                                override fun onDone(inputText: String?, colorCode: Int) {
                                    val styleBuilder = TextStyleBuilder()
                                    styleBuilder.withTextColor(colorCode)
                                    mPhotoEditor.addText(inputText, styleBuilder)
                                }
                            })
                            textEditorDialogFragment.show(
                                (binding.root.context as FragmentActivity).supportFragmentManager,
                                textEditorDialogFragment.tag
                            )
                        }

                        ToolType.SAVE -> {
                            mPhotoEditor.setBrushDrawingMode(false)
                        }
                        ToolType.ERASER -> {
                            mPhotoEditor.setBrushEraserSize(100f)
                            mPhotoEditor.brushEraser()
                        }
                        ToolType.LEFT -> {
                            binding.photoEditorView.rotation =
                                binding.photoEditorView.rotation - 90f
                        }
                        ToolType.RIGHT -> {
                            binding.photoEditorView.rotation =
                                binding.photoEditorView.rotation + 90f
                        }

                        ToolType.UNDO -> mPhotoEditor.undo()
                        ToolType.REDO -> mPhotoEditor.redo()
                        ToolType.RESET -> {
                            mPhotoEditor.setBrushDrawingMode(false)
                            mPhotoEditor.clearAllViews()
                            val image = viewModel.bitmapStaticList[adapterPosition]
                            binding.photoEditorView.rotation = 0F
                            binding.photoEditorView.source.setImageBitmap(image)
                        }

                        else -> {}
                    }
                }
            }

        var editingToolsAdapter = EditingToolsAdapter(onItemSelected)
        val mPhotoEditor: PhotoEditor =
            PhotoEditor.Builder(binding.root.context, binding.photoEditorView)
                .setPinchTextScalable(true)
                .build()

        fun bindTo(bitmap: Bitmap) {
            if (viewModel.isBrushEnabled.value!!)
                mPhotoEditor.setBrushDrawingMode(true)

            viewModel.isBrushEnabled.observe(viewLifecycleOwner, {
                if (it)
                    mPhotoEditor.setBrushDrawingMode(true)
            })

            binding.photoEditorView.source.setImageBitmap(bitmap)

            viewModel.brushSize.observe(viewLifecycleOwner, {
                if (it != -1 && viewModel.isBrushEnabled.value!!) mPhotoEditor.brushSize =
                    it.toFloat()
            })

            viewModel.brushColor.observe(viewLifecycleOwner, {
                if (it != -1 && viewModel.isBrushEnabled.value!!) mPhotoEditor.brushColor = it
            })

            viewModel.opacity.observe(viewLifecycleOwner, {
                if (it != -1 && viewModel.isBrushEnabled.value!!) mPhotoEditor.setOpacity(it)
            })

            if (viewModel.brushColor.value != -1 && viewModel.isBrushEnabled.value!!)
                mPhotoEditor.brushColor = viewModel.brushColor.value!!

            if (viewModel.opacity.value != -1 && viewModel.isBrushEnabled.value!!)
                mPhotoEditor.setOpacity(viewModel.opacity.value!!)

            if (viewModel.brushSize.value != -1 && viewModel.isBrushEnabled.value!!)
                mPhotoEditor.brushSize = viewModel.brushSize.value!!.toFloat()


            val llmTools =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvConstraintTools.layoutManager = llmTools
            binding.rvConstraintTools.adapter = editingToolsAdapter

            mPropertiesBSFragment.setPropertiesChangeListener(object :
                PropertiesBSFragment.Properties {
                override fun onColorChanged(colorCode: Int) {
                    mPhotoEditor.brushColor = colorCode
                    viewModel.brushColor.value = colorCode
                }

                override fun onOpacityChanged(opacity: Int) {
                    mPhotoEditor.setOpacity(opacity)
                    viewModel.opacity.value = opacity
                }

                override fun onBrushSizeChanged(brushSize: Int) {
                    mPhotoEditor.brushSize = brushSize.toFloat()
                    viewModel.brushSize.value = brushSize
                }
            })


            mPhotoEditor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
                override fun onEditTextChangeListener(
                    rootView: View,
                    text: String,
                    colorCode: Int
                ) {
                    val bundle = Bundle()
                    bundle.putString("extra_input_text", "")
                    bundle.putInt("extra_color_code", R.color.white)
                    val textEditorDialogFragment = TextEditorDialogFragment()
                    textEditorDialogFragment.arguments = bundle
                    textEditorDialogFragment.setOnTextEditorListener(object :
                        TextEditorDialogFragment.TextEditor {
                        override fun onDone(inputText: String?, colorCode: Int) {
                            val styleBuilder = TextStyleBuilder()
                            styleBuilder.withTextColor(colorCode)
                            mPhotoEditor.addText(inputText, styleBuilder)
                        }
                    })
                    textEditorDialogFragment.show(
                        (binding.root.context as FragmentActivity).supportFragmentManager,
                        textEditorDialogFragment.tag
                    )
                }

                override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {}
                override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {}
                override fun onStartViewChangeListener(viewType: ViewType) {}
                override fun onStopViewChangeListener(viewType: ViewType) {}
            })

            if (ActivityCompat.checkSelfPermission(
                    binding.root.context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            viewModel.currPage.observe(viewLifecycleOwner, {

                Log.i("response", "outside curr $it prev ${viewModel.prevPage.value}")
                if (adapterPosition == viewModel.prevPage.value) {

                    val file = NewOesToolFragment.getFile(
                        binding.root.context,
                        "" + System.currentTimeMillis() + ".png"
                    )

                    mPhotoEditor.saveAsFile(file.path, object : PhotoEditor.OnSaveListener {
                        override fun onSuccess(@NonNull imagePath: String) {

                            if (file.path != null) {
                                val uploadFile = File(file.path)
                                binding.photoEditorView.source.setImageURI(Uri.fromFile(uploadFile))

                                val options = BitmapFactory.Options()
                                options.inJustDecodeBounds = false
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888

                                viewModel.bitmapList[adapterPosition] =
                                    BitmapFactory.decodeFile(file.path, options)
                                viewModel.imageFiles[adapterPosition] = uploadFile

                                viewModel.fileSavedIndex[adapterPosition] = true

                                Log.i("response", "saving page $adapterPosition")
                                viewModel.prevPage.value = it

                                System.gc()
                            } else
                                binding.root.snackBar("Previous page did not saved")
                        }

                        override fun onFailure(@NonNull exception: Exception) {}
                    })

                }

//                val file = NewOesToolFragment.getFile(
//                        binding.root.context,
//                        "" + System.currentTimeMillis() + ".png"
//                    )
//
//                mPhotoEditor.saveAsFile(file.path, object : PhotoEditor.OnSaveListener {
//                    override fun onSuccess(@NonNull imagePath: String) {
//
//                        if (file.path != null) {
//                            val uploadFile = File(file.path)
//                            binding.photoEditorView.source.setImageURI(Uri.fromFile(uploadFile))
//
//                            val options = BitmapFactory.Options()
//                            options.inJustDecodeBounds = false
//                            options.inPreferredConfig = Bitmap.Config.ARGB_8888
//
//                            viewModel.bitmapList[adapterPosition] =
//                                BitmapFactory.decodeFile(file.path, options)
//                            viewModel.imageFiles[adapterPosition] = uploadFile
//
//                            viewModel.fileSavedIndex[adapterPosition] = true
//
//                            Log.i("response", "saving page $adapterPosition")
//                            viewModel.prevPage.value = it
//
//                            System.gc()
//                        } else
//                            binding.root.snackBar("Previous page did not saved")
//                    }
//
//                    override fun onFailure(@NonNull exception: Exception) {}
//                })
            })

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        context = viewGroup.context
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = OesToolCustomBitmapListBinding.inflate(layoutInflater, viewGroup, false)
        return MyViewHolder(binding, viewModel, viewLifecycleOwner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindTo(viewModel.bitmapList[position])
    }

    override fun getItemCount(): Int {
        return viewModel.bitmapList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}