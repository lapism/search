[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)

# Search
 - Search component for Android
 - Material You Design
 - Styling
 - Kotlin

![Search](https://github.com/lapism/Search/blob/master_v1/images/search.png)

## Apps with this library

* [LapIcons](https://play.google.com/store/apps/details?id=com.lapism.lapicons)

## Api
 - minSdkVersion = 26
 - targetSdkVersion = 30
 - Java = 1.8
 - Kotlin = 1.8

Add the dependency to your gradle file:
```groovy
ASAP
```

## Usage
    private fun clearFocus() {
        binding.materialSearchView.clearFocus()
        binding.materialSearchView.visibility = View.GONE
    }

    private fun requestFocus() {
        binding.materialSearchView.visibility = View.VISIBLE
        binding.materialSearchView.requestFocus()
    }

### MaterialSearchBar
```java
        val toolbar = binding.materialSearchBar.getToolbar()
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setIcon(R.drawable.new_ic_outline_search_24)

        binding.materialSearchBar.apply {
            setHint(getString(R.string.search))
            setOnClickListener {
                requestFocus()
            }
            setNavigationOnClickListener {
                requestFocus()
            }
        }
```

### MaterialSearchView
```java
        binding.materialSearchView.apply {
            visibility = View.GONE
            addView(recyclerView)
            setNavigationIcon(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.search_ic_outline_arrow_back_24
                )
            )
            setNavigationOnClickListener {
                clearFocus()
            }
            setHint(getString(R.string.search))
            setBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.color_surface
                )
            )
            setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: CharSequence): Boolean {
                    adapter.filter(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: CharSequence): Boolean {
                    return true
                }
            })
            setOnFocusChangeListener(object : MaterialSearchView.OnFocusChangeListener {
                override fun onFocusChange(hasFocus: Boolean) {

                }
            })
        }
```

### Layout
Also add android:stateListAnimator="@null" to the AppBarLayout.

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

        <com.lapism.search.widget.MaterialSearchBar
            android:id="@+id/material_search_bar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="@dimen/dp_0"
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
        <attr name="search_backgroundColor" />
        <attr name="search_clearIcon" format="reference" />
        <attr name="search_dividerColor" format="reference" />
        <attr name="search_scrimColor" format="reference" />

        <attr name="android:hint" />
        <attr name="android:imeOptions" />
        <attr name="android:inputType" />
    </declare-styleable>
```

## Changelog
**1.0.0**
- Initial release

## Author

* **Martin Lapiš** - [GitHub](https://github.com/lapism)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://github.com/lapism/Search/blob/searchview/LICENSE) file for details.
