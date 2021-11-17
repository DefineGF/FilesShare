<template>
<body id="poster">
  <el-form class="login-container" label-position="left" label-width="0px">
    <h3 class="login_title">系统登录</h3>

    <el-form-item>
      <el-input type="text" v-model="loginForm.username" placeholder="账号" auto-complete="off" ></el-input>
    </el-form-item>

    <el-form-item>
      <el-input type="password" v-model="loginForm.password" placeholder="密码" auto-complete="off" ></el-input>
    </el-form-item>

    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 100%; background: #505458; border: none" v-on:click="login">登录</el-button>
    </el-form-item>

  </el-form>
</body>

</template>

<script>
  export default {
    name: 'Login',
    data () {
      return {
        loginForm: {
          username: '',
          password: '',
          userid: -1
        },
        responseResult: []
      }
    },
    methods: {
      login () {
        var _this = this
        this.$axios.post('/login', {
          username: this.loginForm.username,
          password: this.loginForm.password
        }).then(successResponse => {
          if (successResponse.data.code === 200) {
            _this.loginForm.userid = Number(successResponse.data.describe)
            _this.$store.commit('login', _this.loginForm)
            
            console.log("login successful: " + JSON.stringify(_this.$store.state))
            var path = this.$route.query.redirect
            _this.$router.replace({path: path === '/' || path === undefined ? '/home' : path})
          } else {
            _this.$message({
              type: 'info',
              message: successResponse.data.describe
            })
          }
        })
      }
    }
  }
</script>

<style>
 #poster {
    background:url("../assets/shi.jpg") no-repeat;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  body{
    margin: 0px;
  }

  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    /* margin-top: 90px;
    margin-left: 90px; */
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
</style>

