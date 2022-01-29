# Android Sources
Android Sources

> Android 成长在于积累和分享

[toc]

# 前言
突发奇想的想了解下Android各个版本的历史变迁，当然不是说功能的变迁，仅仅是源代码的一些变化，所以突然想利用Git工具对各版本的历史变化做了个整理。

# [GitHub](https://github.com/CrazyUnluck/AndroidSources)
目前在GitHub上整理了**SDK**工具中能够下载的所有 **Android Sources**
大约是目前 **Android-15** 到 **Android-31** 的版本。

三个目的：
- 1.将Android源码整理到线上，方便随时查看。
- 2.利用git管理方便查看各个版本之间的变迁。
- 3.分享出来，方便大家查看的同时也可以产生更多的交流方式，毕竟个人精力有限，大众的智慧才是无穷的。

## Main Branch
囊括了所有版本的变化，可以较为方便的查看所有文件的历史变迁，比如，在哪个版本进行了改动，改动的内容是什么，其实了解到这些变化，可能更容易的去学习源码，理解源码的一些逻辑。

![AndroidSources.png](https://upload-images.jianshu.io/upload_images/11129092-5857d5138074db92.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## Android-XX Branch
为每个Android版本单独拉了分支，虽然可以在Main主干中查看历史变化，或者切换到某个历史版本，但是鉴于可能未来要为某个分支单独的列一些说明文件，所以还是先把分支单独拉出来了。
这样也可以方便大家即刻切换，随时方便查看每个版本，不用在历史版本中单独确定和查找了。

![branch.png](https://upload-images.jianshu.io/upload_images/11129092-33803bab710faf5c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# History
GitHub的历史记录查看不是特别的友好，比如现在文件比较多的情况下，单独查看某个文件的历史就很难找。
哈，也可能是我了解的不够多哈，欢迎大家指导一下。

我个人是结合**Android Studio**使用和学习的
比如下方 **HashMap** 的版本记录。
![HashMap History.png](https://upload-images.jianshu.io/upload_images/11129092-3265a5703727d16b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 其它问题
之后会在补充下每个大版本整体更新，会分别记录到每个版本分支中。
部分无法访问Github的同学或者访问比较慢的同学，可以参考[GitHub Host更新](https://www.jianshu.com/p/d3dde0893035)

之后会考虑在CSDN上做个镜像，方便访问慢的同学使用。

# 参考文献
[GitHub AndroidSources（https://github.com/CrazyUnluck/AndroidSources）](https://github.com/CrazyUnluck/AndroidSources)