![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.5.31-blue.svg)](https://kotlinlang.org)
[![Download](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fio%2Fgithub%2Flapism%2Fsearch%2Fmaven-metadata.xml) ](https://repo1.maven.org/maven2/io/github/lapism/search/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Search
 - Search component for Android
 - Material You Design
 - Styling
 - Kotlin

![Search](https://github.com/lapism/Search/blob/master/images/search.png)

## Apps with this library

* [LapIcons](https://play.google.com/store/apps/details?id=com.lapism.lapicons)

## Api
 - minSdkVersion = 26
 - targetSdkVersion = 31
 - Java = 1.8
 - Kotlin = 1.8

Add the dependency to your gradle file:
```groovy
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        implementation 'io.github.lapism:search:1.1.0'
    }
```

## Usage
```java
   binding.materialSearchView.requestFocus()
   binding.materialSearchView.clearFocus()
```

### MaterialSearchBar
```java

```

### MaterialSearchView
```java

```

### Layout<!-- Simple MaterialToolbar extension -->
You have to use app theme Theme.Material3.* or Theme.MaterialComponents.*.

```xml

```

### XML attributes
```xml
    <declare-styleable name="MaterialSearchBar">
        <attr name="search_navigationIconCompat" format="enum">
            <enum name="none" value="0" />
            <enum name="arrow" value="1" />
            <enum name="search" value="2" />
        </attr>
        <attr name="search_navigationIcon" format="reference" />
        <attr name="search_navigationContentDescription" format="reference" />
        <attr name="search_backgroundColor" format="reference" />
        <attr name="search_radius" format="integer" />

        <attr name="android:elevation" />
        <attr name="android:hint" />
        <attr name="android:layout_marginStart" />
        <attr name="android:layout_marginEnd" />
        <attr name="android:layout_marginTop" />
        <attr name="android:layout_marginBottom" />
    </declare-styleable>

    <declare-styleable name="MaterialSearchView">
        <attr name="search_navigationIconCompat" />
        <attr name="search_navigationIcon" />
        <attr name="search_navigationContentDescription" />
        <attr name="search_backgroundColor" />
        <attr name="search_clearIcon" format="reference" />
        <attr name="search_dividerColor" format="reference" />
        <attr name="search_scrimColor" format="reference" />

        <attr name="android:hint" />
        <attr name="android:imeOptions" />
        <attr name="android:inputType" />
    </declare-styleable>
```

## Todo
**Animation**
- animation like Google, needs help :)

## Author

* **Martin Lapiš** - [GitHub](https://github.com/lapism)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://github.com/lapism/Search/blob/searchview/LICENSE) file for details.
