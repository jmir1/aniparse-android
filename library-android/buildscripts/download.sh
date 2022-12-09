#!/bin/bash -e

. ./include.sh

[ -z "$WGET" ] && WGET=wget # possibility of calling wget differently

sudo dpkg --add-architecture i386
sudo apt-get update
echo y | sudo apt-get install $pkg_reqs
pip3 install "python-for-android==${v_p4a}"
pip3 install Cython

if ! javac -version &>/dev/null; then
  echo "Error: missing Java Development Kit."
  echo "Install it using e.g. sudo apt-get install default-jre-headless"
  exit 255
fi

os_ndk="linux"

mkdir -p sdk && cd sdk

# Android SDK
if [ ! -d "android-sdk-linux" ]; then
	$WGET "https://dl.google.com/android/repository/commandlinetools-linux-${v_sdk}.zip"
	mkdir -p "android-sdk-linux/cmdline-tools/latest"
	unzip -q -d "android-sdk-linux/cmdline-tools" "commandlinetools-linux-${v_sdk}.zip"
	rm "commandlinetools-linux-${v_sdk}.zip"
	shopt -s dotglob
	mv android-sdk-linux/cmdline-tools/cmdline-tools/* android-sdk-linux/cmdline-tools/latest/
	shopt -u dotglob
	rm -d android-sdk-linux/cmdline-tools/cmdline-tools
fi
sdkmanager () {
	local exe="./android-sdk-linux/cmdline-tools/latest/bin/sdkmanager"
	[ -x "$exe" ] || exe="./android-sdk-linux/cmdline-tools/bin/sdkmanager"
	"$exe" --sdk_root="${ANDROID_HOME}" "$@"
}
echo y | sdkmanager \
	"platforms;android-${v_target_sdk}" "build-tools;${v_sdk_build_tools}" \
	"ndk;${v_ndk_n}" \
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
