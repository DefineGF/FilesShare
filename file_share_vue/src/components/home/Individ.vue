<template>
    <div>
      <el-form>
        <el-form-item>
          <el-input v-model="user_id" placeholder="id"></el-input>
          <el-button type="primary" v-on:click="init">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="send_info">发送</el-button>
        </el-form-item>
      </el-form>
    </div>
</template>

<script>
  export default {
    name: 'Individ',
    data() {
      return {
        user_id:'',
        websocket: '',
        path: 'ws://127.0.0.1:8443/file_forward/'
      }
    },
    methods: {
      init() {
        console.log("get the id: " + this.user_id);
        this.websocket = new WebSocket(this.path + this.user_id);
        this.websocket.binaryType = "arraybuffer";
        this.websocket.onopen = this.on_open;
        this.websocket.onerror = this.on_error;
        this.websocket.onmessage = this.on_message;
        this.websocket.onclose  = this.on_close;
      },
      send_info() {
        let msg = "ASK:万历十五年.pdf;2";
        this.send_message_str(msg);
      },

      on_open() {
        console.log("open successful!");

      },
      on_error() {
        console.log("连接错误!");
      },

      on_message(event) {
        let data = new Uint8Array(event.data);
        let dataStr = this.byte_to_string(data);
        console.log("get the message is: " + dataStr)
      },
      on_close() {
        console.log("close！");
      },

      send_message_str(msg) {
        let bytes = new Uint8Array(this.string_to_byte(msg));
        try {
            this.websocket.send(bytes);
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

      //byte数组转字符串
      byte_to_string(arr) {
          if (typeof arr === 'string') {
              return arr;
          }
          var str = '',
              _arr = arr;
          for (var i = 0; i < _arr.length; i++) {
              var one = _arr[i].toString(2),
                  v = one.match(/^1+?(?=0)/);
              if (v && one.length === 8) {
                  var bytesLength = v[0].length;
                  var store = _arr[i].toString(2).slice(7 - bytesLength);
                  for (var st = 1; st < bytesLength; st++) {
                      store += _arr[st + i].toString(2).slice(2);
                  }
                  str += String.fromCharCode(parseInt(store, 2));
                  i += bytesLength - 1;
              } else {
                  str += String.fromCharCode(_arr[i]);
              }
          }
          return str;
      }
    },
  }
</script>

<style scoped>

</style>

