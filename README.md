# Binance Ninja

Binance Ninja is a Kotlin Multiplatform project targeting Android, iOS, Desktop. It enables you to
get notifications for P2P orders on Binance.

You can set filters, and It’ll alert you when any order satisfies the filter criteria.

### Project Configuration

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)

### Libraries
[KTOR - Networking](https://ktor.io/docs/http-client-multiplatform.html)
[KotlinX Serialization](https://github.com/Kotlin/kotlinx.serialization)
[SQLDelight Persistence](https://cashapp.github.io/sqldelight/2.0.0)
[Napier - Logging](https://github.com/AAkira/Napier)
[Kotlin Inject](https://github.com/evant/kotlin-inject)
[Voyager Navigation](https://github.com/adrielcafe/voyager)
[Compose](https://developer.android.com/jetpack/compose)
[Notification Desktop](https://github.com/dorkbox/Notify)
[Lyricist Localization](https://github.com/adrielcafe/lyricist)

### Credits
[Twine](https://github.com/msasikanth/twine)

### Known Issues
Kotlin Inject: Currently there are issues with scoping and redundant application of component scoping as per [this issue](https://github.com/evant/kotlin-inject/issues/320)

### TODO
- Add tests
- Add linter/formatting checks
- Add CI/CD
- Add Crypto Selection Feature
