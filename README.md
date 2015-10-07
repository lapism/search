# SearchView

<<<<<<< HEAD
=======
Version: 1.1.2 

>>>>>>> origin/master
Implementation of Persistent SearchView in Google Material Design. 
https://www.google.com/design/spec/patterns/search.html. 
Library based on Krishnakapil original version. 
Big thank you !

# Usage
**Add the dependencies to your gradle file:**
```javascript
	dependencies {
    		compile 'com.lapism:searchview:1.1.3'
	}
```

<a href="https://play.google.com/store/apps/details?id=com.ocman.searchviewapp">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView/blob/master/images/google_play.png" />
</a>


![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_1.png)    ![Screenshot 2]
(https://github.com/lapism/SearchView/blob/master/images/image_2.png)    ![Screenshot 3]
(https://github.com/lapism/SearchView/blob/master/images/image_3.png)    ![Screenshot 4]
(https://github.com/lapism/SearchView/blob/master/images/image_4.png)    

**Add SearchView to your layout file:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?colorPrimary"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />

    <View
        android:layout_width="match_parent"
        android:layout_height="5dp"
        android:background="@drawable/shadow"
        android:clickable="false"
        android:layout_below="@+id/toolbar"
        android:layout_toRightOf="@+id/search_view"
        android:layout_toEndOf="@+id/search_view" />

    <com.lapism.searchview.SearchView
        android:id="@+id/search_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="visible"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true"
        android:layout_alignParentTop="true" />

</RelativeLayout>
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
