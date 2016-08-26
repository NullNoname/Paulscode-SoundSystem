# PaulsCode 3D Sound System
This repository is an unofficial mirror of [Paul Lamb's 3D Sound System](http://www.paulscode.com/forum/index.php?topic=4.0).
Most optional components except JOAL and jPCT-related codes are included.

## External Dependencies
### LWJGL
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

### J-Ogg
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

* LWJGL related codes (ChannelLWJGLOpenAL, LibraryLWJGLOpenAL, and SourceLWJGLOpenAL) are licensed under SoundSystem LibraryLWJGLOpenAL License. Please see "SoundSystem LibraryLWJGLOpenAL License.txt" and "LWJGL License.txt" for more information.

* J-Ogg codec (CodecJOgg) is licensed under SoundSystem CodecJOgg License. Please see "SoundSystem CodecJOgg License.txt" and "J-Ogg License.txt" for more information.

* JSpeex codec (CodecJSpeex) is licensed under SoundSystem CodecJSpeex License. Please see "SoundSystem CodecJSpeex License.txt" and "JSpeex License.txt" for more information.
