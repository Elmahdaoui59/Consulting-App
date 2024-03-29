package com.eldebvs.consulting.domain.use_case.settings_use_case

import com.eldebvs.consulting.domain.repository.SettingsRepository

class EditUserEmail(
    private val repository: SettingsRepository
) {
    operator fun invoke(email: String, password: String) =
        repository.editUserEmail(email, password)
}