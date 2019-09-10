package org.fmod.finaltest.bean.remote

import com.google.gson.annotations.Expose

data class Login(
    @Expose var token: String
)