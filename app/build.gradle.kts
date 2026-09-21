signingConfigs {
    create("release") {
        val keystorePath = System.getenv("KEYSTORE_FILE") ?: ""
        if (keystorePath.isNotEmpty()) {
            storeFile = file(keystorePath)
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = "upload"
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
}

buildTypes {
    release {
        isCrunchPngs = false
        isMinifyEnabled = false
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )

        if (System.getenv("KEYSTORE_FILE") != null) {
            signingConfig = signingConfigs.getByName("release")
        }
    }

    debug {
        // Default Android debug keystore use hoga.
    }
}
