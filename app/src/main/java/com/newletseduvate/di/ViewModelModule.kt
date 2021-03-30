package com.newletseduvate.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newletseduvate.utils.oes_tool.NewOesToolViewModel
import com.newletseduvate.viewmodels.HomeViewModel
import com.newletseduvate.viewmodels.LoginViewModel
import com.newletseduvate.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

//    @Binds
//    @IntoMap
//    @ViewModelKey(LoginViewModel::class)
//    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun provideHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun provideOnBoardViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiaryStudentViewModel::class)
    abstract fun provideDiaryStudentViewModel(viewModel: DiaryStudentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassAttendViewModel::class)
    abstract fun provideOnlineClassAttendViewModel(viewModel: OnlineClassAttendViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassViewClassViewModel::class)
    abstract fun provideOnlineClassViewClassViewModel(viewModel: OnlineClassViewClassViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlogPublishedViewModel::class)
    abstract fun provideBlogPublishedViewModel(viewModel: BlogPublishedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkStudentViewModel::class)
    abstract fun provideHomeworkStudentViewModel(viewModel: HomeworkStudentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkTeacherViewModel::class)
    abstract fun provideHomeworkTeacherViewModel(viewModel: HomeworkTeacherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkStudentDetailsSubmittedViewModel::class)
    abstract fun provideHomeworkStudentDetailsSubmittedViewModel(viewModel: HomeworkStudentDetailsSubmittedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkStudentDetailOpenedViewModel::class)
    abstract fun provideHomeworkStudentDetailOpenedViewModel(viewModel: HomeworkStudentDetailOpenedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkStudentDetailsEvaluatedViewModel::class)
    abstract fun provideHomeworkStudentDetailsEvaluatedViewModel(viewModel: HomeworkStudentDetailsEvaluatedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassAttendDetailsViewModel::class)
    abstract fun provideOnlineClassAttendDetailsViewModel(viewModel: OnlineClassAttendDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassAttendResourceViewModel::class)
    abstract fun provideOnlineClassAttendResourceViewModel(viewModel: OnlineClassAttendResourceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlogStudentCreateNewViewModel::class)
    abstract fun provideBlogStudentCreateNewViewModel(viewModel: BlogStudentCreateNewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlogStudentViewModel::class)
    abstract fun provideBlogStudentViewModel(viewModel: BlogStudentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlogStudentDetailsViewModel::class)
    abstract fun provideBlogStudentDetailsViewModel(viewModel: BlogStudentDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LessonPlanViewModel::class)
    abstract fun provideLessonPlanViewModel(viewModel: LessonPlanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CircularViewModel::class)
    abstract fun provideCircularViewModel(viewModel: CircularViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewCoursePlanViewModel::class)
    abstract fun provideViewCoursePlanViewModel(viewModel: ViewCoursePlanViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(ForgotPasswordViewModel::class)
//    abstract fun provideForgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewOesToolViewModel::class)
    abstract fun provideNewOesToolViewModel(viewModel: NewOesToolViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeeStructureViewModel::class)
    abstract fun provideFeeStructureViewModel(viewModel: FeeStructureViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManageFinanceViewModel::class)
    abstract fun provideManageFinanceViewModel(viewModel: ManageFinanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CircularTeacherViewModel::class)
    abstract fun provideCircularTeacherViewModel(viewModel: CircularTeacherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkTeacherAddHomeworkViewModel::class)
    abstract fun provideHomeworkTeacherAddHomeworkViewModel(viewModel: HomeworkTeacherAddHomeworkViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(HomeworkTeacherSubmittedViewModel::class)
    abstract fun provideHomeworkTeacherSubmittedViewModel(viewModel: HomeworkTeacherSubmittedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkTeacherDetailsHWEvaluatedViewModel::class)
    abstract fun provideHomeworkTeacherDetailsHWEvaluatedViewModel(viewModel: HomeworkTeacherDetailsHWEvaluatedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkTeacherEvaluateViewModel::class)
    abstract fun provideHomeworkTeacherEvaluateViewModel(viewModel: HomeworkTeacherEvaluateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeworkTeacherSubmitViewModel::class)
    abstract fun provideHomeworkTeacherSubmitViewModel(viewModel: HomeworkTeacherSubmitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassTeacherViewClassViewModel::class)
    abstract fun provideOnlineClassTeacherViewClassViewModel(viewModel: OnlineClassTeacherViewClassViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassTeacherViewClassDetailsViewModel::class)
    abstract fun provideOnlineClassTeacherViewClassDetailsViewModel(viewModel: OnlineClassTeacherViewClassDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassTeacherResourcesViewModel::class)
    abstract fun provideOnlineClassTeacherResourcesViewModel(viewModel: OnlineClassTeacherResourcesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassTeacherResourcesUploadViewModel::class)
    abstract fun provideOnlineClassTeacherResourcesUploadViewModel(viewModel: OnlineClassTeacherResourcesUploadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassTeacherAttendanceViewModel::class)
    abstract fun provideOnlineClassTeacherAttendanceViewModel(viewModel: OnlineClassTeacherAttendanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineClassCreateClassViewModel::class)
    abstract fun provideOnlineClassCreateClassViewModel(viewModel: OnlineClassCreateClassViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TeacherDiaryCreateFilterViewModel::class)
    abstract fun provideTeacherDiaryCreateFilterViewModel(viewModel: TeacherDiaryCreateFilterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiaryTeacherViewModel::class)
    abstract fun provideTeacherDiaryTeacherViewModel(viewModel: DiaryTeacherViewModel): ViewModel


//    @Binds
//    @IntoMap
//    @ViewModelKey(ForgotPasswordViewModel::class)
//    abstract fun provideForgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(LoginViewModel::class)
//    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel

}
