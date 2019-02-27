<template>
    <div id="help-page" class="help-android">
        <h1>发布一个 iOS 版本</h1>
        <p>要发布一个 iOS 版本,可参考以下说明:</p>
        <h2>步骤</h2>
        <ol>
            <li><p><a href="#anchor-1">添加 iOS 版本</a></p></li>
        </ol>
        <h2>1.添加 iOS 版本</h2>
        <p>点开<b><router-link :to="{ name: 'version' }">版本管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>选择<b><router-link :to="{ name: 'version-ios-redirect' }">iOS 版本管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>点击<b>添加 iOS 版本</b>按钮，即可添加。</p>
        <img src="~@/images/manual/ios-1.png">
        <p>默认为未上架状态，请在需要上架<img src="~@/images/manual/android-3.png" style="width: 30px;">的时候进行上架。</p>
        <p>更新类型的详细解释可以参考<b>操作手册</b><i class="fas fa-long-arrow-alt-right"></i><b><router-link :to="{ name: 'manual-type' }">更新类型说明</router-link></b></p>
        <div class="end">完</div>
    </div>
</template>
