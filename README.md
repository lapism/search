# Search
Material Design Search component for Android

 - Last Material Design
 - Persistent search
 - Expandable search
 - Styling
 - AndroidX
 - Kotlin

Material Design pattern:  
https://material.io/design/navigation/search.html  

Versions history:  
https://bintray.com/lapism/search/search

## Donations

`Please support me!`

<a href="https://www.paypal.me/lapism">
  <img alt="Paypal"
       src="https://github.com/lapism/search/blob/master/images/search.png" />
</a>

## Usage
minSdkVersion 21  
targetSdkVersion 29  

Add the dependency to your gradle file:
```groovy
dependencies {
    implementation 'com.lapism.androidx:searchview:1.0.0-alpha05'
}
```
# SearchView

## Donations

`Please support me!`

<a href="https://www.paypal.me/lapism">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/search/blob/master/images/paypal.png" />
</a>

### XML
```xml
        <com.lapism.search.widget.SearchView
            android:id="@+id/searchView"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
```

### XML
```xml
        <com.lapism.search.widget.SearchMenuItem
            android:id="@+id/searchMenuItem"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
```

# XML attributes
```xml
        <attr name="search_navigation_icon_support" format="enum">
            <enum name="hamburger" value="100" />
            <enum name="arrow" value="101" />
            <enum name="animation" value="102" />
        </attr>
```

## SearchView
```java
val searchView = findViewById<SearchView>(R.id.searchView)
```

**SearchMenuItem**
```java
val searchMenuItem = findViewById<SearchMenuItem>(R.id.searchMenuItem)
```

### Changelog
**1.0.0-alpha05**
- First release

### Author

* **Martin Lapiš** - [GitHub](https://github.com/lapism)

### License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://github.com/lapism/Search/blob/searchview/LICENSE) file for details.
