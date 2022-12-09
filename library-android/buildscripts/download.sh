#!/bin/bash -e

v_sdk=8512546_latest
v_ndk=r25b
v_ndk_n=25.1.8937393
v_sdk_build_tools=30.0.3
v_target_sdk=33
v_minimum_sdk=21

pkg_reqs="build-essential ccache git zlib1g-dev python3 python3-dev \
libncurses5:i386 libstdc++6:i386 zlib1g:i386 openjdk-8-jdk unzip ant \
ccache autoconf libtool libssl-dev libffi-dev"

[ -z "$WGET" ] && WGET=wget # possibility of calling wget differently

sudo dpkg --add-architecture i386
sudo apt update
sudo apt install $pkg_reqs
sudo pip3 install git+https://github.com/kivy/python-for-android.git

if ! javac -version &>/dev/null; then
  echo "Error: missing Java Development Kit."
  echo "Install it using e.g. sudo apt install default-jre-headless"
  exit 255
fi

os_ndk="linux"

mkdir -p sdk && cd sdk

# Android SDK
if [ ! -d "android-sdk-linux" ]; then
	$WGET "https://dl.google.com/android/repository/commandlinetools-linux-${v_sdk}.zip"
	mkdir "android-sdk-linux"
	unzip -q -d "android-sdk-linux" "commandlinetools-linux-${v_sdk}.zip"
	rm "commandlinetools-linux-${v_sdk}.zip"
fi
sdkmanager () {
	local exe="./android-sdk-linux/cmdline-tools/latest/bin/sdkmanager"
	[ -x "$exe" ] || exe="./android-sdk-linux/cmdline-tools/bin/sdkmanager"
	"$exe" --sdk_root="${ANDROID_HOME}" "$@"
}
echo y | sdkmanager \
	"platforms;android-32" "build-tools;${v_sdk_build_tools}" \
	"extras;android;m2repository"

# Android NDK (either standalone or installed by SDK)
if [ -d "android-ndk-${v_ndk}" ]; then
	:
elif [ -d "android-sdk-linux/ndk/${v_ndk_n}" ]; then
	ln -s "android-sdk-linux/ndk/${v_ndk_n}" "android-ndk-${v_ndk}"
elif [ -z "${os_ndk}" ]; then
	echo y | sdkmanager "ndk;${v_ndk_n}"
	ln -s "android-sdk-linux/ndk/${v_ndk_n}" "android-ndk-${v_ndk}"
else
	$WGET "http://dl.google.com/android/repository/android-ndk-${v_ndk}-${os_ndk}.zip"
	unzip -q "android-ndk-${v_ndk}-${os_ndk}.zip"
	rm "android-ndk-${v_ndk}-${os_ndk}.zip"
fi
if ! grep -qF "${v_ndk_n}" "android-ndk-${v_ndk}/source.properties"; then
	echo "Error: NDK exists but is not the correct version (expecting ${v_ndk_n})"
	exit 255
fi

export ANDROIDSDK="./android-sdk-linux"
export ANDROIDNDK="$ANDROIDSDK/ndk/${v_ndk_n}"
export ANDROIDAPI="$v_target_sdk"
export NDKAPI="$v_minimum_sdk"
