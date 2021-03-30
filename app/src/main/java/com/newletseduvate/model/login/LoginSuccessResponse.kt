package com.newletseduvate.model.login


import com.google.gson.annotations.SerializedName

data class LoginSuccessResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: Result?
) {
    data class Result(
        @SerializedName("user_details")
        var userDetails: UserDetails?,
        @SerializedName("navigation_data")
        var navigationData: List<NavigationData?>?
    ) {
        data class UserDetails(
            @SerializedName("user_id")
            var userId: Int?,
            @SerializedName("email")
            var email: String?,
            @SerializedName("first_name")
            var firstName: String?,
            @SerializedName("last_name")
            var lastName: String?,
            @SerializedName("token")
            var token: String?,
            @SerializedName("is_superuser")
            var isSuperuser: Boolean?,
            @SerializedName("role_details")
            var roleDetails: RoleDetails?,
            @SerializedName("personal_info")
            var personalInfo: PersonalInfo?,
            @SerializedName("erp")
            var erp: String?
        ) {
            data class RoleDetails(
                @SerializedName("erp_user_id")
                var erpUserId: Int?,
                @SerializedName("name")
                var name: String?,
                @SerializedName("user_role")
                var userRole: String?,
                @SerializedName("user_profile")
                var userProfile: String?,
                @SerializedName("role_id")
                var roleId: Int?,
                @SerializedName("is_active")
                var isActive: Boolean?,
                @SerializedName("gender")
                var gender: String?,
                @SerializedName("branch")
                var branch: List<Branch?>?,
                @SerializedName("grades")
                var grades: List<Grade?>?
            ) {
                data class Branch(
                    @SerializedName("id")
                    var id: Int?,
                    @SerializedName("branch_name")
                    var branchName: String?
                )

                data class Grade(
                    @SerializedName("id")
                    var id: Int?,
                    @SerializedName("grade_id")
                    var gradeId: Int?,
                    @SerializedName("grade__grade_name")
                    var gradeGradeName: String?
                )
            }

            data class PersonalInfo(
                @SerializedName("role")
                var role: String?
            )
        }

        data class NavigationData(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("parent_modules")
            var parentModules: String?,
            @SerializedName("child_module")
            var childModule: List<ChildModule?>?
        ) {
            data class ChildModule(
                @SerializedName("child_id")
                var childId: Int?,
                @SerializedName("child_name")
                var childName: String?
            )
        }
    }
}