<template>
    <div id="help-page" class="help-android">
        <h1>添加一个 RN 路由</h1>
        <h2>步骤</h2>
        <ol>
            <li><p><a href="#router-1">添加 RN 路由</a></p></li>
        </ol>
        <h2 id="router-1">1.添加 RN 路由</h2>
        <p>点开<b><router-link :to="{ name: 'rn' }">RN 管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>选择<b><router-link :to="{ name: 'rn-router' }">RN 路由管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>点击<b>添加 RN 路由</b>按钮，即可添加。</p>
        <blockquote>
            <p>RN 路由的<code>适用终端可以多选</code>。</p>
            <p>被拦截URL和目标URL即，你想让访问URL_A的人，不让他不访问URL_A，而让其跳转到URL_B，则URL_A为被拦截URL，URL_B为目标URL。</p>
            <p>版本区间根据勾选的适用终端来填写。</p>
            <p>默认状态也为<code>关闭状态</code>，有上线需要可以通过编辑来修改状态。</p>
        </blockquote>
        <img src="~@/images/manual/router-1.png">
        <div class="end">完</div>
    </div>
</template>
