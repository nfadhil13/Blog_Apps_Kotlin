package com.codingwithmitch.openapi.dependcy_injection

import com.codingwithmitch.openapi.dependcy_injection.auth.AuthComponent
import com.codingwithmitch.openapi.dependcy_injection.main.MainComponent
import dagger.Module

@Module(
    subcomponents = [
        AuthComponent::class,
        MainComponent::class
    ]
)
class SubComponentModule