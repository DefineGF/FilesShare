<template>
  <div id="test">
    <input type="file" id="file_selected" onchange="websocket.on_file_send" value="" style="filter:alpha(opacity=0);opacity:0;width: 0;height: 0;"/>
    <router-view/>
  </div>
</template>

<script>
import websocket from '@/js/my_websocket.js'

export default {
  name: 'APP',
  data : function() {
    return {
      msg : ''
    }
  },
  methods: {

  },
}
</script>

<style scoped>

</style>
