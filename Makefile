APK = gemara/android/src-gen/generated/app/build/outputs/apk/app-debug.apk

all: debug install

test: build-test debug install

build-test:
	mvn test

debug:
	cd gemara/android/src-gen/generated && chmod 777 gradlew && ./gradlew clean assembleDebug

install:
	adb $(TARGET) install -r $(APK)