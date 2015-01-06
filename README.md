AS
==
http://stackoverflow.com/questions/16858086/why-wont-android-studio-find-my-resources

import android.R;

http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Introduction

/Users/sscsis/Desktop/AS-master/app/src/main/res



sourceSets {
    main {
        manifest.srcFile 'AndroidManifest.xml'
        java.srcDirs = ['src/java']
        resources.srcDirs = ['src/main/res']
        aidl.srcDirs = ['src']
        renderscript.srcDirs = ['src']
        res.srcDirs = ['src/main/res']
        assets.srcDirs = ['assets']
    }
}



http://www.gradle.org/docs/current/userguide/java_plugin.html
