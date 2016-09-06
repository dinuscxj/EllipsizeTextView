## EllipsizeTextView

The EllipsizeTextView offers a lot of flexibility when it comes to causing 
words in the text that are longer than the view is wide to be omitted 
instead of broken in the middle. 

The EllipsizeTextView is powerful and flexible. You do not need to worry 
about the `emoji` truncated cause garbled, and you also do not need to worry 
about the text be affected by the class `CharacterStyle` and its subclass
truncated cause unexpected result. 

The EllipsizeTextView extends TextView, it supports multiple lines omit
redundant characters, but the method `{@link EllipsizeTextView#setMaxLines(int)}` 
will be always needed to call. besides, you can call the method
`{@link EllipsizeTextView#setEllipsizeText(CharSequence, int)}` to custom 
the content, style, and index(in the reverse order) of the ellipsis.
#### Preview
![](https://raw.githubusercontent.com/dinuscxj/EllipsizeTextView/master/Preview/EllipsizeTextView.gif?width=300)

## Installation

Add the following dependency to your build.gradle file:
```gradle
    dependencies {
        compile 'com.dinuscxj:ellipsizetextview:1.0.1'
    }
```

## Usage

### Normal Mode 
You can specify the attributes from a layout XML like:
```xml
    <com.dinuscxj.ellipsize.EllipsizeTextView
        android:id="@+id/tv_ellipsize3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:layout_marginTop="16dp"
        app:ellipsize_index="8"
        app:ellipsize_text="..."
        android:maxLines="2" />
```
Or from code like:

```java
    mEllipsizeText.setMaxLines(2);
    mEllipsizeText.setEllipsizeText(ellipsizeText, 8);
    mEllipsizeText.setText(R.string.long_text);
```

### Auto Mode
the diff from Normal Mode is that you should set the attribute `android:layout_height` 
to a accurate value and the attribute `android:maxLines` is optional.
   
```xml
    <com.dinuscxj.ellipsize.EllipsizeTextView
        android:id="@+id/tv_ellipsize3"
        android:layout_width="wrap_content"
        android:layout_height="50dp"
        android:textSize="16sp"
        android:layout_marginTop="16dp"
        app:ellipsize_index="8"
        app:ellipsize_text="..." />
```

Or from code like:

```java
    mEllipsizeText.getLayoutParams().height = 50dp;
    mEllipsizeText.setEllipsizeText(ellipsizeText, 8);
    mEllipsizeText.setText(R.string.long_text);
```
 
## License

    Copyright 2015-2019 dinus

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
