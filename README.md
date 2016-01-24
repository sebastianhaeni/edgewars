# Edge Wars [![Build Status](https://magnum.travis-ci.com/sebastianhaeni/edgewars.svg?token=qgj44e18REfhtFnW25Z2&branch=master)](https://magnum.travis-ci.com/sebastianhaeni/edgewars)

An Android strategy game made by 3 students at Bern University of Applied Sciences.

Authors:
* Steven Cardini
* Sebastian Häni
* Raphael Laubscher

## Building & Starting

1. Install [Android Studio](http://developer.android.com/sdk) including the SDK
2. Fetch yourself a copy of the source (`git clone https://github.com/sebastianhaeni/edgewars.git`)
3. Start Android Studio and select "Import project from Eclipse, Gradle, ..." and point `edgewars/build.gradle`
4. Wait until the import is finished. It will take a while because it needs to download all dependencies.
5. Start the game with "Run \`app\`" and select an emulator or a connected device

### Using a physical device

To use a physical device to test, you need at least Android 5.1 on your phone.

Then you also need to enable the developer options. Use Google to find out how to activate that for
your own device. 

Then enable "USB Debugging" and connect it to your computer. The computer may not have the device
specific drivers. You need to google that too as it's not the same process for every device.

When the phone is correctly connected, it should show up in Android Studio when you try to run
the app.

## Contributing

### Branching

For every task a developer creates a branch with a dash-delimited name. Once the task is finished the branch is merged into master and deleted.

### Code style

* Class member variables begin with `m`. Example: `mNodes`
* Android Studio default settings are the standard we use.
* All gradle lint warnings have to be resolved before a pull request
