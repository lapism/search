# SearchAdapter

## Donations

`Please support me!`

<a href="https://www.paypal.me/lapism">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView/blob/master/images/donate.png" />
</a>

### Java or Kotlin
```java
        SearchItem suggestion = new SearchItem(this);
        suggestion.setTitle("Title");
        suggestion.setIcon_1_resource(R.drawable.search_ic_search_black_24dp);
        suggestion.setSubtitle("Subtitle");

        List<SearchItem> suggestions = new ArrayList<>();
        suggestions.add(suggestion);

        final SearchHistoryTable mHistoryDatabase = new SearchHistoryTable(this);

        SearchAdapter searchAdapter = new SearchAdapter(this);
        searchAdapter.setSuggestionsList(suggestions);
        searchAdapter.setOnSearchItemClickListener(new SearchAdapter.OnSearchItemClickListener() {
            @Override
            public void onSearchItemClick(View view, int position, CharSequence title, CharSequence subtitle) {
                SearchItem item = new SearchItem(MainActivity.this);
                item.setTitle(title);
                item.setSubtitle(subtitle);

                mHistoryDatabase.addItem(item);
            }
        });
        
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new Search.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(CharSequence query) {
                        SearchItem item = new SearchItem(MainActivity.this);
                        item.setTitle(query);
        
                        mHistoryDatabase.addItem(item);
                        return true;
                    }
        
                    @Override
                    public boolean onQueryTextChange(CharSequence newText) {
                        return false;
                    }
                });
```

### Public methods
| Name | Format | Default | Description
| ------ | ------ |  ------ |------ |
| setIcon1Color(@ColorInt int color) | ... | ... | ...
| setIcon2Color(@ColorInt int color) | ... | ... | ...
| setTitleColor(@ColorInt int color) | ... | ... | ...
| setTitleHighlightColor(@ColorInt int color) | ... | ... | ...
| setSubtitleColor(@ColorInt int color) | ... | ... | ...
| setTextStyle(int style) | ... | ... | ...
| setTextFont(Typeface font) | ... | ... | ...
| setTheme(@Search.Theme int theme)  | ... | ... | ...
| getSuggestionsList() | ... | ... | ...
| setSuggestionsList(List<SearchItem> suggestionsList) | ... | ... | ...
| getResultsList() | ... | ... | ...
| setOnSearchItemClickListener(OnSearchItemClickListener listener) | ... | ... | ...