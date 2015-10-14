# SearchView

Implementation of Persistent SearchView in Google Material Design. 
https://www.google.com/design/spec/patterns/search.html. 
Content inside .res folder from library is invisible for using !!!

<a href="https://play.google.com/store/apps/details?id=com.lapism.searchview.sample">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView/blob/master/images/google_play.png" />
</a>

# Usage
**Add the dependencies to your gradle file:**
```javascript
	dependencies {
    		compile 'com.lapism:searchview:1.2'
	}
```

![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_1.png)    ![Screenshot 2]
(https://github.com/lapism/SearchView/blob/master/images/image_2.png)    ![Screenshot 3]
(https://github.com/lapism/SearchView/blob/master/images/image_3.png)    ![Screenshot 4]
(https://github.com/lapism/SearchView/blob/master/images/image_4.png)    

**Add SearchView to your layout file:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

        <include layout="@layout/toolbar_shadow" />

        <com.lapism.searchview.SearchView
            android:id="@+id/search_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />

</FrameLayout>
```

**Styling SearchView:**
```
app:search_style="color"
app:search_style="classic"

app:search_theme="dark"
app:search_theme="light"

app:search_divider="true"
app:search_divider="false"
```

Library is based on Krishnakapil original version. Big thank you !

