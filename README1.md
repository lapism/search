# lib-gallery

Tato knihovna obsahuje implementaci galerie.

  - Umožňuje načítat URL jednotlivých obrázků.
  - Obsahuje tři možností zobrazení obrázků.

# Použití
Knihovna obsahuje tři možnosti zobrazení obrázků.

  - GalleryView
  - Gallery
  - Grid

# GalleryView
GalleryView slouží jako widget pro procházení obrázků v okně pomocí swipe gesta. Obsahuje tři pomocná tlačítka. Tlačitko se šipkou vlevo a vpravo slouží také k procházení obrázků. Tlačítku dole lze přiřadit text a click listener.

GalleryView je nutné přidat do XML kódu layoutu. 

```xml
    <cz.seznam.gallery.widget.GalleryView
        android:id="@+id/galleryView"
        android:layout_width="360dp"
        android:layout_height="180dp" />
```

GalleryView nemá žádné XML parametry, pouze metody přístupné v Java kódu.

```java
        GalleryView mGalleryView = (GalleryView) findViewById(R.id.galleryView);
        mGalleryView.setImageList(TestList.getImageList());
        mGalleryView.setBottomButtonText("Přejít do galerie");
        mGalleryView.setOnBottomButtonClickListener(new GalleryView.OnBottomButtonClickListener() {
            @Override
            public void onBottomButtonClick() {
            }
        });
```

| name | format | default | popis
| ------ | ------ |  ------ |------ |
| setButtonsVisibility | boolean | true | zobrazení tlačítek
| setBottomButtonText | int |  | text spodního tlačítka
| setBottomButtonText | CharSequence |  | text spodního tlačítka
| setImageList | ArrayList<String> |  | seznam url
| setOnBottomButtonClickListener | OnBottomButtonClickListener |  | click listener spodního tlačítka

# Gallery
Zde se využívá Builder, který vytvoří AppCompatActivity s nadhledem konkretního obrázku. Obrázek lze přiblížit pomocí běžného gesta pro zoom.

```java
        Gallery.with(this, TestList.getImageList())
                .setScrollList(false)
                .setCountable(true)
                .setToolbarTitle(getResources().getString(R.string.gallery))
                .show();
```

| name | format | default | popis
| ------ | ------ |  ------ |------ |
| with | AppCompatActivity, ArrayList<String> |  | inicializace - POVINNÝ PARAMETR
| show | | | zobrazit - POVINNÝ PARAMETR
| setToolbarTitle | CharSequence |  | titulek
| setToolbarTitle | int |  | titulek
| setScrollList | boolean | false | scrollovací nadhledy
| setSelectedImagePosition | int | 0 | pozice zobrazeného obrázku
| setCountable | boolean | false | počítání obrázků v titulku

# Grid
Grid je vytvářen pomocí Builderu, který vytváří novou AppCompatActivity s galerii ve formě mřížky.

```java
        Grid.with(this, TestList.getImageList())
                .setScrollList(false)
                .setCountable(false)
                .setToolbarTitle(getResources().getString(R.string.grid))
                .show();
```

| name | format | default | popis
| ------ | ------ |  ------ |------ |
| with | AppCompatActivity, ArrayList<String> |  | inicializace - POVINNÝ PARAMETR
| show | | | zobrazit - POVINNÝ PARAMETR
| setToolbarTitle | CharSequence |  | titulek
| setToolbarTitle | int |  | titulek
| setScrollList | boolean | false | scrollovací nadhledy
| setSelectedImagePosition | int | 0 | pozice zobrazeného obrázku
| setCountable | boolean | false | počítání obrázků v titulku
| setSpanCountPortrait | int | 2 | počet sloupců pro Portrait režim
| setSpanCountLandscape | int | 5 | počet sloupců pro Landscape režim

