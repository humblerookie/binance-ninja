package dev.anvith.binanceninja.di

import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import me.tatarka.inject.annotations.Scope

@Scope @Target(CLASS, FUNCTION, PROPERTY_GETTER) annotation class AppScope
