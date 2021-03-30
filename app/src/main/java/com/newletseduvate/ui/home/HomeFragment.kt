package com.newletseduvate.ui.home

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.newletseduvate.R
import com.newletseduvate.adapter.ViewCoursePlanAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.Status
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.profile.ChangePasswordRequest
import com.newletseduvate.ui.activities.MainActivity
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.constants.CameraUtils
import com.newletseduvate.utils.constants.RequestCode
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.viewmodels.HomeViewModel
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_view_course_plan.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment(R.layout.fragment_home){

    private val viewModel by lazy { getViewModel<HomeViewModel>() }
    private lateinit var dialog: Dialog
    private lateinit var oldPassword: EditText
    private lateinit var newPassword: EditText
    private lateinit var confirmNewPassword: EditText
    private lateinit var cancelDialog: Button
    private lateinit var confirmDialog: Button

    private var mHighQualityImageUri: Uri? = null
    private val dataPartList = ArrayList<UploadFileModel>()
    var compositeDisposable = CompositeDisposable()
    private var wordCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initializeDialog()
        clickEvents()
        displayProfileDetails()
        initObservers()
    }

    private fun initObservers(){
        viewModel.changeProfilePicture.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                withContext(Dispatchers.Main){
                    dismissProgress()
                }
            }

            it?.let {
                when(it.status){
                    Status.Success -> {
                        GlobalScope.launch {
                            viewModel.getProfileDetails()
                            withContext(Dispatchers.Main){
                                displayProgress()
                            }
                        }
                    }
                    Status.Error -> {
                        Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.getProfileDetailsResponse.observe(viewLifecycleOwner,{
            GlobalScope.launch {
                withContext(Dispatchers.Main){
                    dismissProgress()
                }
            }
            it?.let {
                when(it.status){
                    Status.Success -> {
                        Glide.with(requireContext())
                            .load(it.data?.result?.profile)
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(user_image)
                        editText_profile_name.setText("${it.data?.result?.user?.firstName} ${it.data?.result?.user?.lastName}")
                        editText_profile_email.setText(it.data?.result?.user?.email)
                        editText_profile_erp_id.setText(it.data?.result?.user?.username)
                        editText_profile_phone_no.setText(it.data?.result?.contact)
                    }
                    Status.Error -> {
                        Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.changePasswordResponse.observe(viewLifecycleOwner,{
            GlobalScope.launch {
                withContext(Dispatchers.Main){
                    dismissProgress()
                }
            }
            it?.let {
                when(it.status){
                    Status.Success -> {
                        if(::dialog.isInitialized){
                            if(dialog.isShowing){
                                dialog.dismiss()
                            }
                        }
                        Toast.makeText(activity,"Password Updated Successfully",Toast.LENGTH_SHORT).show()
                    }
                    Status.Error -> {
                        Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun displayProfileDetails() {
        GlobalScope.launch {
            viewModel.getProfileDetails()
            withContext(Dispatchers.Main){
              //  displayProgress()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).provideToolbarTitle()
    }

    fun initializeDialog() {
        try {
            dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.layout_change_password)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            oldPassword = dialog.findViewById(R.id.editText_changePassword_old)
            newPassword = dialog.findViewById(R.id.editText_changePassword_new)
            confirmNewPassword = dialog.findViewById(R.id.editText_changePassword_confirmNew)
            cancelDialog = dialog.findViewById(R.id.button_changePassword_cancel)
            confirmDialog = dialog.findViewById(R.id.button_changePassword_confirm)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun clickEvents() {
        button_profile_change_password.setOnClickListener {
            if(::dialog.isInitialized) dialog.show()
        }
        cancelDialog.setOnClickListener {
            if(::dialog.isInitialized){
                if(dialog.isShowing){
                    dialog.dismiss()
                }
            }
        }
        confirmDialog.setOnClickListener {
            if(oldPassword.text.trim().toString().isNotEmpty()){
                if(newPassword.text.trim().toString().isNotEmpty()){
                    if(confirmNewPassword.text.trim().toString().isNotEmpty()){
                        if((newPassword.text.trim().toString().isNotEmpty()).equals(confirmNewPassword.text.trim().toString().isNotEmpty())){
                            GlobalScope.launch {
                                viewModel.getProfileDetails()
                                withContext(Dispatchers.Main){
                                    displayProgress()
                                }
                            }
                            viewModel.changePassword(ChangePasswordRequest(oldPassword.text.trim().toString(),
                                newPassword.text.trim().toString()))
                        }else{
                            Toast.makeText(requireContext(),"New Password mismatch", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireContext(),"Enter New Confirm Password", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(),"Enter New Password", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(),"Enter Old Password", Toast.LENGTH_SHORT).show()
            }
        }
        user_image.setOnClickListener {
            showOptionMenu()
        }
    }

    /* Profile Pic edit */
    private fun showOptionMenu() {
        val optionsArray = ArrayList<BottomSheetIconItem>()

        optionsArray.add(
            BottomSheetIconItem(
                0,
                R.drawable.ic_camera,
                resources.getString(R.string.camera)
            )
        )

        optionsArray.add(
            BottomSheetIconItem(
                1,
                R.drawable.ic_menu_gallery,
                resources.getString(R.string.gallery)
            )
        )
        val bottomSheet = SingleSelectionIconBottomSheetFragment(
            0,
            optionsArray,
            resources.getString(R.string.select_file_from),
            object : SingleSelectionIconBottomSheetFragment.OnChooseReasonListener {

                override fun onChooseSingleItem(
                    BOTTOM_SHEET_ID: Int,
                    selectedItem: BottomSheetIconItem,
                    bottomSheetItemPostion: Int
                ) {
                    when (selectedItem.id) {
                        0 -> openCamera()
                        1 -> openGallery()
                    }
                }

            })
        bottomSheet.show(
            childFragmentManager,
            bottomSheet.tag
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri: Uri?
        var imageFile: String?
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REQUEST_CODE_GALLERY -> if (data!!.clipData != null) {
                    var i = 0
                    while (i < data.clipData!!.itemCount) {
                        uri = data.clipData!!.getItemAt(i).uri
                        imageFile = CameraUtils.getPath(requireActivity(), uri)
                        if (FileExtension.isImageFile(imageFile!!)) {
                            createBitmap(imageFile)
                        } else {
                            // binding.root.snackBar("This type of file is not supported")
                            Toast.makeText(requireContext(), "This type of file is not supported",Toast.LENGTH_SHORT).show()
                        }
                        i++
                    }
                } else {
                    uri = data.data
                    imageFile = CameraUtils.getPath(requireActivity(), uri!!)
                    if (FileExtension.isImageFile(imageFile!!)) {
                        createBitmap(imageFile)
                    } else {
                        // binding.root.snackBar("This type of file is not supported")
                        Toast.makeText(requireContext(), "This type of file is not supported",Toast.LENGTH_SHORT).show()
                    }
                }
                RequestCode.REQUEST_CODE_IMAGE_CAPTURE -> try {
                    val urifile = File(mHighQualityImageUri!!.path!!)
                    imageFile = urifile.path
                    if (FileExtension.isImageFile(imageFile)) {
                        createBitmap(imageFile)
                    } else {
                        // binding.root.snackBar("This type of file is not supported")
                        Toast.makeText(requireContext(), "This type of file is not supported",Toast.LENGTH_SHORT).show()
                    }
                } catch (e: java.lang.Exception) {
                    Toast.makeText(activity, "Image is too large. Choose other", Toast.LENGTH_LONG)
                        .show()
                }
            }
            checkFileAdapter(dataPartList)
        }
    }

    private fun checkFileAdapter(dataPartList: ArrayList<UploadFileModel>) {
        // adapter.replaceList(dataPartList)
        /*viewModel.changeProfilePicture(sharedPreferences.getToken()!!,
            dataPartList[0],
            sharedPreferences.getErpUserId().toString())*/
    }

    private fun prepareFilePart(
        partName: String,
        fileUri: String
    ): UploadFileModel {
        val file = File(fileUri)
        val requestFile = file.asRequestBody(getMimeType(fileUri)?.toMediaTypeOrNull())
        return UploadFileModel(file, requestFile)
    }

    private fun getMimeType(url: String): String? {
        try {
            var type: String? = null
            val extension = url.substring(url.lastIndexOf(".") + 1, url.length)
            Log.i("extension", "ext : $extension")
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    extension.toLowerCase(
                        Locale.getDefault()
                    )
                )
            }
            return type
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun openCamera() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        if (Build.VERSION.SDK_INT >= 23) {
            if (requireActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    RequestCode.RC_CAMERA_PERMISSION
                )
            } else {
                cameraOpen()

            }
        } else {
            cameraOpen()
        }
    }

    private fun cameraOpen() {
        mHighQualityImageUri = CameraUtils.generateTimeStampPhotoFileUri(requireActivity())
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri)
        startActivityForResult(
            intent,
            RequestCode.REQUEST_CODE_IMAGE_CAPTURE
        )
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    RequestCode.RC_STORAGE_PERMISSION
                )
            } else {
                galleryOpen()
            }
        } else {
            galleryOpen()
        }
    }

    private fun galleryOpen() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            intent,
            RequestCode.REQUEST_CODE_GALLERY
        )
    }

    private fun createBitmap(path: String) {

        Log.d("TAGY", "Path InsideCreateBitmap : "+path)
        val disposable = Compressor(activity)
            .setMaxWidth(640)
            .setMaxHeight(480)
            .setQuality(requireContext().quality(path))
            .compressToFileAsFlowable(File(path))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                prepareFilePart(getMimeType(it.path)!!, it.path).let { file ->
                    dataPartList.clear()
                    dataPartList.add(
                        file
                    )
                    viewModel.changeProfilePicture(sharedPreferences.getToken()!!,
                        file,
                        sharedPreferences.getErpUserId().toString())
                }
                checkFileAdapter(dataPartList)
            }, {
                it.printStackTrace()
            })
        compositeDisposable.add(disposable)
    }
}