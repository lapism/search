# Search
Material Design Search component for Android

 - Last Material Design
 - Persistent search
 - Expandable search
 - History
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
       src="https://github.com/lapism/Search/blob/master/images/donate.png" />
</a>

## Usage
minSdkVersion 21
targetSdkVersion 29

Add the dependency to your gradle file:
```groovy
dependencies {
    implementation 'com.lapism.androidx:searchview:1.0.0'
}
```
# SearchView

## Donations

`Please support me!`

<a href="https://www.paypal.me/lapism">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView-SearchBar/blob/master/images/donate.png" />
</a>

### XML
```xml
<com.lapism.searchview.widget.SearchView
    android:id="@+id/INFO2"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

# XML attributes
```xml


```

### SearchView
```java
SearchView INFO2 = findViewById(R.id.INFO2);
```

**SearchMenuItem**
```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.action_search:
            mSearchView.open();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}
```

### Public methods
| Name | Format | Default | Description
| ------ | ------ |  ------ |------ |
| getLogo() | ... | ... | ...


![Search](https://github.com/lapism/Search/blob/master/images/search.png)


**[README for SearchAdapter](https://github.com/lapism/Search/blob/master/README_SearchAdapter.md)**

### Changelog
**1.0.0-alpha05**
- First release

### Author

* **Martin Lapiš** - [GitHub](https://github.com/lapism)

### License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://github.com/lapism/Search/blob/searchview/LICENSE) file for details.
