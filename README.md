# PaulsCode 3D Sound System Unoffical Android Branch
This repository is an unofficial Android port of [Paul Lamb's 3D Sound System](http://www.paulscode.com/forum/index.php?topic=4.0).
Most optional components except JOAL and jPCT-related codes are included.

This port still works in PC, and this repository contain nothing for Android. To actually being able to play audio in Android, you will need to use [LibraryAudioTrack](https://github.com/NullNoname/paudiotrack).

This port added "PAudioFormat" class which is a clone of JavaSound's AudioFormat. AudioFormat is missing from Android but was used by several places in 3D Sound System. This port also added "AudioFormatConverter" class which converts between our PAudioFormat and the real AudioFormat.

## Codec Compatibility
* CodecJOrbis: Works
* CodecJSpeex: Works
* CodecIBXM: Only generates garbage audio. I don't know why.
* CodecWav: Not ported to Android because it has heavy usage of JavaSound API.
* CodecJOgg: Same as CodecWav. Need JavaSound to work.

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

### J-Ogg (PC only)
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

* PAudioFormat is a modified class of AudioFormat from OpenJDK 6-b14. It also contains the "NOT_SPECIFIED" constant from AudioSystem from the same OpenJDK. PAudioFormat is licensed under [GNU General Public License, version 2, with the Classpath Exception](http://openjdk.java.net/legal/gplv2+ce.html).

* AudioFormatConverter class is licensed under Unlicense. Please see "AudioFormatConverter License.txt" for more information.

* LWJGL related codes (ChannelLWJGLOpenAL, LibraryLWJGLOpenAL, and SourceLWJGLOpenAL) are licensed under SoundSystem LibraryLWJGLOpenAL License. Please see "SoundSystem LibraryLWJGLOpenAL License.txt" and "LWJGL License.txt" for more information.

* J-Ogg codec (CodecJOgg) is licensed under SoundSystem CodecJOgg License. Please see "SoundSystem CodecJOgg License.txt" and "J-Ogg License.txt" for more information.

* JSpeex codec (CodecJSpeex) is licensed under SoundSystem CodecJSpeex License. Please see "SoundSystem CodecJSpeex License.txt" and "JSpeex License.txt" for more information.
