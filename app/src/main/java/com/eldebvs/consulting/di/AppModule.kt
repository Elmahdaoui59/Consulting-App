package com.eldebvs.consulting.di

import com.eldebvs.consulting.data.repository.AuthRepositoryImpl
import com.eldebvs.consulting.domain.repository.AuthRepository
import com.eldebvs.consulting.domain.use_case.auth_use_case.*
import com.eldebvs.consulting.data.util.Constants.DATABASE_REFERENCE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideAuthUseCases(
        repository: AuthRepository
    ): AuthUseCases = AuthUseCases(
        registerUser = RegisterUser(repository),
        signOutUser = SignOutUser(repository),
        signInUser = SignInUser(repository),
        getAuthState = GetAuthState(repository),
        resendVerificationEmail = ResendVerificationEmail(repository),
        getUserDetails = GetUserDetails(repository),
        editUserDetails = EditUserDetails(repository),
        resetUserPassword = ResetUserPassword(repository),
        editUserEmail = EditUserEmail(repository)
    )

    @Provides
    fun provideFirebaseDatabaseReference() = Firebase
        .database(DATABASE_REFERENCE)
        .reference



    @Provides
    fun provideAuthFirebase() = Firebase.auth

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
        db: DatabaseReference
    ): AuthRepository = AuthRepositoryImpl(auth, db)
}