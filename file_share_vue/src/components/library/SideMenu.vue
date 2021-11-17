<template>
  <el-menu class="categories" default-active="0" active-text-color="red" @select="handleSelect">
    <el-menu-item index="0">
      <i class="el-icon-menu"></i>
      <span slot="title">全部</span>
    </el-menu-item>

    <el-menu-item index="1">
      <i class="el-icon-menu"></i>
      <span slot="title">计算机</span>
    </el-menu-item>

    <el-menu-item index="2">
      <i class="el-icon-menu"></i>
      <span slot="title">推理</span>
    </el-menu-item>

    <el-menu-item index="3">
      <i class="el-icon-menu"></i>
      <span slot="title">历史</span>
    </el-menu-item>

    <el-menu-item index="4">
      <i class="el-icon-menu"></i>
      <span slot="title">科幻</span>
    </el-menu-item>

    <el-menu-item index="5">
      <i class="el-icon-menu"></i>
      <span slot="title">文学</span>
    </el-menu-item>

    <el-menu-item index="6">
      <i class="el-icon-menu"></i>
      <span slot="title">生活</span>
    </el-menu-item>

    <el-menu-item index="7">
      <i class="el-icon-menu"></i>
      <span slot="title">个人</span>
    </el-menu-item>

  </el-menu>
</template>

<script>
 export default {
    name: 'SideMenu',
    data () {
      return {
        cid: ''
      }
    },
    methods: {
      handleSelect (key, keyPath) {
        this.cid = key
        this.$emit('indexSelect')
      }
    }
  }
</script>

<style scoped>
  .categories {
    position: fixed;
    margin-left: 50%;
    left: -600px;
    top: 100px;
    width: 150px;
  }
</style>

