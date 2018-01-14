# lib-název

Popisek.
 - funkce `funkce`

## Použití
```groovy
dependencies {
     compile 'cz.seznam:lib-nazev:x.x.x'
}
```
## Specifikace
  - min API 16
 
### Závislosti
 - "com.google.android.exoplayer:exoplayer-core"
 
## Inicializace, Funkce a volání knihovny, Popis
Služba ...

### Volání pomocí XML
```xml
    <cz.seznam.widget.customview
        android:id="@+id/customView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
### Volání pomocí Java kódu
```java
Java kód
```





**SearchView.Version.MENU_ITEM and SearchView.Version.TOOLBAR:**
```java


```

**SearchView.Version.MENU_ITEM:**
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

**XML:**
```xml

```

**Styling**
```xml

```

