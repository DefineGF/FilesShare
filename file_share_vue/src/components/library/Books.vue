<template>
  <div>
    <el-row style="height: 840px;">
      <search-bar @onSearch="searchResult" ref="searchBar"></search-bar>

      <el-tooltip effect="dark" placement="right"
                  v-for="item in books.slice((currentPage - 1) * pagesize, currentPage * pagesize)" :key="item.id">
        <p slot="content" style="font-size: 14px;margin-bottom: 6px;">{{item.name}}</p>

        <p slot="content" style="font-size: 13px;margin-bottom: 6px">
          <span>{{item.author}}</span>
          <span>{{item.uploadDate}}</span>
        </p>
        <p slot="content" style="width: 300px" class="abstract">{{item.brief}}</p>

        <el-card class="book"
                 style="width: 135px;margin-bottom: 20px;height: 300px;float: left;margin-right: 15px" bodyStyle="padding:10px" shadow="hover">

          <div class="cover" @click="editBook(item)">
            <img :src="item.cover" alt="封面">
          </div>

          <div class="info">
            <div class="name">
              <a href="">{{item.name}}</a>
            </div>
            <i class="el-icon-delete" @click="deleteBook(item.id)"></i>
            <i class="el-icon-download" @click="downloadBook(item.id, item.name)"></i>
          </div>

          <div class="author">{{item.author}}</div>
        </el-card>
      </el-tooltip>

      <edit-form @onSubmit="loadBooks()" ref="edit"></edit-form>
      <contacts-select @onSubmit="askFile" ref="contacts"></contacts-select>
    </el-row>

    <el-row>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pagesize"
        :total="books.length">
      </el-pagination>
    </el-row>
  </div>
</template>

<script>
  import EditForm from './EditForm'
  import SearchBar from './SearchBar'
  import ContactsSelect from './ContactsSelect'

  export default {
    name: 'Books',
    components: {EditForm, SearchBar, ContactsSelect},
    data () {
      return {
        books: [],
        currentPage: 1,
        pagesize: 17,
        book_name: ''
      }
    },
    mounted: function () { // 页面加载完毕调用
      this.loadBooks()
    },
    methods: {
      // 加载书
      loadBooks () {
        var _this = this
        this.$axios.get('/books').then(resp => {
          if (resp && resp.status === 200) {
            _this.books = resp.data
          }
        })
      },

      // 删除书
      deleteBook (id) {
        this.$confirm('是否删除此文件信息?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            this.$axios.post('/delete', {id: id}).then(resp => {
              if (resp && resp.status === 200) {
                this.loadBooks()
              }
            })
          }
        ).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },

      // 编辑书
      editBook (item) {
        console.log("you click the book:" + item.name)
        this.$refs.edit.dialogFormVisible = true
        this.$refs.edit.form = {
          id: item.id,
          cover: item.cover,
          name: item.name,
          author: item.author,
          uploadDate: item.uploadDate,
          brief: item.brief,
          category: {
            id: item.bookCategory.id.toString(),
            name: item.bookCategory.category
          }
        }
      },

      // 下载书
      downloadBook(book_id, book_name) {
        let _this = this
        this.book_name = book_name
        this.$axios.get("/book/" + book_id + "/users")                // 获取该书的下载者
                   .then(resp => {
                     if (resp && resp.status === 200) {
                       _this.$refs.contacts.users = resp.data
                       _this.$refs.contacts.dialogFormVisible = true
                     } else {
                       console.log("加载失败!")
                     }
                   })

      },

      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage
        console.log(this.currentPage)
      },

      searchResult () {
        var _this = this
        this.$axios.get('/search?keywords=' + this.$refs.searchBar.keywords, {}).then(resp => {
          if (resp && resp.status === 200) {
            _this.books = resp.data
          }
        })
      },

      askFile(target_name) {
        let ask = "ASK:" + this.book_name + ";" + target_name
        this.send_message_str(ask)
      },

      send_message_str(msg) {
        let bytes = new Uint8Array(this.string_to_byte(msg));
          try {
              this.$ws.send(bytes);
          } catch (ex) {
              alert(ex.message);
          }
      },

      //将字符串转为 Array byte数组
     string_to_byte(str) {
        var bytes = [];
        var len, c;
        len = str.length;
        for(var i = 0; i < len; i++) {
            c = str.charCodeAt(i);
            if(c >= 0x010000 && c <= 0x10FFFF) {
                bytes.push(((c >> 18) & 0x07) | 0xF0);
                bytes.push(((c >> 12) & 0x3F) | 0x80);
                bytes.push(((c >> 6) & 0x3F) | 0x80);
                bytes.push((c & 0x3F) | 0x80);
            } else if(c >= 0x000800 && c <= 0x00FFFF) {
                bytes.push(((c >> 12) & 0x0F) | 0xE0);
                bytes.push(((c >> 6) & 0x3F) | 0x80);
                bytes.push((c & 0x3F) | 0x80);
            } else if(c >= 0x000080 && c <= 0x0007FF) {
                bytes.push(((c >> 6) & 0x1F) | 0xC0);
                bytes.push((c & 0x3F) | 0x80);
            } else {
                bytes.push(c & 0xFF);
            }
        }
        return bytes;
    },

    }
  }
</script>
<style scoped>

  .cover {
    width: 115px;
    height: 172px;
    margin-bottom: 7px;
    overflow: hidden;
    cursor: pointer;
  }

  img {
    width: 115px;
    height: 172px;
    /*margin: 0 auto;*/
  }

  .name {
    font-size: 14px;
    text-align: left;
  }

  .author {
    color: #333;
    width: 102px;
    font-size: 13px;
    margin-bottom: 6px;
    text-align: left;
  }

  .abstract {
    display: block;
    line-height: 17px;
  }

  .el-icon-delete {
    cursor: pointer;
    float: right;
  }

  .el-icon-download {
    cursor: pointer;
    float: right;
  }

  .switch {
    display: flex;
    position: absolute;
    left: 780px;
    top: 25px;
  }

  a {
    text-decoration: none;
  }

  a:link, a:visited, a:focus {
    color: #3377aa;
  }

</style>

