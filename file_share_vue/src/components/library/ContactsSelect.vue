<template>
  <div>
    <i @click="dialogFormVisible = true"></i>
    <el-dialog title="选择联系人" :visible.sync="dialogFormVisible" @close="clear">
      <el-form>
        <el-form-item :label-width="formLabelWidth" prop="uid">
          <el-select v-model="uname" placeholder="请选择请求对象:">
            <el-option v-for="item in users" :key="item.id" :value="item.name" :label="item.name"></el-option>
          </el-select>
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
    name: 'ContactsSelect',
    data () {
      return {
        dialogFormVisible: false,
        formLabelWidth: '60px',
        uname: '',
        users: []
      }
    },
    methods: {
      clear () {
        this.uname = ''
      },

      onSubmit () {
        console.log("开始向 name: " + this.uname + " 请求文件")
        this.dialogFormVisible = false
        this.$emit('onSubmit', this.uname) // 下载
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


