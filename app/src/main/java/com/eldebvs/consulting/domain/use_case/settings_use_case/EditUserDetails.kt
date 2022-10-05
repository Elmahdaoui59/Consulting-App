package com.eldebvs.consulting.domain.use_case.settings_use_case

import com.eldebvs.consulting.domain.repository.SettingsRepository

class EditUserDetails(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(name: String?, phone: String?) = repository.editUserDetails(name, phone)
}