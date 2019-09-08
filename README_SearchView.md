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

### XML for CoordinatorLayout
```xml
<com.lapism.searchview.widget.SearchView
    android:id="@+id/INFO2"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/search_behavior"
    <!-- or -->
    app:layout_behavior="com.lapism.searchview.widget.SearchBehavior" />
```

# XML attributes
```xml


```

### Java or Kotlin
```java
SearchView INFO2 = findViewById(R.id.INFO2);
```

**Search.Version.MENU_ITEM:**
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