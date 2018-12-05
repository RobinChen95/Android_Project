# 安卓天气软件 By RobinChen95@PKUSE
Cooperative developed by: Windows10 And MacOS High Sierra using Android Studio</br>
安卓版本的天气软件，同时在一台windows电脑与Mac电脑上部署了开发环境，最终版本完成时间：2018/12/05</br></br>
实现的功能：天气展示、下拉刷新、未来天气、省份聚合、城市搜索、背景变换</br></br>
额外自定义了：</br>
1、ExpandableListView二级列表控件及其Adapter</br>
2、pageView分页展示控件及其Adapter、重写parseXML()解析函数</br>
3、下方圆点的展示及其制作相关函数</br>
4、SwipeRefreshLayout下拉刷新控件及多线程</br>
5、ClearEditText类以及搜索后的数据过滤方法</br>
6、使用Python爬虫实时爬取天气信息到数据库</br>
7、自定了34个省份的选择界面及其相关的更改背景函数</br>
8、有可能自定义了其他东西，但是写到代码过于复杂最后忘了加在哪里了</br>

<h1>&nbsp&nbsp主界面与下拉刷新：</h1>
<h4>&nbsp&nbsp实现了天气展示、主界面下拉刷新、ViewPager的未来天气以及下方小圆点指示</h4>
<p>
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/主界面.png" width="340" height="571">
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/下拉刷新.png" width="340" height="571">
</p>
 
<h1>&nbsp&nbsp列表能够折叠并且带有图片的选择界面：</h1>
<h4>&nbsp&nbsp实现了ExpandableListView及其Adapter，并修改Adapter配合搜索数据的过滤</br>
&nbsp&nbsp在网上找了34张各省的图片并自己加工，用于折叠列表的省份信息展示</h4>
<p>
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/选择界面.png" width="340" height="571">
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/选择界面2.png" width="340" height="571">
</p>
 
<h1>&nbsp&nbsp根据用户选择的城市所在的省份变换背景：</h1>
<h4>&nbsp&nbsp利用了安卓Application生命周期长的特点设置了全局参数保存citycode以及cityname，此处没有用Intent</h4>
<p>
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/变换背景1.png" width="340" height="571">
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/变换背景2.png" width="340" height="571">
 <p>
 
<h1>&nbsp&nbsp能够实时显示搜索结果的搜索界面：</h1>
<h4>&nbsp&nbsp能够根据用户输入的内容，实时变更返回的省份以及城市结果</br>
&nbsp&nbsp比如搜索“内”字会出现河北、河南、四川三个省份，因为这三个省份都有名字包含“内”字的城市</h4>
<p>
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/搜索结果1.png" width="340" height="571">
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/搜索结果2.png" width="340" height="571">
<p>
 
<h1>&nbsp&nbsp点击最后的搜索结果：</h1>
<h4>&nbsp&nbsp部分省份图片分辨率不够，导致界面不好看，以后会补上</br>
&nbsp&nbsp程序中有部分不影响使用体验的小BUG，正在绝赞寻找中！</h4>
<img src="https://github.com/RobinChen95/Android_Project/blob/master/result/搜索结果.png" width="340" height="571">

  
