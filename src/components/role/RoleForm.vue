<template>
  <div class="custom-add">
    <el-drawer title="新增" v-model="dialog" direction="rtl" ref="drawer">
      <el-row>
        <el-col :span="22" :push="1">
          <el-form :model="addForm" ref="forms" :rules="addRules" label-width="100px">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="addForm.name" placeholder="请输入系统全称"> </el-input>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <el-divider content-position="left"></el-divider>
      <div class="demo-drawer__footer">
        <el-button class="el-button--custom" @click="submitAdd" :loading="formLoading">{{ formLoading ? "提交中..." : "确定" }} </el-button>
        <el-button class="el-button-white" @click="cancelForm">取消</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { ref, reactive, inject } from "vue"
import { addRole, getRoleById, updateRole } from "@/api/role"
import { ElMessage, ElMessageBox } from "element-plus"
import bus from "@/bus"

export default {
  name: "RoleAdd",
  setup() {
    let forms = ref(null)
    let drawerTitle = ref("新增")
    let dialog = inject("dialog")
    let formLoading = ref(false)

    let addForm = reactive({
      id: 0,
      name: "",
    })
    let addRules = reactive({
      name: [{ required: true, trigger: "blur", message: "不能为空" }],
    })
    bus.$on("handleAdd", () => {
      addForm.name = ""
      addForm.id = 0
    })

    bus.$on("handleEdit", (selectData) => {
      addForm.id = selectData[0].id
      findOne(addForm.id)
    })

    //获取单个应用
    function findOne(id) {
      getRoleById(id)
        .then((res) => {
          if (res.code === 200) {
            addForm.name = res.data.name
            addForm.id = res.data.id
          }
        })
        .catch((err) => {})
    }

    function submitAdd() {
      forms.value.validate((valid) => {
        if (valid) {
          ElMessageBox.confirm("确定提交吗?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
            .then(() => {
              if (addForm.id > 0) {
                updateRole(addForm.id, addForm)
                  .then((res) => {
                    if (res.code === 200) {
                      ElMessage({
                        type: "success",
                        message: "修改成功！",
                      })
                      dialog.value = false
                      addForm.name = ""
                      addForm.id = 0
                      bus.$emit("refreshApi")
                    }
                  })
                  .catch((err) => {})
              } else {
                addRole(addForm)
                  .then((res) => {
                    if (res.code === 200) {
                      ElMessage({
                        type: "success",
                        message: "新增成功！",
                      })
                      dialog.value = false
                      addForm.name = ""
                      addForm.id = 0
                      bus.$emit("refreshApi")
                    }
                  })
                  .catch((err) => {})
              }
            })
            .catch(() => {})
        } else {
          return false
        }
      })
    }

    function cancelForm() {
      dialog.value = false
    }

    return {
      forms,
      drawerTitle,
      dialog,
      addRules,
      addForm,
      formLoading,
      submitAdd,
      cancelForm,
    }
  },
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
