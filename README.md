# SearchView

Persistent SearchView Library in Material Design. 
Now it supports layout like section My apps in Google Play Store.
https://www.google.com/design/spec/patterns/search.html. 

Material colors in the project:
https://gist.github.com/lapism/3b417142300d9dbde3b4

<a href="https://play.google.com/store/apps/details?id=com.lapism.searchview.sample">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView/blob/master/images/google_play.png" />
</a>

# Usage
**Add the dependencies to your gradle file:**
```javascript
dependencies {
        compile 'com.lapism:searchview:1.3.0'
        }
```

![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_1.png)    ![Screenshot 2]
(https://github.com/lapism/SearchView/blob/master/images/image_2.png)    ![Screenshot 3]
(https://github.com/lapism/SearchView/blob/master/images/image_3.png)    ![Screenshot 4]
(https://github.com/lapism/SearchView/blob/master/images/image_4.png)    

**In code:**

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                mSearchView.showSearch();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

**In xml with layout like Google Play:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context=".SearchActivity">

    <android.support.design.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.design.widget.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <include layout="@layout/toolbar" />

            <!-- TabLayout here instead of View -->
            <View
                android:background="?attr/colorPrimary"
                android:layout_width="match_parent"
                android:layout_height="48dp" />

        </android.support.design.widget.AppBarLayout>

        <android.support.v4.widget.NestedScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_behavior="@string/appbar_scrolling_view_behavior">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:contentDescription="@string/cont_desc"
                android:src="@drawable/logo" />

        </android.support.v4.widget.NestedScrollView>

    </android.support.design.widget.CoordinatorLayout>

    <com.lapism.searchview.SearchView
        android:id="@+id/search_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <!-- NavigationView here -->

</android.support.v4.widget.DrawerLayout>
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

