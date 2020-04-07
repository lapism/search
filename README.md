[ ![Download](https://api.bintray.com/packages/lapism/maven/search/images/download.svg?version=2.0.0) ](https://bintray.com/lapism/maven/search/2.0.0/link)

# Search
Material Design Search component for Android

 - Last Material Design
 - Persistent search
 - Styling
 - Kotlin

Material Design pattern:  
https://material.io/design/navigation/search.html  

Versions history:  
https://bintray.com/lapism/maven/search

![Search](https://github.com/lapism/Search/blob/master/images/search.png)

## Donations

`Please support me!`

<a href="https://www.paypal.me/lapism">
  <img alt="Paypal"
       src="https://github.com/lapism/search/blob/master/images/paypal.png" />
</a>

## Usage
minSdkVersion 21  
targetSdkVersion 29  

Add the dependency to your gradle file:
```groovy
dependencies {
    implementation 'com.lapism.search:search:2.0.0'
}
```

## SearchView
```java
val materialSearchView = findViewById<MaterialSearchView>(R.id.materialSearchView)
```

### XML
```xml
        <com.lapism.search.widget.MaterialSearchView
            android:id="@+id/searchView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
```

### XML attributes
```xml
        <attr name="search_navigation_icon_support" format="enum">
            <enum name="none" value="1000" />
            <enum name="menu" value="1001" />
            <enum name="arrow" value="1002" />
            <enum name="search" value="1003" />
        </attr>
```

## Changelog
**2.0.0**
- NOT COMPATIBLE WITH 1.0 !!!
- SearchMenu item removed
- SearchView renamed to MaterialSearchView
- changed NavigationIconSupport properties
- NavigationIconSupport properties moved to SearchLayout
- fixed bugs
- improved open and hide animation
- new public methods

**1.0.0**
- first release

## Author

* **Martin Lapiš** - [GitHub](https://github.com/lapism)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://github.com/lapism/Search/blob/searchview/LICENSE) file for details.
