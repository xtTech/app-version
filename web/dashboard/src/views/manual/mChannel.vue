<template>
    <div id="help-page" class="help-android">
        <h1>添加渠道</h1>
        <h2>渠道是什么?</h2>
        <p>这里的渠道指的是你可以通过什么途径来下载我们发布的APP。</p>
        <p>渠道分为官方渠道和非官方渠道。</p>
        <p>一个应用默认有一个官方渠道，这个官方渠道指的是我们对应的APP的官网的APP下载处，拿“泰然金融”APP举例，泰然金融的官方渠道就是指泰然金融官网的APP下载处。</p>
        <p>官方渠道的渠道码为<code>official</code>。</p>
        <h2>1.添加一个渠道</h2>
        <p>点开<b><router-link :to="{ name: 'version' }">版本管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>选择<b><router-link :to="{ name: 'version-channel' }">渠道管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i><b>添加渠道</b>，即可添加新的渠道。</p>
        <div class="end">完</div>
    </div>
</template>
