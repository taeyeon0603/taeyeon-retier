<template>
  <div class="custom-add">
    <el-dialog :title="drawerTitle" v-model="dialog" ref="drawer" :width="800">
      <el-row>
        <el-col :span="22" :push="1">
          <el-form :model="addForm" ref="forms" :rules="addRules" label-width="60px">
            <el-form-item label="标题" prop="title">
              <el-input v-model="addForm.title" placeholder="输入标题">
              </el-input>
            </el-form-item>
            <el-form-item label="内容" prop="content">
              <my-editor></my-editor>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <el-divider content-position="left"></el-divider>
      <div class="demo-drawer__footer">

        <el-button class="el-button--custom" @click="submitAdd" :loading="formLoading">{{
            formLoading ?
              '提交中...' : '确定'
        }}
        </el-button>
        <el-button class="el-button-white" @click="cancelForm">取消</el-button>
      </div>

    </el-dialog>
  </div>

</template>

<script>
import { ref, reactive, inject, onMounted, nextTick } from 'vue'
import { workNewsAdd } from '@/api/work'
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"
import moment from 'moment'
import MyEditor from '../../MyEditor'

export default {
  name: "newsForm",
  components: { MyEditor },
  setup() {
    let forms = ref(null)
    let drawerTitle = ref('新增')
    let dialog = inject('dialog')
    let formLoading = ref(false)
    let uploadFileList = ref([])
    let addForm = reactive({
      id: "",
      title: "",
      files: [
        {
          name: "",
          path: ""
        }
      ],
      content: ""
    })
    ///附件上传
    let actionAddress = process.env.VUE_APP_UPLOAD_URL

    let fileList = ref([])
    const handleRemove = (file, fileList) => {
      // console.log('handleChange', fileList)
      addForm.files.length = 0
      if (addForm.id === "") {
        if (fileList.length > 0) {
          for (let item of fileList) {
            addForm.files.push({
              name: item.name,
              path: item.response.data
            })
          }
        }
      } else {
        if (fileList.length > 0) {
          for (let item of fileList) {
            addForm.files.push({
              name: item.name,
              path: item.url
            })
          }
        }
      }
    }

    let addRules = reactive({
      title: [{ required: true, trigger: 'blur', message: "标题不能为空" }]
    })

    bus.$on('postValueHtml', (val) => {
      let arr_list = []
      addForm.content = val.valueHtml
      for (let item of val.files) {
        for (let item_final of val.finalFiles) {
          if (item.path == item_final.src) {
            arr_list.push({
              name: item.name,
              path: item.path
            })
          }
        }
      }
      addForm.files = arr_list
      // console.log(addForm)
    })
    bus.$on('handleAdd', () => {
      addForm.title = ""
      addForm.id = ""
      addForm.content = ""
      addForm.files.length = 0
      drawerTitle.value = "新增"
      nextTick(() => {
        bus.$emit('getValueHtml', '')
      })
    })
    bus.$on('handleEdit', (selectData) => {
      addForm.id = selectData[0].id
      addForm.content = selectData[0].content
      addForm.title = selectData[0].title
      addForm.files = selectData[0].files
      drawerTitle.value = "修改"
      nextTick(() => {
        bus.$emit('getValueHtml', selectData[0].content)
      })
      // findOne(addForm.id)
    })

    function submitAdd() {
      forms.value.validate((valid) => {
        if (valid) {
          ElMessageBox.confirm('确定提交吗?', '提示', {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (addForm.id != "") {
              workNewsAdd(addForm).then(res => {
                if (res.code === 200) {
                  ElMessage({
                    type: 'success',
                    message: '修改成功！'
                  })
                  dialog.value = false
                  addForm.title = ""
                  addForm.content = ""
                  addForm.id = ""
                  bus.$emit('refreshApi')
                }
              }).catch(err => {

              })
            } else {
              workNewsAdd(addForm).then(res => {
                if (res.code === 200) {
                  ElMessage({
                    type: 'success',
                    message: '新增成功！'
                  })
                }
                dialog.value = false
                addForm.title = ""
                addForm.content = ""
                addForm.id = ""
                bus.$emit('refreshApi')

              }).catch(err => {

              })
            }
          }).catch(() => {
          })
        } else {
          return false
        }
      })
    }

    function cancelForm() {
      dialog.value = false
    }

    onMounted(() => {

    })
    return {
      forms,
      drawerTitle,
      dialog,
      addRules,
      addForm,
      formLoading,
      submitAdd,
      cancelForm,
      actionAddress,
      fileList,
      handleRemove
    }
  }
}
</script>


<style scoped lang="scss">
.custom-add {

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