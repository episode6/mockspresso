package com.episode6.hackit.mockspresso.guavapack;

import com.google.common.reflect.TypeToken;

class Stub {
    private static TypeToken<String> get() {
        return new TypeToken<String>() {};
    }
}
