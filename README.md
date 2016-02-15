# SearchView

**Note: Version 2.0 is not compatible with 1.5 !**
**Also check CheckableImageView in version 2.0 of the library.**

Persistent SearchView Library with history in Material Design. 
It supports layout like section My apps in Google Play Store.
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
        compile 'com.lapism:searchview:1.5' (2.0 is not uploaded yet) 
        }
```

![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_1.png)
![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_2.png)
![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_3.png)
![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_4.png)
![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_5.png)
![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_6.png)

**In code (Check the Sample project!):**

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                mSearchView.show(true/false); // animate, ONLY FOR MENU ITEM
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
        
Examples of layouts in the Sample project!     
```

**Styling SearchView:**
```
app:search_version = "toolbar / menu_item"
app:search_style = "classic / color"
app:search_theme = "light / dark"
app:search_divider = "true / false"
app:search_hint = "Hint text"
app:search_hint_size = "16sp"
app:search_voice = "true / false"
app:search_voice_text = "Voice text"
app:search_animation_duration = "360"
```

