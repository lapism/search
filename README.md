# SearchView & SearchBar

Features: Material Design, Bar/Toolbar/MenuItem version, History, Styling.

Google Material Design Pattern:  
https://material.io/guidelines/patterns/search.html

Version History:  
https://bintray.com/lapism/maven/searchview/view

Material colors in the project:  
https://gist.github.com/lapism/3b417142300d9dbde3b4

## Donations:
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

## Usage
**Add the dependencies to your gradle file:**
```groovy
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

**Styling SearchView:**
```xml
    <declare-styleable name="SearchView">
        <attr name="search_layout" format="reference" />
        <attr name="search_logo" format="enum">
            <enum name="google" value="1000" />
            <enum name="g" value="1001" />
            <enum name="hamburger" value="1002" />
            <enum name="arrow" value="1003" />
        </attr>
        <attr name="search_shape" format="enum">
            <enum name="classic" value="2000" />
            <enum name="rounded" value="2001" />
            <enum name="oval" value="2002" />
        </attr>
        <attr name="search_theme" format="enum">
            <enum name="color" value="3000" />
            <enum name="light" value="3001" />
            <enum name="dark" value="3002" />
        </attr>
        <attr name="search_version" format="enum">
            <enum name="toolbar" value="4000" />
            <enum name="menu_item" value="4001" />
        </attr>
        <attr name="search_version_margins" format="enum">
            <enum name="toolbar_small" value="5000" />
            <enum name="toolbar_big" value="5001" />
            <enum name="menu_item" value="5002" />
        </attr>
        <attr name="search_logo_icon" format="integer" />
        <attr name="search_logo_color" format="color" />
        <attr name="search_mic_icon" format="integer" />
        <attr name="search_mic_color" format="color" />
        <attr name="search_clear_icon" format="integer" />
        <attr name="search_clear_color" format="color" />
        <attr name="search_menu_icon" format="integer" />
        <attr name="search_menu_color" format="color" />
        <attr name="search_background_color" format="color" />
        <attr name="search_text_image" format="integer" />
        <attr name="search_text_color" format="color" />
        <attr name="search_text_size" format="dimension" />
        <attr name="search_text_style" format="enum">
            <enum name="normal" value="0" />
            <enum name="bold" value="1" />
            <enum name="italic" value="2" />
            <enum name="bold_italic" value="3" />
        </attr>
        <attr name="search_hint" format="string" />
        <attr name="search_hint_color" format="color" />
        <attr name="search_animation_duration" format="integer" />
        <attr name="search_shadow" format="boolean" />
        <attr name="search_shadow_color" format="color" />
        <attr name="search_elevation" format="dimension" />
    </declare-styleable>

    <declare-styleable name="SearchBar">
        <attr name="search_layout" />
        <attr name="search_logo" />
        <attr name="search_shape" />
        <attr name="search_theme" />
    </declare-styleable>
```

### Methods
| name | format | default | description
| ------ | ------ |  ------ |------ |
| setButtonsVisibility | boolean | true | Viditelnost všech tlačítek