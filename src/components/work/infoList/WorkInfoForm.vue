<template>
  <div class="custom-add">
    <el-dialog :title="drawerTitle" v-model="dialog" ref="drawer" :width="800">
      <el-row>
        <el-col :span="22" :push="1">
          <el-form :model="addForm" ref="forms" :rules="addRules" label-width="100px">
            <el-form-item label="标题" prop="title">
              <el-input v-model="addForm.title" placeholder="输入标题">
              </el-input>
            </el-form-item>

            <el-form-item label="附件上传">

              <el-upload :action="actionAddress" :show-file-list="true" :on-success="handleSuccess" :multiple="true"
                :on-remove="handleRemove" :file-list="fileList" ref="file">
                <el-button size="small" type="success">点击上传</el-button>
                <div slot="tip" class="el-upload_tip">文件大小不超过200MB</div>
              </el-upload>

            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="addForm.remark" placeholder="输入备注">
              </el-input>
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
import { ref, reactive, inject, onMounted } from 'vue'
import { workInfoAdd } from '@/api/work'
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"
import moment from 'moment'

export default {
  name: "WorkInfoForm",
  setup() {
    let forms = ref(null)
    let drawerTitle = ref('新增')
    let dialog = inject('dialog')
    let formLoading = ref(false)
    let uploadFileList = ref([])
    let addForm = reactive({
      id: "",
      title: "",
      remark: "",
      files: []
    })
    ///附件上传
    let actionAddress = process.env.VUE_APP_UPLOAD_URL

    let fileList = ref([])

    const handleRemove = (file, fileList) => {
      console.log('handleChange', fileList)
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

    function handleSuccess(res) {

      if (res.code === 200) {
        uploadFileList.value.push({
          name: res.data[0].name,
          path: res.data[0].path
        })
        // fileList.value = uploadFileList.value
      }
      addForm.files.length = 0

      console.log(uploadFileList.value)
      for (let item of uploadFileList.value) {
        addForm.files.push({
          name: item.name,
          path: item.path
        })
      }
    }


    let addRules = reactive({
      title: [{ required: true, trigger: 'blur', message: "标题不能为空" }]
    })

    bus.$on('handleAdd', () => {
      addForm.title = ""
      addForm.remark = ""
      addForm.id = ""
      fileList.value.length = 0
      addForm.files = []
      drawerTitle.value = "新增"
    })

    bus.$on('handleEdit', (selectData) => {

      fileList.value.length = 0
      addForm.files.length = 0
      addForm.id = selectData[0].id
      addForm.remark = selectData[0].remark
      addForm.title = selectData[0].title
      for (let item of selectData[0].files) {
        addForm.files.push({
          name: item.name,
          path: item.path
        })
        fileList.value.push({
          name: item.name,
          url: item.path
        })
      }
      drawerTitle.value = "修改"
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
              workInfoAdd(addForm).then(res => {
                if (res.code === 200) {
                  ElMessage({
                    type: 'success',
                    message: '修改成功！'
                  })
                  dialog.value = false
                  addForm.title = ""
                  addForm.remark = ""
                  addForm.files.length = 0
                  fileList.value.length = 0
                  bus.$emit('refreshApi')
                }
              }).catch(err => {

              })
            } else {
              workInfoAdd(addForm).then(res => {
                if (res.code === 200) {
                  ElMessage({
                    type: 'success',
                    message: '新增成功！'
                  })
                }
                dialog.value = false
                addForm.title = ""
                addForm.remark = ""
                addForm.files.length = 0
                fileList.value.length = 0
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
      handleSuccess,
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