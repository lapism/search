# SearchView & SearchBar

Persistent SearchView Library.  
Features: Material Design, Bar/Toolbar/MenuItem version, History, Styling.

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
    implementation 'com.lapism:searchview:5.0.0-beta1'
}
```
**SearchBar**  

![Screenshot 2](https://github.com/lapism/SearchView/blob/master/images/image_2.png)
![Screenshot 1](https://github.com/lapism/SearchView/blob/master/images/image_1.png)
![Screenshot 2](https://github.com/lapism/SearchView/blob/master/images/image_2.png)

**SearchView**  

![Screenshot 1](https://github.com/lapism/SearchView/blob/master/images/image_1.png)
![Screenshot 2](https://github.com/lapism/SearchView/blob/master/images/image_2.png)
![Screenshot 1](https://github.com/lapism/SearchView/blob/master/images/image_1.png)

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
        public void onMicIconClick() {
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

**XML with CoordinatorLayout:** 
 ```xml

 ```

**Styling SearchView:**
```xml

```
