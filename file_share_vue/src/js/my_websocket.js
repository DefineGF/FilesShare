
var url = "ws://127.0.0.1:8888/file_forward/";
var ws;
var is_file = false;
var sourceId;
var req_file_info = {file_name : "", file_size : 0};
var file_selected_id = ''


var websocket = {
    SetFileId: function (file_id) {
        file_selected_id = file_id
    },
    Init: function (username) {
        if ('WebSocket' in window) {
            ws = new WebSocket(url + username);
            ws.binaryType = "arraybuffer";
        } else {
            console.log("你的浏览器不支持 websocket");
            return;
        }
        ws.onopen = function () {
            console.log("连接成功！");
        }

        ws.onmessage = function (event) {
            if (!is_file) {
                let data = new Uint8Array(event.data);
                let dataStr = byte_to_string(data);

                console.log("get the message from server is: " + dataStr);

                if (dataStr.startsWith("ASK:")) { // 文件发送方会收到
                    let source = dataStr.substring(dataStr.lastIndexOf(";") + 1);
                    let file_name = dataStr.substring(dataStr.indexOf(":") + 1, dataStr.lastIndexOf(";"));
                    sourceId = source;
                    let ans = confirm("客户端: " + source + " 请求文件:" + file_name);
                    if (ans) {
                        // $("#file_selected").trigger("click");  // 同意发送：选择文件时触发 on_file_send() 函数
                        var file_selected = document.getElementById(file_selected_id);
                        file_selected.dispatchEvent(new MouseEvent('click'));
                    } else {  // 拒绝发送
                        alert("很遗憾");
                        let resp = "REF:" + source + ";";
                        this.on_msg_send(resp);             // 向 服务器回复 "拒绝"
                        ws.close();
                    }
                } else if (dataStr.startsWith("RES:")) {    // 文件请求方会收到
                    is_file = true;
                    req_file_info.file_name = dataStr.substring(dataStr.indexOf(":") + 1, dataStr.indexOf(";"));
                    req_file_info.file_size = Number(dataStr.substring(dataStr.indexOf(";") + 1));
                } else if (dataStr.startsWith("REF:")) {    // 文件请求方被拒绝
                    console.log("你被拒绝了，再试试吧~");
                }
            } else { // 保存文件
                let array_buffer = new Uint8Array(event.data);
                let file_blob = new Blob([array_buffer]);
                saveAs(file_blob, req_file_info.file_name);
                is_file = false;
                ws.close();
                ws = null;
            }
        };

        ws.onclose = function (event) {
            console.log("已经与服务器断开连接...");
        };
    },
    // 选择文件后触发的事件
    on_file_send: function () {
        let file = document.getElementById(file_selected_id).files[0];
        let msg = "RES:" + file.name + ";" + file.size + ";" + sourceId;
        this.on_msg_send(msg);
        let file_reader = new FileReader();
        file_reader.readAsArrayBuffer(file);
        file_reader.onload = function loaded(evt) {
            let data = evt.target.result;
            ws.send(data);
        }
        alert("文件发送成功!");
    },

    // 将 string类型数据转换为 array_buffer 类型后发送
    on_msg_send: function (msg) {
        console.log("on_msg_send: " + msg);
        let bytes = new Uint8Array(string_to_byte(msg));
        try {
            ws.send(bytes);
        } catch (ex) {
            alert(ex.message);
        }
    },

    on_close() {
      ws.close();
      ws = null;
    }
}
export default websocket;

//byte数组转字符串
function byte_to_string(arr) {
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

//将字符串转为 Array byte数组
function string_to_byte(str) {
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
}


