<template>
    <div id="help-page" class="help-android">
        <h1>添加一个 RN 包</h1>
        <h2>步骤</h2>
        <ol>
            <li><p><a href="#router-1">添加 RN 包</a></p></li>
        </ol>
        <h2 id="router-1">1.添加 RN 包</h2>
        <p>点开<b><router-link :to="{ name: 'rn' }">RN 管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>选择<b><router-link :to="{ name: 'rn-package' }">RN 包管理</router-link></b><i class="fas fa-long-arrow-alt-right"></i>点击<b>添加 RN 包</b>按钮，即可添加。</p>
        <h2>部分选项说明</h2>
        <ul>
            <li><p>模块名称</p><p>所填的内容应该为字母大小写和下划线。</p></li>
            <li><p>通用昵称</p><p>可以为中文。</p></li>
            <li><p>RN包资源地址</p><p>上传完RN包后自动填写。</p></li>
            <li><p>状态</p><p>默认为关闭状态，如果需要上线，可通过编辑修改状态为“线上开启”。</p></li>
        </ul>
        <img src="~@/images/manual/rn-1.png">
        <div class="end">完</div>
    </div>
</template>
