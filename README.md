Made by [Antino Labs](https://www.antino.io/) with ❤️
# DataPicker Library 

<img src="https://github.com/antinolabs/DataPickerLib/blob/master/device-2020-05-14-174516.png?raw=true" width="200" alt="accessibility text">

An Android Library written in Java, to pick Images/Videos with a single implementation. 



## Installation

### gradle
Add dependency in your build.gradle.

```bash
implementation 'io.antinolabs.libs:1.0.0'
```

## Usage

```java
DataPicker
      .with(MainActivity.this)
      .setSelectMaxCount(2)
      .setTextNameBottomSheetHeading("Select Media")
      .setTextNameBottomSheetHeadingClose("Done")
      .setPagerTabTextColor(getResources().getColor(android.R.color.black))
      .selectedImagePhotoEmptyText("No media selected")
      .selectedColorEmptyText(Color.rgb(240, 120, 120))
      .selectedImagesEnable(true)
      .selectedVideosEnable(true)

      .show(new BottomSheetPickerFragment.OnMultiImageSelectedListener() {
        @Override
        public void onImagesSelected(final List<Uri> uriList) {
          for (int i = 0; i < uriList.size(); i++) {
            //get list of uri here
          }
        }
      });
```

## Attributes


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
