package com.newletseduvate.utils.constants

import com.newletseduvate.BuildConfig


object ApiConstants {

    const val ACCEPT_APP_JSON = "application/json"
    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer "

    private const val currentBase = BuildConfig.BASE_URL

    const val loginUrl: String = "erp_user/login/"

    //Diary
    const val generalDiaryMessagesUrl = "academic/general-dairy-messages/"
    const val getTeacherDiaryActiveStudentsList = "academic/general-dairy-users/"
    const val teacherDiaryFileUpload = "academic/dairy-upload/"
    const val teacherDiaryBranch = "erp_user/list-all-branch/"
    const val teacherDiaryCreate = "academic/create-dairy/"
    const val teacherDiarySubjects = "erp_user/subject/"
    const val deleteDiaryUrl = "academic/{diary_id}/update-delete-dairy/"
    const val teacherDiaryChapters = "academic/chapters/"

    //Blog
    const val publishedBlogUrl = "academic/blog/"
    const val studentBlogUrl = "academic/blog/"
    const val studentBlogGenreUrl = "academic/genre/"
    const val studentBlogCreateUrl = "academic/blog/"

    //Online class
    const val studentOnlineClassUrl = "erp_user/student_online_class/"
    const val studentOnlineClassOcDetailsUrl = "erp_user/{zoom_id}/student-oc-details/"
    const val studentOnlineClassMarkAttendanceUrl = "erp_user/mark_attendance/"
    const val studentOnlineClassResourceFileUrl = "erp_user/resource_files/"
    const val viewCoursePlanUrl = "aol/coursetag/"
    const val teacherAcademicYearUrl = "erp_user/list-academic_year/"
    const val teacherViewClassBranchUrl = "erp_user/branch/"
    const val teacherViewClassGradeMappingUrl = "erp_user/grademapping/"
    const val teacherViewClassSectionMappingUrl = "erp_user/sectionmapping/"
    const val teacherViewClassSubjectsUrl = "erp_user/subject/"
    const val teacherViewClassTeacherOnlineClassUrl = "erp_user/teacher_online_class/"
    const val teacherViewClassTeacherDetailsUrl = "erp_user/{teacher_class_id}/online-class-details/"
    const val teacherViewClassTeacherDetailsCancelClassUrl = "erp_user/cancel-online-class/"
    const val teacherViewClassCourseUrl = "aol/courses/"
    const val teacherViewClassResourcesUrl = "erp_user/resource_files/"
    const val teacherResourceUploadUrl = "erp_user/resource_files/"
    const val teacherViewClassAttendanceUrl = "erp_user/onlineclass_attendeelist/"
    const val teacherViewClassMarkAttendanceUrl = "erp_user/mark_attendance/"
    const val createClassSubSectionUrl = "erp_user/sub-sec-list/"
    const val createClassTutorEmailUrl = "erp_user/teacher-list/"
    const val createClassCheckTutorTimeUrl = "erp_user/check-tutor-time/"
    const val createClassOnlineClassStudentFilterUrl = "erp_user/student_filter/"
    const val createClassOnlineRecurringUrl = "erp_user/online-recurring/"
    const val createClassOnlineOnlineErp = "erp_user/online-erp-class/"

    //Homework
    const val homeworkStudentUrl = "academic/student-homework/"
    const val homeworkStudentDetailsUrl = "academic/{hw_details_id}/hw-questions/"
    const val homeworkStudentUploadQuestionFile = "academic/upload-question-file/"
    const val homeworkStudentDeleteFile = "academic/delete-file/"
    const val homeworkStudentSubmission = "academic/homework-submission/"
    const val homeworkTeacherSubmittedUrl = "academic/{hw_details_id}/hw-questions/"
    const val homeworkTeacherEvaluationCompletedUrl = "academic/{hw_details_id}/evaluation-completed/"
    const val homeworkTeacherSubmittedDataUrl = "academic/homework-submitted-data/"
    const val homeworkTeacherStudentHomeworkUrl = "academic/stu-submited-data/"
    const val homeworkTeacherUploadHomeworkUrl = "academic/upload-homework/"
    const val homeworkEvaluationCompleted = "academic/{student_homework}/evaluation-completed/"
    const val homeworkTeacherEvaluationUrl = "academic/{question_id}/teacher-evaluation/"
    const val homeworkTeacherResourcesUploadUrl = "academic/dairy-upload/"
    const val homeworkTeacherResourcesDeleteUrl = "academic/delete-file/"

    //LessonPlan
    const val lessonPlanAcademicYearUrl = "https://dev.mgmt.letseduvate.com/qbox/lesson_plan/list-session/"
    const val lessonPlanVolume = "https://dev.mgmt.letseduvate.com/qbox/lesson_plan/list-volume/"
    const val lessonPlanBranch = "erp_user/branch/"
    const val lessonPlanGrade = "erp_user/grademapping/"
    const val lessonPlanSubject = "academic/lesson-plan-subjects/"
    const val lessonPlanChapter = "academic/central-chapters-list/"

    //LessonPlan Teacher
    const val teacherLessonPlanFilterResults = "https://mgmt.letseduvate.com/qbox/lesson_plan/chapter-period/"
    const val teacherLessonPlanViewMoreResults = "https://mgmt.letseduvate.com/qbox/lesson_plan/lesson/"
    const val teacherLessonPlanCompleted = "academic/lessonplan-completed-status/"

    //Profile
    const val changePasswordUrl = "erp_user/{userId}/change-password/"
    const val changeProfilePicture = "erp_user/{userId}/update-user-profile/"
    const val getProfileDetails = "erp_user/user-data/"

    //Forgot Password
    const val forgotPasswordUrl = "erp_user/forgot-password/"

    //Finance
    const val financeStudentFeeDisplayUrl = "finance/studentfeedisplay/"
    const val financeGetYearUrl = "finance/list-acad-session"
    const val financeFeeDetails = "finance/feedisplay/"
    const val financeMakePayment = "finance/studentmakepayment/"
    const val financeAllTransactions = "finance/studenttransactionataccountant/"

    //Circular
    const val circularFilterResponse = "circular/upload-circular/"
    const val circularAcademicYear = "erp_user/list-academic_year/"
    const val circularBranch = "erp_user/branch/"
    const val circularGrade = "erp_user/grademapping/"
    const val circularSection = "erp_user/sectionmapping/"
    const val circularTeacherFilterResponse = "circular/upload-circular/"
    const val teacherCircularCreate = "circular/upload-circular/"
    const val teacherCircularDelete = "circular/delete-circular/"
    const val teacherCircularUploadFiles = "circular/upload-circular-file/"
    const val teacherCircularDeleteFiles = "academic/delete-file/"
}

