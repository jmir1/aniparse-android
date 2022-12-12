# aniparse-android

![Build](https://github.com/jmir1/aniparse-android/workflows/Pre%20Merge%20Checks/badge.svg)

## Summary

This is an android wrapper for the python module [Aniparse](https://github.com/MeGaNeKoS/Aniparse)

Huge thanks for the useful repos and inspiration go to:
 - [@MeGaNeKoS](https://github.com/MeGaNeKoS) for Aniparse,
 - [@erengy](https://github.com/erengy) for [Anitomy](https://github.com/erengy/anitomy) and
 - [@joaoventura](https://github.com/joaoventura) for [pybridge](https://github.com/joaoventura/pybridge).

## Usage

build.gradle.kts:
```kotlin
dependencies {
    implementation("com.github.jmir1:aniparse-android:0.2")
}
```

```kotlin
// [...]
import com.github.jmir1.aniparseandroid.library.Parser
// [...]
    Parser.start(context)
    val aniparseResult = Parser.parse("[TaigaSubs]_Toradora!_(2008)_-_01v2_-_Tiger_and_Dragon_[1280x720_H.264_FLAC][1234ABCD].mkv")
    val animeTitle = aniparseResult?.animeTitle // "ToraDora!"
    val episodeTitle = aniparseResult?.episodeTitle // "Tiger and Dragon"
    val releaseGroup = aniparseResult?.releaseGroup // "TaigaSubs"
    Parser.stop()
// [...]
```

## Building

The scripts in [library-android/buildscripts](https://github.com/jmir1/aniparse-android/tree/main/library-android/buildscripts) only work on Debian-like systems at the moment.
```shell
cd library-android/buildscripts
./download.sh # This will download the Android SDK and NDK and all other requirements of python-for-android
./build_python.sh # This will run p4a to build the Python distribution and copy all libraries and assets to the src folder
cd ../..
./gradlew library-android:build # You can also use Android Studio if you have successfully run build_python.sh.
```

## Known issues

- The library can only be built on Linux.
  I have no idea if it's possible to run python-for-android on Windows
  and I don't know enough about macOS to bother with it.
- The library archive is quite large at 20MB.
  This is because I haven't yet found a way to split the needed assets
  according to the processor architecture.

## License

    Copyright (C) 2022 jmir1

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
