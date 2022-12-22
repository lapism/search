![API](https://img.shields.io/badge/minSdk-21-brightgreen.svg?style=flat)
![API](https://img.shields.io/badge/targetSdk-33-brightgreen.svg?style=flat)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-blue.svg)](https://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.lapism/search)](https://s01.oss.sonatype.org/content/repositories/releases/io/github/lapism/search)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## DEPRECATED  

Please use:
 - com.google.android.material.search.SearchBar
 - com.google.android.material.search.SearchView


# Search

 - Search component for Android
 - Material You Design
 - Styling
 - Kotlin

![Search](https://github.com/lapism/Search/blob/master/images/search.png)

## Api
 - Java = 1.8
 - Kotlin = 1.8

Add the dependency to your gradle file:

```groovy
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation 'io.github.lapism:search:2.0.1'
}
```

## Usage

```java
        binding.materialSearchView.requestFocus()
        binding.materialSearchView.clearFocus()
```

### MaterialSearchBar

```java
        val toolbar = binding.materialSearchBar.getToolbar()
        setSupportActionBar(toolbar)

        binding.materialSearchBar.apply {
            navigationIconCompat = NavigationIconCompat.SEARCH
            setHint(getString(R.string.search))
            setOnClickListener {
                binding.materialSearchView.requestFocus()
            }
            setNavigationOnClickListener {
                binding.materialSearchView.requestFocus()
            }
        }
```

### MaterialSearchView

```java
        binding.materialSearchView.apply {
            addView(recyclerView)
            navigationIconCompat = NavigationIconCompat.ARROW
            setNavigationOnClickListener {
                binding.materialSearchView.clearFocus()
            }
            setHint(getString(R.string.search))
            setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: CharSequence) {
                    adapter.filter(newText)
                }

                override fun onQueryTextSubmit(query: CharSequence) {

                }
            })
            setOnFocusChangeListener(object : MaterialSearchView.OnFocusChangeListener {
                override fun onFocusChange(hasFocus: Boolean) {

                }
            })
        }
```

### Layout

You have to use app theme Theme.Material3.* or Theme.MaterialComponents.*.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="false"
    tools:context=".activity.MainActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <!-- Simple MaterialToolbar extension -->
        <com.lapism.search.widget.MaterialSearchBar
            android:id="@+id/material_search_bar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_scrollFlags="scroll|enterAlways|snap" />

    </com.google.android.material.appbar.AppBarLayout>

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"

        app:layout_behavior="@string/material_search_bar_scrolling_view_behavior"

        app:navGraph="@navigation/mobile_navigation" />

    <com.lapism.search.widget.MaterialSearchView
        android:id="@+id/material_search_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_anchor="@id/material_search_bar" />

    <BottomNavigationView
        android:id="@+id/bottom_navigation_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:orientation="vertical"
        app:layout_insetEdge="bottom"
        app:menu="@menu/menu_bottom_nav" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
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
    <attr name="search_navigationBackgroundColor" format="reference" />
    <attr name="search_navigationElevation" format="dimension" />
    <attr name="search_radius" format="dimension" />
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
    <attr name="search_navigationBackgroundColor" />
    <attr name="search_navigationElevation" />
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
