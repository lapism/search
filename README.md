# SearchView

Persistent SearchView Library like Play Store.  
Check sample project very thoroughly !!!  
Features: Material Design, Toolbar / Menu item version, History, Styling.  
Google Material Design Pattern:  
https://www.google.com/design/spec/patterns/search.html  
Version history here:  
https://bintray.com/lapism/maven/searchview/view  
Material colors in the project:  
https://gist.github.com/lapism/3b417142300d9dbde3b4  
Sample application on:  
<a href="https://play.google.com/store/apps/details?id=com.lapism.searchview.sample">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView/blob/master/images/google-play-badge.png" />
</a>

Because I have done firstly this library as a private project, 
I would like to know if someone of you use my library in your application. 
If yes, please send me a name of your app and link to Play Store and I will promote your app in Readme.

**Apps using this library:**

[Service Notes](https://play.google.com/store/apps/details?id=notes.service.com.servicenotes)  
[Business Card Reader](https://play.google.com/store/apps/details?id=com.iac.bcreader) <br />
[Last Bastion](https://play.google.com/store/apps/details?id=org.super8.lastbastion) <br />

------------------------------------------------------------------------------------------------------------------------------

# Usage
**Add the dependencies to your gradle file:**
```javascript
dependencies {
        compile 'com.lapism:searchview:3.3'
        }
```

![Screenshot 1]
(https://github.com/lapism/SearchView/blob/master/images/image_1.png)![Screenshot2]
(https://github.com/lapism/SearchView/blob/master/images/image_2.png)![Screenshot 3]
(https://github.com/lapism/SearchView/blob/master/images/image_3.png)![Screenshot 4]
(https://github.com/lapism/SearchView/blob/master/images/image_4.png)![Screenshot 5]
(https://github.com/lapism/SearchView/blob/master/images/image_5.png)![Screenshot 6]
(https://github.com/lapism/SearchView/blob/master/images/image_6.png)

**Code:**
```java
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
```

**XML:**
```xml
<com.lapism.searchview.SearchView
    android:id="@+id/searchView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

**Styling SearchView:**
```xml
        <attr name="search_version" format="enum">
            <enum name="toolbar" value="1000" />
            <enum name="toolbar_icon" value="1001" />
            <enum name="menu_item" value="1002" />
        </attr>
        <attr name="search_version_margins" format="enum">
            <enum name="toolbar_small" value="2000" />
            <enum name="toolbar_big" value="2001" />
            <enum name="menu_item" value="2002" />
        </attr>
        <attr name="search_theme" format="enum">
            <enum name="light" value="3000" />
            <enum name="dark" value="3001" />
        </attr>
        <attr name="search_navigation_icon" format="integer" />
        <attr name="search_icon_color" format="color" />
        <attr name="search_background_color" format="color" />
        <attr name="search_text" format="string" />
        <attr name="search_text_color" format="color" />
        <attr name="search_text_highlight_color" format="color" />
        <attr name="search_text_size" format="dimension" />
        <attr name="search_text_style" format="enum">
            <enum name="normal" value="0" />
            <enum name="bold" value="1" />
            <enum name="italic" value="2" />
            <enum name="bold_italic" value="3" />
        </attr>
        <attr name="search_hint" format="string" />
        <attr name="search_hint_color" format="color" />
        <attr name="search_divider" format="boolean" />
        <attr name="search_voice" format="boolean" />
        <attr name="search_voice_text" format="string" />
        <attr name="search_animation_duration" format="integer" />
        <attr name="search_shadow" format="boolean" />
        <attr name="search_shadow_color" format="boolean" />
        <attr name="search_elevation" format="dimension" />
```
