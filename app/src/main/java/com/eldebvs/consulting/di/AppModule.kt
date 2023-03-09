package com.eldebvs.consulting.di

import android.content.Context
import com.eldebvs.consulting.data.repository.AuthRepositoryImpl
import com.eldebvs.consulting.data.repository.ChatRepositoryImpl
import com.eldebvs.consulting.data.repository.SettingsRepositoryImpl
import com.eldebvs.consulting.domain.repository.AuthRepository
import com.eldebvs.consulting.domain.repository.ChatRepository
import com.eldebvs.consulting.domain.use_case.auth_use_case.*
import com.eldebvs.consulting.util.Constants.DATABASE_REFERENCE
import com.eldebvs.consulting.domain.repository.SettingsRepository
import com.eldebvs.consulting.domain.use_case.chat_use_case.ChatUseCases
import com.eldebvs.consulting.domain.use_case.chat_use_case.CreateChatroom
import com.eldebvs.consulting.domain.use_case.chat_use_case.LoadChatrooms
import com.eldebvs.consulting.domain.use_case.settings_use_case.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseStorageReference() = FirebaseStorage.getInstance().reference

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

    @Provides
    fun provideSettingsRepository(
        auth: FirebaseAuth,
        db: DatabaseReference,
        storageReference: StorageReference,
        @ApplicationContext context: Context
    ): SettingsRepository = SettingsRepositoryImpl(auth, db, storageReference, context)

    @Provides
    fun provideChatRepository(
        auth: FirebaseAuth,
        db: DatabaseReference
    ): ChatRepository = ChatRepositoryImpl(auth, db)

    @Provides
    fun provideAuthUseCases(
        repository: AuthRepository
    ): AuthUseCases = AuthUseCases(
        registerUser = RegisterUser(repository),
        signOutUser = SignOutUser(repository),
        signInUser = SignInUser(repository),
        getAuthState = GetAuthState(repository),
        resendVerificationEmail = ResendVerificationEmail(repository)
    )

    @Provides
    fun provideSettingsUseCases(
        repository: SettingsRepository
    ): SettingsUsesCases = SettingsUsesCases(
        editUserEmail = EditUserEmail(repository),
        editUserDetails = EditUserDetails(repository),
        getUserDetails = GetUserDetails(repository),
        resetUserPassword = ResetUserPassword(repository),
        uploadImage = UploadImage(repository),
        compressAndUploadImage = CompressAndUploadImage(repository)
    )
    @Provides
    fun provideChatUseCases(
        repository: ChatRepository
    ): ChatUseCases = ChatUseCases(
        createChatroom = CreateChatroom(repository),
        loadChatrooms = LoadChatrooms(repository)
    )



}