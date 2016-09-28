# 简介

一个手势解锁的view(仿照支付宝手势解锁的样式)，可以改变手势解锁的颜色、样式（目前只有两种样式）、大小等等。
使用方法如下：

需要在整个项目的build.gradle中添加如下依赖项（jcenter暂时没有传上去）
``` java
allprojects {
    repositories {
        maven {
            url  "http://dl.bintray.com/loner/maven"
        }
    }
}
```

在你自己的app的build.gradle中添加如下依赖项
``` java
compile 'loner.library.gesture:gesture:1.0.1'
```

# 使用介绍

在需要的xml中添加view：
``` java
<loner.widget.lockpattern.view.LockPatternView
        android:id="@+id/mLocusPassWordViewFirstRegister"
        android:layout_width="wrap_content"
        android:layout_height="400dp"
        app:radius="30dp"
        android:layout_gravity="center"></loner.widget.lockpattern.view.LockPatternView>
```
在values/attrs.xml中添加
``` java
<declare-styleable name="LockPatternAttrs">
        <attr name="pressColor" format="color" />
        <attr name="initColor" format="color" />
        <attr name="errorColor" format="color" />
        <attr name="style" format="integer" />
        <attr name="radius" format="dimension"/>
    </declare-styleable>
```
这里几个属性简单介绍一下：
-  pressColor表示按下是按钮的颜色。 
-  initColor表示初始时按钮的颜色。
-  errorColor表示按下是按钮的颜色。 
-  style表示手势解锁样式，目前只有0和1两种样式，大家可以试试
-  radius表示手势解锁键盘的圆圈半径

布局中放好view之后，加功能，我把需要的功能提供给大家，使用如下：

- 设置密码长度
``` java
lpv.setPasswordMinLength(3);

```
- 获取手势密码
``` java
lpv.setOnCompleteListener(new OnCompleteListener() {
     @Override
     public void onComplete(String mPassword) {
     	...
     }
 }
 ```
 - 设置view不可触摸

 ``` java
lpv.disableTouch();
```
 - 设置错误

 ``` java
lpv.error();
```
 - 清空

 ``` java
lpv.clearPassword();
```
 - 保存密码

 ``` java
lpv.setPassWord();
```
 - 获取密码

 ``` java
lpv.getPassWord();
```
 - 设置小键盘显示
 
 ``` java
 LockPatternSmallView lps=(LockPatternSmallView)findViewById();
lps.setOndraw(str);
```


# 注意
-  我的demo代码比较多，其实实现最简单的功能就是在xml放view，attrs参数都不用设置，这样就可以实现一个最简单的手势解锁控件。
-  保存的密码是明文，键盘中1-9的数字，中间用'，'隔开（当然你也可以自己保存密码）
-  设置小键盘，其实就是类似于支付宝一样，上面有一个提示的小手势键盘，大家可以使用，也可以不用使用，xml中使用方法跟LockPatternView一样。
-  radius这个属性，大家尽量不用改变，因为你们自己改变半径的时候，可能会出现一些布局变形的情况，所以建议还是不设置这个参数，使用默认的半径来做。


具体不明白的地方，可以看这个github的我使用的小demo。




