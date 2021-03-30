package com.newletseduvate.utils.constants

import android.content.SharedPreferences
import androidx.core.content.edit
import com.newletseduvate.R
import com.newletseduvate.model.login.LoginSuccessResponse

object ModulesConstant {

    object OnlineClassModule {
        const val onlineClass = "Online Class"

        const val createClass = "Create Class"
        const val viewClass = "View Class"
        const val attendOnlineClass = "Attend Online Class"
        const val resources = "Resources"
        const val teacherViewClass = "Teacher View Class"
    }

    object Diary {
        const val diary = "Diary"
        const val studentDiary = "Student Diary"
        const val teacherDiary = "Teacher Diary"
    }

    object Blog {
        const val blog = "Blogs"
        const val studentBlog = "Student Blogs"
    }

    object Homework {
        const val homework = "Homework"

        const val studentHomework = "Student Homework"
        const val teacherHomework = "Teacher Homework"
    }

    object LessonPlan {
        const val lessonPlan = "Lesson Plan"
        const val studentView = "Student View"
        const val teacherView = "Teacher View"
    }

    object Circular {
        const val circular = "Circular"
        const val studentCircular = "Student Circular"
        const val teacherCircular = "Teacher Circular"
    }

    object Finance {
        const val finance = "Finance"
        const val feeStructure = "Fee Structure"
        const val managePayment = "Manage Payment"
    }

    fun saveId(
        navigationData: LoginSuccessResponse.Result.NavigationData,
        childModule: LoginSuccessResponse.Result.NavigationData.ChildModule,
        pref: SharedPreferences
    ) {
        val uniqueId = childModule.childId!!

        when (navigationData.parentModules) {
            OnlineClassModule.onlineClass -> {
                when (childModule.childName) {
                    OnlineClassModule.createClass -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${OnlineClassModule.onlineClass}${OnlineClassModule.createClass}"
                        )
                    }
                    OnlineClassModule.viewClass -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${OnlineClassModule.onlineClass}${OnlineClassModule.viewClass}"
                        )
                    }
                    OnlineClassModule.attendOnlineClass -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${OnlineClassModule.onlineClass}${OnlineClassModule.attendOnlineClass}"
                        )
                    }
                    OnlineClassModule.resources -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${OnlineClassModule.onlineClass}${OnlineClassModule.resources}"
                        )
                    }
                    OnlineClassModule.teacherViewClass -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${OnlineClassModule.onlineClass}${OnlineClassModule.teacherViewClass}"
                        )
                    }
                }
            }

            Diary.diary -> {
                when (childModule.childName) {
                    Diary.studentDiary -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Diary.diary}${Diary.studentDiary}"
                        )
                    }
                    Diary.teacherDiary -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Diary.diary}${Diary.teacherDiary}"
                        )
                    }
                }
            }

            Blog.blog -> {
                when(childModule.childName){
                    Blog.studentBlog -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Blog.blog}${Blog.studentBlog}"
                        )
                    }
                }
            }

            Homework.homework -> {
                when(childModule.childName){
                    Homework.studentHomework -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Homework.homework}${Homework.studentHomework}"
                        )
                    }
                    Homework.teacherHomework -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Homework.homework}${Homework.teacherHomework}"
                        )
                    }
                }
            }

            LessonPlan.lessonPlan -> {
                when(childModule.childName){
                    LessonPlan.studentView -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${LessonPlan.lessonPlan}${LessonPlan.studentView}"
                        )
                    }
                    LessonPlan.teacherView -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${LessonPlan.lessonPlan}${LessonPlan.teacherView}"
                        )
                    }
                }
            }

            Circular.circular -> {
                when(childModule.childName){
                    Circular.studentCircular -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Circular.circular}${Circular.studentCircular}"
                        )
                    }
                    Circular.teacherCircular -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Circular.circular}${Circular.teacherCircular}"
                        )
                    }
                }
            }

            Finance.finance -> {
                when(childModule.childName){
                    Finance.feeStructure -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Finance.finance}${Finance.feeStructure}"
                        )
                    }
                    Finance.managePayment -> {
                        putInSharedPref(
                            pref,
                            uniqueId,
                            "${Finance.finance}${Finance.managePayment}"
                        )
                    }
                }
            }
        }
    }

    fun getSavedId(identifier: Int, pref: SharedPreferences): Int {

        if (identifier == 0)
            return R.id.nav_home_fragment

        when (getFromSharedPref(pref, identifier)) {
            //Online Class
//            "${OnlineClassModule.onlineClass}${OnlineClassModule.viewClass}" -> return R.id.nav_view_online_class
            "${OnlineClassModule.onlineClass}${OnlineClassModule.attendOnlineClass}" -> return R.id.nav_attend_online_class
            "${OnlineClassModule.onlineClass}${OnlineClassModule.teacherViewClass}" -> return R.id.nav_teacher_view_online_class
            "${OnlineClassModule.onlineClass}${OnlineClassModule.resources}" -> return R.id.nav_teacher_resources_online_class
            "${OnlineClassModule.onlineClass}${OnlineClassModule.createClass}" -> return R.id.nav_online_class_create

            //Diary
            "${Diary.diary}${Diary.studentDiary}" -> return R.id.nav_student_diary
            "${Diary.diary}${Diary.teacherDiary}" -> return R.id.nav_teacher_diary_home

            //Blog
            "${Blog.blog}${Blog.studentBlog}" -> return R.id.nav_student_blog

            //Homework
            "${Homework.homework}${Homework.studentHomework}" -> return R.id.nav_student_homework
            "${Homework.homework}${Homework.teacherHomework}" -> return R.id.nav_teacher_homework

            //LessonPlan
            "${LessonPlan.lessonPlan}${LessonPlan.studentView}" -> return R.id.nav_lesson_plan
            "${LessonPlan.lessonPlan}${LessonPlan.teacherView}" -> return R.id.nav_teacher_lesson_plan_home

            //Circular
            "${Circular.circular}${Circular.studentCircular}" -> return R.id.nav_circular
            "${Circular.circular}${Circular.teacherCircular}" -> return R.id.nav_circular_teacher_main

            //Finance
            "${Finance.finance}${Finance.feeStructure}" -> return R.id.nav_finance_fee_structure
            "${Finance.finance}${Finance.managePayment}" -> return R.id.nav_finance_manage_payment

        }

        return R.id.nav_home_fragment
    }

    private fun putInSharedPref(pref: SharedPreferences, key: Int, value: String) {
        pref.edit {
            putString(key.toString(), value)
        }
    }

    private fun getFromSharedPref(pref: SharedPreferences, key: Int): String {
        return pref.getString(key.toString(), "")!!
    }

}