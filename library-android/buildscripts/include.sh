#!/bin/bash -e

v_sdk=9123335_latest
v_ndk=r25b
v_ndk_n=25.1.8937393
v_sdk_build_tools=33.0.1
v_target_sdk=33
v_minimum_sdk=21
v_p4a=2022.9.4

pkg_reqs="build-essential ccache git zlib1g-dev python3 python3-dev \
libncurses5:i386 libstdc++6:i386 zlib1g:i386 openjdk-8-jdk unzip ant \
ccache autoconf libtool libssl-dev libffi-dev"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

SDK_DIR="$DIR/sdk"
ANDROID_HOME="$SDK_DIR/android-sdk-linux"
