<template>
  <el-container>
    <el-aside style="width: 200px;margin-top: 20px">
      <switch></switch>
      <SideMenu @indexSelect="listByCategory" ref="sideMenu"></SideMenu>
    </el-aside>

    <el-main>
      <books class="books-area" ref="booksArea"></books>
    </el-main>
  </el-container>
</template>

<script>
  import SideMenu from './SideMenu'
  import Books from './Books'

  export default {
    name: 'AppLibrary',
    components: {Books, SideMenu},

    methods: {
      listByCategory () {
        var _this = this
        var cid = this.$refs.sideMenu.cid
        var url = ''
        if(cid < 7) {   // 根据书的种类加载
          url = 'categories/' + cid + '/books'
        } else {        // 根据用户信息加载
          url = 'user/' + _this.$store.state.user.userid + '/books'
        }
        console.log("ask the url is: " + url)
        this.$axios.get(url).then(resp => {
            if (resp && resp.status === 200) {
              _this.$refs.booksArea.books = resp.data
            }
        })
      }
    },
  }
</script>

<style scoped>
  .books-area {
    width: 990px;
    margin-left: auto;
    margin-right: auto;
  }
</style>

