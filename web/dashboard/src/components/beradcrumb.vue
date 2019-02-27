<template>
    <div id="breadcrumb">
        <Breadcrumb>
            <BreadcrumbItem :to="{name:'home'}">主页</BreadcrumbItem>
            <BreadcrumbItem v-for="item in routerMatched" v-if="hasValue(item.meta.title) && item.name !== 'home'" :key="item.name" :to="{name:item.name}">{{item.meta.title}}
            </BreadcrumbItem>
        </Breadcrumb>
    </div>
</template>
<script>
import { hasValue } from '@/libs/util';
export default {
    data () {
        return {
            routerMatched: []
        };
    },
    created () {
        this.routeWatch();
    },
    methods: {
        routeWatch () {
            this.routerMatched = this.$route.matched;
        },
        hasValue
    },
    watch: {
        '$route': 'routeWatch'
    }
};
</script>
<style scoped lang="scss">
    #breadcrumb{
        margin:16px 0
    }
</style>
