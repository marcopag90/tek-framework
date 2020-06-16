package com.tek.security.common.service

import com.querydsl.core.types.Predicate
import com.tek.security.common.form.ChangePasswordForm
import com.tek.security.common.form.ChangeEmailForm
import com.tek.security.common.form.UserCreateForm
import com.tek.security.common.model.TekProfile
import com.tek.security.common.model.TekUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TekUserService {

    /** Method do create a user from within the application **/
    fun create(form: UserCreateForm)

    /** Method do read a user by id **/
    fun read(id: Long): TekUser

    /** Method to fetch a list of users **/
    fun list(pageable: Pageable, predicate: Predicate?): Page<TekUser>

    /** Method to change email **/
    fun changeEmail(form: ChangeEmailForm): TekUser

    /** Method to change password **/
    fun changePassword(form: ChangePasswordForm): TekUser

    /** Method to change profiles **/
    fun changeProfiles(profiles: Set<Long>)

    /** Method to change enabled state to lock/unlock login attempts **/
    fun setEnabled(enabled: Boolean): TekUser

    /** Method to remove a profile and invalidate session **/
    fun removeUserProfileAndInvalidate(profile: TekProfile)
}