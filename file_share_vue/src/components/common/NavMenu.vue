<template>
    <el-menu
    :default-active="'/library'"
      router
      mode="horizontal" background-color="white" text-color="#222" active-text-color="red" style="min-width: 1300px">

      <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name">
        {{ item.navItem }}
      </el-menu-item>

      <a href="#nowhere" style="color: #222; float: right; padding: 20px;" >退出</a>
      <i class="el-icon-close" style="float:right;font-size: 45px;color: #222;padding-top: 8px" @click="exit"></i>

      <span style="position: absolute;padding-top: 20px;right: 43%;font-size: 20px;font-weight: bold">
        文件分享平台
      </span>
    </el-menu>
</template>

<script>
  export default {
    name: 'NavMenu',
    data () {
      return {
        navList: [
          {name: '/library', navItem: '公共文件'},
          {name: '/Individ', navItem: '个人中心'}
        ]
      }
    },
    methods: {
      exit() {
        this.$message({
          type:'info',
          message: '退出系统成功!'
        })
        window.localStorage.clear()
        this.$ws.close()
        this.$router.replace('/login')
      }
    },
  }
</script>

<style scoped>
  a{
    text-decoration: none;
  }

  span {
    pointer-events: none;
  }

</style>

