# SearchView

Persistent SearchView Library from Play Store.  
Features: Material Design, Toolbar/MenuItem version, History, Styling.

Google Material Design Pattern:  
https://material.io/guidelines/patterns/search.html

Version History:  
https://bintray.com/lapism/maven/searchview/view

Material colors in the project:  
https://gist.github.com/lapism/3b417142300d9dbde3b4

# Donations:
Please support me!

<a href="https://www.paypal.me/lapism">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView/blob/master/images/donate.png" />
</a>
  
------------------------------------------------------------------------------------------------------------------------------

**Apps using this library:**  
Because I have done firstly this library as a private project, I would like to know if someone of you use my library in your application. If yes, please send me the name of your app and the link to Play Store and I will promote your app in ReadMe.

[Service Notes](https://play.google.com/store/apps/details?id=notes.service.com.servicenotes)  
[Business Card Reader](https://play.google.com/store/apps/details?id=com.iac.bcreader)  
[Toiletto](https://play.google.com/store/apps/details?id=org.super8.lastbastion)  
[Zikobot](https://play.google.com/store/apps/details?id=com.startogamu.zikobot)  
[AllWeather](https://play.google.com/store/apps/details?id=com.dev.nicola.allweather)  
[Karaoke Online Sing & Record](https://play.google.com/store/apps/details?id=com.anhlt.karaokeonline)  
[Playbuzz](https://play.google.com/store/apps/details?id=com.playbuzz.android.app)  
[MovieTracker](https://play.google.com/store/apps/details?id=jacobs.yen.movietracker)  
[MaxTorz](https://play.google.com/store/apps/details?id=com.maxxsol.maxtorz)  
[RealTime Subscriber YouTube](https://play.google.com/store/apps/details?id=vulcanweblabs.realtimeyoutube)  
[Lucidity - Lucid Dream Journal](https://play.google.com/store/apps/details?id=ch.b3nz.lucidity)  
[Guide COC](https://play.google.com/store/apps/details?id=com.superguide.coc)

------------------------------------------------------------------------------------------------------------------------------

# Usage
**Add the dependencies to your gradle file:**
```javascript
dependencies {
    implementation 'com.lapism:searchview:5.0.0-alpha7'
    
    implementation 'com.android.support:cardview-v7:26.1.0'
    implementation 'com.google.android:flexbox:0.3.1'
}
```
![Screenshot 1](https://github.com/lapism/SearchView/blob/master/images/image_1.png)
![Screenshot 2](https://github.com/lapism/SearchView/blob/master/images/image_2.png)

**Versions:**  
  
SearchView identifies its layout style through versions. Currently, there are two values, namely `SearchView.Version.TOOLBAR` for the persistent view, and `SearchView.Version.MENU_ITEM` for the view that appears on a MenuItem press. The version can be defined through `setVersion`.

**SearchView.Version.MENU_ITEM and SearchView.Version.TOOLBAR:**
```java
SearchHistoryTable mHistoryDatabase = new SearchHistoryTable(this);

SearchView mSearchView = (SearchView) findViewById(R.id.searchView); // to API 25
SearchView mSearchView = findViewById(R.id.searchView); // from API 26
if (mSearchView != null) {
    mSearchView.setVersionMargins(SearchView.VersionMargins.TOOLBAR_SMALL);
    mSearchView.setHint(R.string.search);
    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mHistoryDatabase.addItem(new SearchItem(query));
            mSearchView.close(false);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    });
    mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
        @Override
        public boolean onOpen() {
            if (mFab != null) {
                mFab.hide();
            }
            return true;
        }

        @Override
        public boolean onClose() {
            if (mFab != null && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mFab.show();
            }
            return true;
        }
    });
    mSearchView.setVoiceText("Set permission on Android 6.0+ !");
    searchView.setOnVoiceIconClickListener(new SearchView.OnVoiceIconClickListener() {
        @Override
        public void onVoiceIconClick() {
            // permission
        }
    });

    List<SearchItem> suggestionsList = new ArrayList<>();
    suggestionsList.add(new SearchItem("search1"));
    suggestionsList.add(new SearchItem("search2"));
    suggestionsList.add(new SearchItem("search3"));

    SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
    searchAdapter.setOnSearchItemClickListener(new SearchAdapter.OnSearchItemClickListener() {
        @Override
        public void onSearchItemClick(View view, int position, String text) {
            mHistoryDatabase.addItem(new SearchItem(text));
            mSearchView.close(false);
        }
    });
    mSearchView.setAdapter(searchAdapter);

    suggestionsList.add(new SearchItem("search1"));
    suggestionsList.add(new SearchItem("search2"));
    suggestionsList.add(new SearchItem("search3"));
    searchAdapter.notifyDataSetChanged();

    List<SearchFilter> filter = new ArrayList<>();
    filter.add(new SearchFilter("Filter1", true));
    filter.add(new SearchFilter("Filter2", true));
    mSearchView.setFilters(filter);
    //use mSearchView.getFiltersStates() to consider filter when performing search
}
```

**SearchView.Version.MENU_ITEM:**
```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.action_search: {
            mSearchView.open(true/false); // enable or disable animation
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
    
    // will be new
```

**XML with CoordinatorLayout:** 
 ```xml
 <com.lapism.searchview.SearchView
     android:id="@+id/searchView"
     android:layout_width="match_parent"
     android:layout_height="match_parent" 
     app:layout_behavior="com.lapism.searchview.SearchBehavior" />
     
    // will be new
 ```

**Styling SearchView:**
```xml
<attr name="search_version" format="enum">
    <enum name="toolbar" value="1000" />
    <enum name="menu_item" value="1001" />
</attr>
<attr name="search_version_margins" format="enum">
    <enum name="toolbar_small" value="2000" />
    <enum name="toolbar_big" value="2001" />
    <enum name="menu_item" value="2002" />
</attr>
<attr name="search_theme" format="enum">
    <enum name="light" value="3000" />
    <enum name="dark" value="3001" />
    <enum name="play_store" value="3002" />
</attr>
<attr name="search_height" format="dimension" />
<attr name="search_navigation_icon" format="integer" />
<attr name="search_icon_color" format="color" />
<attr name="search_background_color" format="color" />
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
<attr name="search_voice" format="boolean" />
<attr name="search_voice_text" format="string" />
<attr name="search_animation_duration" format="integer" />
<attr name="search_shadow" format="boolean" />
<attr name="search_shadow_color" format="boolean" />
<attr name="search_elevation" format="dimension" />
<attr name="search_clear_on_open" format="boolean" />
<attr name="search_clear_on_close" format="boolean" />
<attr name="search_hide_on_keyboard_close" format="boolean" />
<attr name="search_show_progress" format="boolean" />
<attr name="search_cursor_drawable" format="integer" />
```
