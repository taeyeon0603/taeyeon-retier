<template>
  <div class="custom-add">
    <el-drawer :title="drawerTitle" v-model="dialog" direction="rtl" ref="drawer">
      <el-row>
        <el-col :span="22" :push="1">
          <el-form :model="addForm" ref="forms" :rules="addRules" label-width="100px">

            <el-form-item label="用户名" prop="username">
              <el-input v-model="addForm.username" placeholder="请输入用户名">
              </el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="addForm.password" placeholder="请输入密码">
              </el-input>
            </el-form-item>
            <el-form-item label="重复密码" prop="password2">
              <el-input v-model="addForm.password2" placeholder="请输入重复密码">
              </el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="addForm.name" placeholder="请输入姓名">
              </el-input>
            </el-form-item>
            <el-form-item label="部门" prop="deptId">
              <el-select v-model="addForm.deptId" placeholder="请选择部门">
                <el-option v-for="(item, index) in deptList" :key="index" :label="item.deptName" :value="item.deptId">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="工号" prop="policeNum">
              <el-input v-model="addForm.policeNum" placeholder="请输入工号">
              </el-input>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <el-divider content-position="left"></el-divider>
      <div class="demo-drawer__footer">

        <el-button class="el-button--custom" @click="submitAdd" :loading="formLoading">{{ formLoading ?
            '提交中...' : '确定'
        }}
        </el-button>
        <el-button class="el-button-white" @click="cancelForm">取消</el-button>
      </div>

    </el-drawer>
  </div>
</template>

<script>
import { ref, reactive, inject, onMounted } from 'vue'
import { addUser, updateUser, getUserById, getDeptList } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"

export default {
  name: "AppAdd",
  setup() {
    let forms = ref(null)
    let drawerTitle = ref('新增')
    let dialog = inject('dialog')
    let formLoading = ref(false)
    let deptList = ref([])
    let password2 = ref("")

    let addForm = reactive({
      id: "",
      deptId: "",
      name: "",
      policeNum: "",
      username: "",
      password: "",
      password2: "",
    })


    const validatePass2Add = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('二次密码不能为空'))
      } else if (value !== addForm.password) {
        callback(new Error('重复密码不匹配'))
      } else {
        callback()
      }
    }
    const validatePass2Edit = (rule, value, callback) => {
      if (addForm.password != "" && value !== addForm.password) {
        callback(new Error('重复密码不匹配'))
      } else {
        callback()
      }
    }

    let addRules = reactive({
      username: [{ required: true, trigger: 'blur', message: "不能为空" }],
      name: [{ required: true, trigger: 'blur', message: "不能为空" }],
      deptId: [{ required: true, trigger: 'change', message: "不能为空" }],
      password: [{ required: true, trigger: 'change', message: "不能为空" }],
      password2: [{ validator: validatePass2Add, trigger: 'blur' }],
    })

    bus.$on('handleAdd', () => {
      addForm.name = ""
      addForm.deptId = ""
      addForm.id = ""
      addForm.policeNum = ""
      addForm.username = ""
      drawerTitle.value = "新增"
      addRules.password = [{ required: true, trigger: 'change', message: "不能为空" }]
      addRules.password2 = [{ validator: validatePass2Add, trigger: 'blur' }]
    })

    bus.$on('handleEdit', (selectData) => {
      addForm.id = selectData[0].id
      drawerTitle.value = "修改"
      findOne(addForm.id)
      addRules.password = []
      addRules.password2 = [{ validator: validatePass2Edit, trigger: 'blur' }]

    })

    //获取单个应用
    function findOne(id) {
      getUserById(id).then(res => {
        if (res.code === 200) {
          addForm.name = res.data.name
          addForm.username = res.data.username
          addForm.deptId = res.data.deptId
          addForm.id = res.data.id
          addForm.policeNum = res.data.policeNum
        }
      }).catch(err => {
      })
    }

    function getDept() {
      getDeptList().then(res => {
        if (res.code === 200) {
          deptList.value = res.data
        }
      }).catch(err => {

      })
    }

    function submitAdd() {
      forms.value.validate((valid) => {
        if (valid) {
          ElMessageBox.confirm('确定提交吗?', '提示', {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {

            if (addForm.id != "") {
              updateUser(addForm.id, addForm).then(res => {
                if (res.code === 200) {
                  ElMessage({
                    type: 'success',
                    message: '修改成功！'
                  })
                  dialog.value = false
                  addForm.name = ""
                  addForm.password = ""
                  addForm.password2 = ""
                  addForm.username = ""
                  addForm.deptId = ""
                  addForm.id = ""
                  addForm.policeNum = ""
                  bus.$emit('refreshApi')
                }
              }).catch(err => {

              })
            } else {
              addUser(addForm).then(res => {
                if (res.code === 200) {
                  ElMessage({
                    type: 'success',
                    message: '新增成功！'
                  })
                }
                dialog.value = false
                addForm.name = ""
                addForm.password = ""
                addForm.password2 = ""
                addForm.username = ""
                addForm.deptId = ""
                addForm.id = ""
                addForm.policeNum = ""
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

    function handleSuccess(res, file) {

      if (res.code === 200) {
        addForm.imgUrl = res.data
      }
      console.log('handleSuccess,res', res)
      console.log('handerSuccess,file', file)
    }

    function beforeUpload(file) {
      console.log('beforeUpload', file)
    }

    onMounted(() => {
      getDept()
    })


    return {
      forms,
      drawerTitle,
      dialog,
      addRules,
      addForm,
      formLoading,
      handleSuccess,
      beforeUpload,
      submitAdd,
      cancelForm,
      deptList,
      password2
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