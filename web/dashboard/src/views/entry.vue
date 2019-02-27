<template>
    <div id="page-admin">
        <v-sidebar></v-sidebar>
        <div class="page-body">
            <v-header></v-header>
            <div class="page-body-content">
                <v-beradcrumb></v-beradcrumb>
                <router-view/>
            </div>
        </div>
    </div>
</template>
<script>
import Sidebar from '@/components/sidebar';
import Header from '@/components/header';
import Beradcrumb from '@/components/beradcrumb';
export default {
    components: {
        vSidebar: Sidebar,
        vHeader: Header,
        vBeradcrumb: Beradcrumb
    }
};
</script>
