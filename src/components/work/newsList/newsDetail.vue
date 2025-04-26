<template>
  <div class="custom-add">
    <el-dialog :title="drawerTitle" v-model="dialog_detail" ref="drawer" :width="800">
      <el-row>
        <el-col :span="22" :push="1"
          style="display:flex; justify-content: center;flex-direction: column;align-items: center;">
          <h3 style="font-size: 25px;margin-top: 0px;">{{ addForm.title }}</h3>
          <div style="max-height: 500px;overflow-y: auto" v-html="addForm.content"></div>
        </el-col>
      </el-row>
      <el-divider content-position="left"></el-divider>
      <div class="demo-drawer__footer"
        style="display:flex; justify-content: center;flex-direction: column;align-items: center;">
        <el-button class="el-button--custom" @click="cancelForm">关闭</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { ref, reactive, inject, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"
import moment from 'moment'


export default {
  name: "newsForm",
  setup() {
    let drawerTitle = ref('')
    let dialog_detail = inject('dialog_detail')
    let formLoading = ref(false)
    let uploadFileList = ref([])
    let addForm = reactive({
      id: "",
      title: "",
      content: ""
    })
    bus.$on('handleDetail', (rowValue) => {
      console.log(rowValue)
      addForm.content = rowValue.content
      addForm.title = rowValue.title
      nextTick(() => {
        let scours = document.querySelectorAll("span[data-w-e-type='attachment']")
        for (let item of scours) {
          let newA = document.createElement("a")
          newA.href = item.getAttribute('data-link')
          newA.text = item.getAttribute('data-filename')
          insertAfter(newA, item)
          item.remove()
        }
      })
    })

    function insertAfter(newElement, targetElement) {
      let parent = targetElement.parentNode
      if (parent.lastChild == targetElement) {
        parent.appendChild(newElement)
      } else {
        parent.insertBefore(newElement, targetElement.nextSibling)
      }
    }

    function cancelForm() {
      dialog_detail.value = false
    }

    return {
      drawerTitle,
      dialog_detail,
      addForm,
      formLoading,
      cancelForm,
    }
  }
}
</script>


<style scoped lang="scss">
.custom-add {

  :deep(.el-dialog__body) {
    color: #000000;
  }

  :deep(.el-form-item__content) {
    text-align: left;

  }

  :deep(.el-drawer__header span) {
    outline: none;
    color: $font-color-theme;
    font-size: 20px;
    font-weight: bold;
    text-align: left;
  }
}
</style>