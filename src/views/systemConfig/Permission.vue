<template>
  <div class="list-contain">
    <el-row :gutter="20">
      <el-col style="margin-bottom: 20px" :span="6" v-for="(item, index) in resData.arrayData">
        <el-card shadow="hover">
          <template #header>
            <span class="card-title">{{ item.label }}</span>

          </template>
          <div v-for="(itemChildren, index) in item.children" style="margin-bottom: 15px;">
            <el-dropdown :key="itemChildren.value" @command="handleCommand" trigger="click" :hide-on-click="false"
              ref="refDrop">
              <span class="card-div">● {{ itemChildren.name }} <span
                  style="font-size: 12px;color: #8c8c8c;font-style:italic">{{ itemChildren.uri }}</span></span>
              <template #dropdown>
                <el-dropdown-menu>

                  <el-dropdown-item :command="{ type: 'edit', id: itemChildren.id }">修改</el-dropdown-item>
                  <el-popover :visible="itemChildren.visibleDelete" placement="right" :width="400" trigger="click">
                    <p>确定要删除这个权限吗？</p>
                    <div style="text-align: right;margin:0">
                      <el-button size="mini" class="el-button--custom"
                        @click="handleDelete({ type: 'delete', id: itemChildren.id, handle: true })">
                        确定
                      </el-button>
                      <el-button size="mini" type="custom-text"
                        @click="handleDelete({ type: 'delete', id: itemChildren.id, handle: false })">取消
                      </el-button>
                    </div>
                    <template #reference>
                      <el-dropdown-item :command="{ type: 'delete', id: itemChildren.id }">删除</el-dropdown-item>
                    </template>
                  </el-popover>

                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="修改权限" v-model="dialogVisible" ref="drawer" :width="400">
      <el-row>
        <el-col :span="24">
          <el-form :model="formDataEdit" :ref="'myFormEdit' + index" label-position="right" label-width="60px">
            <el-form-item label="权限名" prop="name">
              <el-input v-model="formDataEdit.name" placeholder="请输入权限名">
              </el-input>
            </el-form-item>
            <el-form-item label="描述" prop="description">
              <el-input v-model="formDataEdit.description" placeholder="请输入描述">
              </el-input>
            </el-form-item>
            <el-divider content-position="left"></el-divider>
            <div style="text-align: right">

            </div>
          </el-form>
        </el-col>
      </el-row>
      <template #footer>
        <div class="demo-drawer__footer">
          <el-button class="el-button--custom" @click="handleEdit({ type: 'edit', handle: true })">确定
          </el-button>
          <el-button class="el-button-white" @click="handleEdit({ type: 'edit', handle: false })">取消
          </el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import { ref, reactive, provide, inject, onMounted, h, getCurrentInstance, nextTick } from 'vue'
import bus from "@/bus"
import { permissionList, getPermissionById, updatePermission, deletePermission } from '@/api/permission'
import { ElMessage, ElMessageBox } from 'element-plus'


export default {
  name: "List",
  setup() {
    let dialogVisible = ref(false)
    let { proxy } = getCurrentInstance()
    const refDrop = ref < HTMLSelectElement | null > (null)
    const documentName = (index) => {
      nextTick(() => {
        console.log(refDrop.value)
      })
    }


    let resData = reactive({
      arrayData: []
    })

    // let formData = reactive({
    //   description: '',
    //   name: '',
    //   type: '',
    //   url: ''
    // })

    let formDataEdit = reactive({
      description: '',
      name: '',
      uri: '',
      id: '',
      groupName: '',
    })

    function getPermission() {
      permissionList().then(res => {
        if (res.code === 200) {
          resData.arrayData = res.data
          resData.arrayData.map((item) => {
            return item.visible = false
          })
          for (let item of resData.arrayData) {
            item.children.map((item) => {
              item.visibleEdit = false
              item.visibleDelete = false
              return item
            })
          }
        }
      }).catch(err => {

      })
    }

    function handleEdit(command) {
      if (command.handle === true) {
        updatePermission(formDataEdit.id, formDataEdit).then(res => {
          if (res.code === 200) {
            ElMessage.success('修改成功')
            getPermission()
            dialogVisible.value = false

          } else if (res.code === 500) {
            ElMessage.error(res.message)
          }
        }).catch(err => {
        })
      } else {
        dialogVisible.value = false
      }
      changeChildVisible(command, false)
    }

    function handleDelete(command) {
      if (command.handle === true) {
        deletePermission(command.id).then(res => {
          if (res.code === 200) {
            ElMessage.error('删除成功')
            getPermission()
          }
        }).catch(err => {

        })
      }
      changeChildVisible(command, false)
    }

    function handleCommand(command) {
      if (command.type === "edit") {
        dialogVisible.value = true
        getPermissionById(command.id).then(res => {
          formDataEdit.name = res.data.name
          formDataEdit.description = res.data.description
          formDataEdit.uri = res.data.uri
          formDataEdit.id = res.data.id
          formDataEdit.groupName = res.data.groupName
        }).catch(err => {

        })
      }
      changeChildVisible(command, true)
    }

    // function handleAddPermission(para) {
    //   changeVisible(para, true)
    // }
    //
    // function submitAdd(para) {
    //   changeVisible(para, false)
    //
    // }
    //
    // function submitCancel(para) {
    //   changeVisible(para, false)
    // }


    function changeVisible(para, boolean) {
      for (let item of resData.arrayData) {
        if (item.value === para) {
          item.visible = boolean
        }
      }
    }

    function changeChildVisible(para, boolean) {

      for (let item of resData.arrayData) {
        for (let itemChild of item.children) {
          itemChild.visibleEdit = false
          itemChild.visibleDelete = false
        }
      }
      for (let item of resData.arrayData) {
        for (let itemChild of item.children)
          if (itemChild.id === para.id) {
            if (para.type === "edit") {
              itemChild.visibleEdit = boolean
            } else {
              itemChild.visibleDelete = boolean
            }
          }
      }
    }

    onMounted(() => {
      // console.log(proxy._.refs['refDrop1'])
      getPermission()
      document.body.addEventListener('click', () => {
        for (let item of resData.arrayData) {
          for (let itemChild of item.children) {
            itemChild.visibleEdit = false
            itemChild.visibleDelete = false
          }
        }
      }, false)
    })

    return {
      resData,
      handleCommand,
      handleDelete,
      handleEdit,
      formDataEdit,
      refDrop,
      dialogVisible
    }
  }
}
</script>

<style lang="scss">
</style>

<style scoped lang="scss">
.list-contain {

  width: calc(100% - 40px);
  margin: 20px 20px 20px 20px;


  :deep(.el-dialog__body) {
    padding: 30px 20px 10px 20px;
  }

  :deep(.el-card) {
    height: 310px;
    border: none;

    div::-webkit-scrollbar {
      width: 10px;
      height: 10px;
    }

    div::-webkit-scrollbar-thumb {
      background-color: $font-color-theme;
      border-radius: 5px;
    }

    .card-title {
      color: $font-color-theme;
      font-weight: bold;
      font-size: 24px
    }
  }


  :deep(.el-card:hover) {
    box-shadow: 1px 1px 1px 1px #cccccc;
  }

  :deep(.el-card__header) {
    border-bottom: 1px solid #d4d4d4;
  }

  :deep(.el-card__body) {
    overflow-y: auto !important;
    height: 216px;
    padding: 20px 20px 0px 20px;
  }

  :deep(.el-button--text) {
    color: #ead94f;
  }

  .card-div {
    color: $font-color-theme;
    cursor: pointer;
  }


}
</style>