<template>
  <div style="border: 1px solid #ccc">
    <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" />
    <Editor style="height: 500px; overflow-y: hidden;" v-model="formList.valueHtml" :defaultConfig="editorConfig"
      :mode="mode" @onCreated="handleCreated" @onChange="handleChange" />
  </div>
</template>


<script>
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import bus from "@/bus"
import { onBeforeUnmount, ref, shallowRef, onMounted, provide, inject, reactive } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

export default {
  components: { Editor, Toolbar },
  setup() {


    // 编辑器实例，必须用 shallowRef1
    const editorRef = shallowRef()


    let formList = reactive({
      valueHtml: "",
      files: [],
      finalFiles: []
    })


    const toolbarConfig = {
      insertKeys: {
        index: 0,
        keys: ['uploadAttachment']
      }
    }
    const editorConfig = {
      MENU_CONF: {
        uploadImage: {
          server: process.env.VUE_APP_UPLOAD_URL,

          maxFileSize: 50 * 1024 * 1024,
          fieldName: 'file',
          onBeforeUpload(file) {
            return file
          },
          customInsert(res, insertFn) {
            insertFn(res.data[0].path, res.data[0].name, res.data[0].path)
          }
        },
        insertImage: {
          onInsertedImage(file) {
            formList.files.push({
              name: file.alt,
              path: file.src
            })
          }
        },
        uploadAttachment: {
          server: process.env.VUE_APP_UPLOAD_URL,
          fieldName: 'file',
          onBeforeUpload(file) {
            return file
          },
          customInsert(res, file, insertFn) {
            insertFn(file.name, res.data[0].path)
          }
        }
      }
    }


    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
    })

    const handleCreated = (editor) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }
    const handleChange = () => {
      // console.log(editorRef.value.getElemsByType('image'))
      formList.finalFiles = editorRef.value.getElemsByType('image')
      bus.$emit('postValueHtml', formList)
    }

    bus.$on('getValueHtml', (content) => {
      console.log('getValueHtml')
      formList.valueHtml = content
    })

    onMounted(() => {
    })


    return {
      editorRef,
      formList,
      mode: 'default', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,
      handleChange
    };
  }
}
</script>
<style scoped>
</style>