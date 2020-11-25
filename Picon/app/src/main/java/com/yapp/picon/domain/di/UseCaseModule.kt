package com.yapp.picon.domain.di

import com.yapp.picon.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single {
        GetLocalUseCase(get())
    }
    single {
        GetAllSearedUseCase(get())
    }
    single {
        InsertSearedUseCase(get())
    }
    single {
        DeleteSearedUseCase(get())
    }
    single {
        SimpleJoinUseCase(get())
    }
    single {
        LoginUseCase(get())
    }
    single {
        SaveAccessTokenUseCase(get())
    }
    single {
        LoadAccessTokenUseCase(get())
    }
    single {
        LogoutUseCase(get())
    }
    single {
        UploadImageUseCase(get())
    }
    single {
        CreatePostUseCase(get())
    }
    single {
        RequestPostsUseCase(get(), get())
    }
    single {
        LoadFirstUseCase(get())
    }
    single {
        SaveFirstUseCase(get())
    }
}