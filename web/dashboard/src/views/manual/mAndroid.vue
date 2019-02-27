<template>
    <div id="help-page" class="help-android">
        <h1>发布一个 Android 版本</h1>
        <p>要发布一个安卓版本,可参考以下说明:</p>
        <h2>步骤</h2>
        <ol>
            <li><p><a href="#anchor-1">添加渠道</a></p></li>
            <li><p><a href="#anchor-2">创建 Android 版本</a></p></li>
            <li><p><a href="#anchor-3">上传 APK 包</a></p></li>
            <li><p><a href="#anchor-4">上架 APK 包</a></p></li>
        </ol>
        <h2 id="anchor-1">1.添加渠道</h2>
        <p>如果想发布一个安卓版本，你需要有一个“渠道”，添加渠道的方法见<b>操作手册</b><i class="fas fa-long-arrow-alt-right"></i><b><router-link :to="{ name: 'manual-channel' }">添加渠道</router-link></b>部分。</p>
        <h2 id="anchor-2">2.创建 Android 版本</h2>
        <p>点开<b><router-link :to="{ name: 'version' }">版本管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>选择<b><router-link :to="{ name: 'version-android' }">Android 版本管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>点击<b>添加 Android 版本</b>按钮。</p>
        <p>按照页面操作即可创建一个安卓版本。</p>
        <p>创建好的版本<code>默认为下架状态</code>。</p>
        <img src="~@/images/manual/android-1.png">
        <p>更新类型的详细解释可以参考<b>操作手册</b><i class="fas fa-long-arrow-alt-right"></i><b><router-link :to="{ name: 'manual-type' }">更新类型说明</router-link></b></p>
        <h2 id="anchor-3">3.上传 APK 包</h2>
        <p>此时创建的安卓版本没有APK包的，你需要去添加APK包。</p>
        <p>点击<b>包管理</b>按钮，进入APK包管理界面，点击<b>添加 APK 包</b>按钮，添加APK包。</p>
        <img src="~@/images/manual/android-2.png">
        <p>渠道码可以填入官方渠道码或已经添加好的渠道码。之后完成上传，APK包就添加成功了。</p>
        <p>上传好的APK包也<code>默认为下架状态</code>。</p>
        <h2 id="anchor-4">4.上架 APK 包</h2>
        <p>如果需要将安卓版本或对应的APK包上架，那么点击上下架开关<img src="~@/images/manual/android-3.png" style="width: 30px;">，输入要上架的版本号，通过校验即可上架。</p>
        <p>注意要把上架的<code>APK 包开启上架</code>。</p>
        <div class="end">完</div>
    </div>
</template>
