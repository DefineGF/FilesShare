<template>
  <div>
    <i class="el-icon-circle-plus-outline"  @click="dialogFormVisible = true"></i>
    <el-dialog title="添加/修改文件" :visible.sync="dialogFormVisible" @close="clear">
      <el-form v-model="form" style="text-align: left" ref="dataForm">
        <el-form-item label="文件名" :label-width="formLabelWidth" prop="name">
          <el-input v-model="form.name" autocomplete="off" placeholder="不加《》"></el-input>
        </el-form-item>

        <el-form-item label="作者" :label-width="formLabelWidth" prop="author">
          <el-input v-model="form.author" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="上传日期" :label-width="formLabelWidth" prop="uploadDate">
          <el-input v-model="form.uploadDate" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="封面" :label-width="formLabelWidth" prop="cover">
          <el-input v-model="form.cover" autocomplete="off" placeholder="图片 URL"></el-input>
        </el-form-item>

        <el-form-item label="简介" :label-width="formLabelWidth" prop="brief">
          <el-input type="textarea" v-model="form.brief" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="分类" :label-width="formLabelWidth" prop="cid">
          <el-select v-model="form.bookCategory.id" placeholder="请选择分类">
            <el-option label="计算机" value="1"></el-option>
            <el-option label="推理" value="2"></el-option>
            <el-option label="历史" value="3"></el-option>
            <el-option label="科幻" value="4"></el-option>
            <el-option label="文学" value="5"></el-option>
            <el-option label="生活" value="6"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="id" style="height: 0">
          <el-input type="hidden" v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'EditForm',
    data () {
      return {
        dialogFormVisible: false,
        form: {
          id: '',
          name: '',
          author: '',
          uploadDate: '',
          cover: '',
          brief: '',
          bookCategory: {
            id: '',
            category: ''
          }
        },
        formLabelWidth: '120px'
      }
    },
    methods: {
      clear () {
        this.form = {
          id: '',
          name: '',
          author: '',
          uploadDate: '',
          cover: '',
          brief: '',
          bookCategory: {
            id: '',
            category:''
          }
        }
      },
      onSubmit () {
        this.$axios
          .post('/books', {
            id: this.form.id,
            cover: this.form.cover,
            name: this.form.name,
            author: this.form.author,
            uploadDate: this.form.uploadDate,
            brief: this.form.brief,
            bookCategory: this.form.bookCategory
          }).then(resp => {
          if (resp && resp.status === 200) {
            this.dialogFormVisible = false
            this.$emit('onSubmit')
          }
        })
      }
    }
  }
</script>

<style scoped>
  .el-icon-circle-plus-outline {
    margin: 50px 0 0 20px;
    font-size: 100px;
    float: left;
    cursor: pointer;
  }
</style>


