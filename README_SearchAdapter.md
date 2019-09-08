# SearchAdapter

## Donations

`Please support me!`

<a href="https://www.paypal.me/lapism">
  <img alt="Get it on Google Play"
       src="https://github.com/lapism/SearchView-SearchBar/blob/master/images/donate.png" />
</a>

### Java
```java
        SearchItem suggestion = new SearchItem(this);
        suggestion.setTitle("Title");
        suggestion.setIcon1Resource(R.drawable.search_ic_outline_search_24px);
        suggestion.setSubtitle("Subtitle");

        List<SearchItem> suggestions = new ArrayList<>();
        suggestions.add(suggestion);

        final SearchHistoryTable mHistoryDatabase = new SearchHistoryTable(this);

        SearchAdapter searchAdapter = new SearchAdapter(this);
        searchAdapter.setSuggestionsList(suggestions);
        searchAdapter.setOnSearchItemClickListener(new SearchAdapter.OnSearchItemClickListener() {
            @Override
            public void onSearchItemClick(int position, CharSequence title, CharSequence subtitle) {
                SearchItem item = new SearchItem(MainActivity.this);
                item.setTitle(title);
                item.setSubtitle(subtitle);

                mHistoryDatabase.addItem(item);
            }
        });

        SearchView INFO2 = findViewById(R.id.INFO2);
        INFO2.setHint("Search");
        INFO2.setVersion(Search.Version.MENU_ITEM);
        INFO2.setVersionMargins(Search.VersionMargins.MENU_ITEM);
        INFO2.setAdapter(searchAdapter);
        INFO2.setOnQueryTextListener(new Search.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(CharSequence query) {
                SearchItem item = new SearchItem(MainActivity.this);
                item.setTitle(query);

                mHistoryDatabase.addItem(item);
                return true;
            }

            @Override
            public boolean onQueryTextChange(CharSequence newText) {
                return true;
            }
        });
```

### Public methods
| Name | Format | Default | Description
| ------ | ------ |  ------ |------ |
| setIcon1Color(@ColorInt int color) | ... | ... | ...