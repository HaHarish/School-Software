package com.newletseduvate.utils.oes_tool

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.newletseduvate.R
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentNewOesToolModifiedBinding
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.utils.rx_component_observables.RxImageButtonObservable
import com.newletseduvate.viewmodels.HomeworkStudentDetailOpenedViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * 1. Instantiate ViewModel in your fragment
 *
 * class YourFragment : Fragment() {
 * ..
 * private lateinit var oesViewModel: NewOesToolViewModel
 * ..
 *
 * oesViewModel = ViewModelProvider(requireActivity()).get(NewOesToolViewModel::class.java)
 *
 *  2. Send ArrayList of Bitmap in bundle
 *
 *  3. Observe isSaved variable which is in oesViewModel, when it changes to true, bitmap
 *  list will have updated files
 *
 */

//sending toolbar_name in argument is optional

class NewOesToolFragment : BaseFragment(R.layout.fragment_new_oes_tool_modified) {
    lateinit var binding: FragmentNewOesToolModifiedBinding

    private val viewModel by lazy { getViewModel<NewOesToolViewModel>(requireActivity()) }
    private lateinit var adapter: CustomBitmapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("viewmodel","$viewModel")
        viewModel.clear()

        val bundle = requireArguments()
        viewModel.bitmapList = bundle.getParcelableArrayList("bitmaps")!!
        viewModel.bitmapStaticList = ArrayList(viewModel.bitmapList)

        viewModel.fileSavedIndex = BooleanArray(viewModel.bitmapList.size)
        Arrays.fill(viewModel.fileSavedIndex, false)

        repeat(viewModel.bitmapList.size) {
            viewModel.imageFiles.add(File(""))
        }
    }

    fun initViewPager() {

        adapter = CustomBitmapAdapter(viewModel, viewLifecycleOwner)

        binding.rvBitmapList.adapter = adapter
        binding.rvBitmapList.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.currPage = position
                viewModel.currPage.value = position
            }
        })

        binding.btnSave.setOnClickListener {

//            viewModel.isSaved.value = true
//            findNavController().popBackStack()

            viewModel.prevPage.observe(viewLifecycleOwner, {

                Log.i("response", "main curr $it prev ${viewModel.currPage.value}")

                if (it == viewModel.bitmapList.size && viewModel.currPage.value == viewModel.bitmapList.size) {
                    if (isAllPageSaved()) {
                        viewModel.isSaved.value = true
                        findNavController().popBackStack()
                    } else
                        binding.root.snackBar("Some pages failed to save")
                }
            })

            viewModel.currPage.value = viewModel.bitmapList.size

        }
    }

    private fun isAllPageSaved(): Boolean {
        viewModel.fileSavedIndex.forEach {
            if (!it)
                return false
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewOesToolModifiedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvBitmapList.isUserInputEnabled = false
        binding.totalPages = viewModel.bitmapList.size - 1

        viewModel.disposable.add(
            RxImageButtonObservable.fromView(binding.btnLeft)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    binding.rvBitmapList.currentItem = viewModel.currPage.value?.minus(1) ?: 0
                }, {

                })
        )

        viewModel.disposable.add(
            RxImageButtonObservable.fromView(binding.btnRight)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    binding.rvBitmapList.currentItem =
                        viewModel.currPage.value?.plus(1) ?: viewModel.bitmapList.size - 1
                }, {

                })
        )

        initViewPager()
    }

    companion object {

        fun getFile(context: Context, fileName: String): File {
            val mFolder = File("" + context.filesDir + "/sample")
            val imgFile = File(mFolder.absolutePath, fileName)
            if (!mFolder.exists()) {
                mFolder.mkdir()
            }
            if (!imgFile.exists()) {
                try {
                    imgFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return imgFile
        }

        fun getMimeType(url: String): String {
            val extension = url.substring(url.lastIndexOf(".") + 1, url.length)
            return MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(extension.toLowerCase(Locale.ROOT))
                .toString()
        }
    }
}