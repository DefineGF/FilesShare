<template>
  <div>
    <!-- 将组件放在 Home 中 -->
    <div>

      <input type="file" id="files" ref="refFile" v-on:change="on_file_send" style="display:none; width: 5rem;height: 5rem;position: fixed;bottom: 5rem;right: 1rem;z-index: 9999;" />
    </div>

    <nav-menu></nav-menu>
    <router-view/>
  </div>
</template>

<script>
import NavMenu from '@/components/common/NavMenu'
import Vue from 'vue'
import {saveAs } from 'file-saver'

export default {
  name: 'Home',
  components: { NavMenu },
  data() {
    return {
      path: "ws://127.0.0.1:8888/file_forward/",
      ws: null,
      is_file: false,
      source_name: '',
      req_file_info : {
        file_name: '',
        file_size: 0
      }
    }
  },

  mounted() {
     // this.$refs.refFile.dispatchEvent(new MouseEvent('click'))
    this.ws_init()
  },

  methods: {
    ws_init() {
      let _this = this
      let _ws = this.ws
      if (this.ws == null) {
        _ws = new WebSocket(this.path + this.$store.state.user.username)
        _ws.binaryType = "arraybuffer"
        _ws.onopen = _this.on_open
        _ws.onclose = _this.on_close
        _ws.onmessage = _this.on_message
        Vue.prototype.$ws = _ws
      }
    },

    send_message_str(msg) {
      let bytes = new Uint8Array(this.string_to_byte(msg));
        try {
            this.$ws.send(bytes);
        } catch (ex) {
            alert(ex.message);
        }
    },
    on_file_send() {
        let file = this.$refs.refFile.files[0];
        let msg = "RES:" + file.name + ";" + file.size + ";" + this.source_name;
        this.send_message_str(msg);
        let file_reader = new FileReader();
        let _this = this
        file_reader.readAsArrayBuffer(file);
        file_reader.onload = function loaded(evt) {
            let data = evt.target.result;
            _this.$ws.send(data)
        }
        this.$refs.refFile.style.display = 'none'
        alert("文件发送成功!");
    },

    on_message(event) {
      if (!this.is_file) {
        let data = new Uint8Array(event.data);
        let dataStr = this.byte_to_string(data);

        console.log("get the message from server is: " + dataStr);

        if (dataStr.startsWith("ASK:")) { // 文件发送方会收到
            let source = dataStr.substring(dataStr.lastIndexOf(";") + 1);
            let file_name = dataStr.substring(dataStr.indexOf(":") + 1, dataStr.lastIndexOf(";"));
            this.source_name = source;
            let ans = confirm("客户端: " + source + " 请求文件:" + file_name);
            if (ans) { // 同意发送
              this.$refs.refFile.style.display = ""
            } else {  // 拒绝发送
              alert("很遗憾");
              let resp = "REF:" + source + ";";
              this.on_msg_send(resp);             // 向 服务器回复 "拒绝"
            }
        } else if (dataStr.startsWith("RES:")) {    // 文件请求方会收到
              this.is_file = true;
              this.req_file_info.file_name = dataStr.substring(dataStr.indexOf(":") + 1, dataStr.indexOf(";"));
              this.req_file_info.file_size = Number(dataStr.substring(dataStr.indexOf(";") + 1));
        } else if (dataStr.startsWith("REF:")) {    // 文件请求方被拒绝
              console.log("你被拒绝了，再试试吧~");
        }
      } else { // 保存文件
          let array_buffer = new Uint8Array(event.data);
          let file_blob = new Blob([array_buffer]);
          saveAs(file_blob, this.req_file_info.file_name);
          this.is_file = false;
        }
    },
    on_open() {
      console.log("连接成功!")
    },
    on_close() {
      console.log("断开连接!")
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
