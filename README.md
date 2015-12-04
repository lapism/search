# SearchView

Persistent SearchView Library in Material Design. 
Now it supports layout like section My apps in Google Play Store.
https://www.google.com/design/spec/patterns/search.html. 

Material colors in the project:
https://gist.github.com/lapism/3b417142300d9dbde3b4

Sample application on:
<a href="https://play.google.com/store/apps/details?id=com.lapism.searchview.sample">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView/blob/master/images/google_play.png" />
</a>

# Usage
**Add the dependencies to your gradle file:**
```javascript
dependencies {
        compile 'com.lapism:searchview:1.3.7'
        }
```

![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_1.png)    ![Screenshot 2]
(https://github.com/lapism/SearchView/blob/master/images/image_2.png)    ![Screenshot 3]
(https://github.com/lapism/SearchView/blob/master/images/image_3.png)    ![Screenshot 4]
(https://github.com/lapism/SearchView/blob/master/images/image_4.png) 
![Screenshot 5]
(./images/image_5.png) 

**In code (Check the Sample project!):**

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

**In xml (Check the Sample project!):**
```xml
<com.lapism.searchview.SearchView
    android:id="@+id/search_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
        
You can find examples of layouts in the Sample project !!!     
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

