# PaulsCode 3D Sound System Unoffical Android Branch
This repository is an unofficial Android port of [Paul Lamb's 3D Sound System](http://www.paulscode.com/forum/index.php?topic=4.0).
Most optional components except JOAL and jPCT-related codes are included.

This port still works in PC, and this repository contain nothing for Android. To actually being able to play audio in Android, you will need to use [LibraryAudioTrack](https://github.com/NullNoname/paudiotrack).

## Changes from the original
* This port added "PAudioFormat" class which is a clone of JavaSound's AudioFormat. AudioFormat is missing from Android but was used by several places in 3D Sound System. This port also added "AudioFormatConverter" class which converts between our PAudioFormat and the real AudioFormat.
* FileInputProvider and DefaultFileInputProvider classes are added. They can be implemented or extended to feed your own InputStream to the system. These are useful for loading files from Android assets. Use SoundSystemConfig.setFileInputProvider(FileInputProvider) to set your own implementation.
* Added sample rate setting (setDefaultSampleRate) to CodecIBXM.

## Android Codec Compatibility
* CodecJOrbis: Works
* CodecJSpeex: Works
* CodecIBXM: Works
* CodecWav: Not ported to Android because it has heavy usage of JavaSound API.
* CodecJOgg: Same as CodecWav. Need JavaSound to work.
* MIDI: Don't even try. I'm sure it will crash and burn.
 
## External Dependencies
### LWJGL (PC only)
Required for playing sound through LibraryLWJGLOpenAL.
```
<dependency>
    <groupId>org.lwjgl.lwjgl</groupId>
    <artifactId>lwjgl</artifactId>
    <version>2.9.1</version>
</dependency>
<dependency>
    <groupId>org.lwjgl.lwjgl</groupId>
    <artifactId>lwjgl_util</artifactId>
    <version>2.9.1</version>
</dependency>
```

### J-Ogg (PC only for now)
Required for loading OGG audio through CodecJOgg. It is faster than JOrbis, but not compatible with several OGG files. Sometimes processing an existing OGG file though a converter make it compatible.
```
<dependency>
    <groupId>de.jarnbjo</groupId>
    <artifactId>j-ogg-all</artifactId>
    <version>1.0.0</version>
</dependency>
```

### JOrbis
Required for loading OGG audio through CodecJOrbis. It is more compatible than J-Ogg, but slower.
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- https://github.com/NullNoname/jorbis -->
    <dependency>
        <groupId>com.github.NullNoname</groupId>
        <artifactId>jorbis</artifactId>
        <version>0.0.17</version>
    </dependency>
</dependencies>
```

### IBXM
Required for loading Protracker MOD and XM files though CodecIBXM. Newer releases of IBXM are not compatible because of API changes and feature removals.
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- https://github.com/NullNoname/micromod -->
    <dependency>
        <groupId>com.github.NullNoname</groupId>
        <artifactId>micromod</artifactId>
        <version>a51</version>
    </dependency>
</dependencies>
```

### JSpeex
Required for loading Speex audio though CodecJSpeex.
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- https://github.com/SourceUtils/jspeex -->
    <dependency>
        <groupId>com.github.SourceUtils</groupId>
        <artifactId>jspeex</artifactId>
        <version>b7f6f864f0</version>
    </dependency>
</dependencies>
```

## License
* Unless otherwise noted, most codes and documents of this library are licensed under The SoundSystem License. Please see "SoundSystem License.txt" for more information.

* PAudioFormat is a modified class of AudioFormat from Apache Harmony. It also contains the "NOT_SPECIFIED" constant from AudioSystem from the same codebase. PAudioFormat is licensed under Apache License Version 2.0. Please see "PAudioFormat License.txt" for more information.

* AudioFormatConverter, FileInputProvider, and DefaultFileInputProvider classes are licensed under Unlicense. Please see "AudioFormatConverter License.txt" for more information.

* LWJGL related codes (ChannelLWJGLOpenAL, LibraryLWJGLOpenAL, and SourceLWJGLOpenAL) are licensed under SoundSystem LibraryLWJGLOpenAL License. Please see "SoundSystem LibraryLWJGLOpenAL License.txt" and "LWJGL License.txt" for more information.

* J-Ogg codec (CodecJOgg) is licensed under SoundSystem CodecJOgg License. Please see "SoundSystem CodecJOgg License.txt" and "J-Ogg License.txt" for more information.

* JSpeex codec (CodecJSpeex) is licensed under SoundSystem CodecJSpeex License. Please see "SoundSystem CodecJSpeex License.txt" and "JSpeex License.txt" for more information.
